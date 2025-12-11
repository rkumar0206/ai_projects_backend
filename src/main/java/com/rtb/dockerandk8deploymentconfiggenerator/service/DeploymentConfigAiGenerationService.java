package com.rtb.dockerandk8deploymentconfiggenerator.service;

import com.rtb.common.service.CommonService;
import com.rtb.dockerandk8deploymentconfiggenerator.enums.Framework;
import com.rtb.dockerandk8deploymentconfiggenerator.model.DeploymentRequest;
import com.rtb.dockerandk8deploymentconfiggenerator.model.DockerfileConfig;
import com.rtb.dockerandk8deploymentconfiggenerator.model.ResourceLimits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class DeploymentConfigAiGenerationService {

    private final CommonService commonService;

    public byte[] getK8ConfigZip(DeploymentRequest deploymentRequest) throws IOException {

        DeploymentRequest dr = generateTempDeploymentRequest();

        if (deploymentRequest != null) {
            dr = deploymentRequest;
        }

        String prompt = buildPrompt(dr);

        String rawText = commonService.getPromptTextResult(prompt);
        System.out.println(rawText);

        Map<String, String> files = new LinkedHashMap<>();

        // Extract YAML files
        files.putAll(extractFilesByLanguage(rawText, dr.getAppName(), "yaml"));

        // Extract Dockerfile (if present)
        Map<String, String> dockerMap = extractFilesByLanguage(rawText, dr.getAppName(), "dockerfile");
        if (!dockerMap.isEmpty()) {
            files.put("Dockerfile", dockerMap.values().iterator().next()); // Only one Dockerfile expected
        }

        System.out.println(files);

        byte[] bytes = zipYamlFiles(files);

//        Path zipFilePath = Paths.get("src/main/resources/static/output.zip");
//        Files.write(zipFilePath, bytes);
        return bytes;
    }

    private DeploymentRequest generateTempDeploymentRequest() {

        return new DeploymentRequest(
                Framework.SPRINGBOOT,
                "utility",
                "v1",
                new DockerfileConfig(
                        "latest gradle image with jdk 21",
                        "utility-0.0.1-SNAPSHOT.jar",
                        Map.of("quay.expires-after", "1y"),
                        null
                ),
                8099,
                Map.of(
                        "SPRING_DATASOURCE_URL", "jdbc:postgresql://utility-db-service:5432/utilityDb"
                ),
                Map.of(
                        "DB_USERNAME", "utilityadmin",
                        "DB_PASSWORD", "secret"
                ),
                3,
                new ResourceLimits("500m", "512Mi"),
                true,
                "utility.rtb.com",
                false,
                null
        );

    }

    private String buildPrompt(DeploymentRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("You are a DevOps assistant. Based on the following application details, generate everything required to containerize and deploy a %s app to Kubernetes.%n%n", req.getFramework().name()));

        if (req.getFramework().equals(Framework.SPRINGBOOT)) {

            sb.append("Dockerfile configuration: ")
                    .append(" - It should be 2 staged docker file, 1st should be the build using gradle or maven (default is gradle) and jdk and 2nd should be an alpine image (or override this if user has provided any other value in consideration)\n")
                    .append(" - docker file consideration: \n")
                    .append(req.getDockerfileConfig().toString()).append("\n").append("port: ").append(req.getPort()).append("\n\n");
        }

        sb.append("App name: ").append(req.getAppName()).append("\n");
        sb.append("Exposed port: ").append(req.getPort()).append("\n");
        sb.append("Environment variables: ").append(req.getEnvVariables()).append("\n");
        sb.append("Secrets (only use names and bse64 encrypted value): ").append(req.getSecrets()).append("\n");
        sb.append("Replicas: ").append(req.getReplicas()).append("\n");
        sb.append("Resource limits: ").append(req.getResourceLimits().getCpu()).append(" CPU, ").append(req.getResourceLimits().getMemory()).append(" memory\n");
        if (req.isEnableIngress()) {
            sb.append("Enable Ingress: true\n");
            sb.append("Ingress host: ").append(req.getIngressHost()).append("\n");
        }
        if (req.isEnableHPA() && req.getHpa() != null) {
            sb.append("Enable HPA: true\n");
            sb.append("Min replicas: ").append(req.getHpa().getMinReplicas()).append("\n");
            sb.append("Max replicas: ").append(req.getHpa().getMaxReplicas()).append("\n");
            sb.append("Target CPU Utilization: ").append(req.getHpa().getTargetCPUUtilizationPercentage()).append("%\n");
        }
        sb.append("\nPlease generate:\n");
        sb.append("1. Dockerfile\n");
        sb.append("2. Kubernetes ConfigMap YAML\n");
        sb.append("3. Kubernetes Secret YAML\n");
        sb.append("4. Kubernetes Deployment YAML\n");
        sb.append("5. Kubernetes Service YAML\n");

        if (req.isEnableIngress()) {
            sb.append("6. Ingress YAML\n");
        }

        if (req.isEnableHPA()) {
            sb.append("7. HPA YAML\n\n");
        }

        sb.append("8. Commands YAML: list of kubectl commands in sequence the config files should be executed. The filenames should be the appName-yamlType\\n");

        sb.append("Each yaml file heading should be ```yaml and for dockerfile it should be ```dockerfile. Also each file should be seperated by ---");
        return sb.toString();
    }

    private Map<String, String> extractFilesByLanguage(String input, String appName, String lang) {
        Map<String, String> fileMap = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("```" + lang + "\\s*([\\s\\S]+?)\\s*```", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        int count = 1;
        while (matcher.find()) {
            String content = matcher.group(1).trim();
            String fileName = lang.equalsIgnoreCase("yaml") ? guessYamlFileName(content, appName, count++) : "Dockerfile";
            fileMap.put(fileName, content);
        }

        return fileMap;
    }

    private String guessYamlFileName(String yaml, String appName, int fallbackId) {
        if (yaml.contains("kind: Deployment")) return appName + "-deployment.yaml";
        if (yaml.contains("kind: Service")) return appName + "-service.yaml";
        if (yaml.contains("kind: ConfigMap")) return appName + "-configmap.yaml";
        if (yaml.contains("kind: Secret")) return appName + "-secret.yaml";
        if (yaml.contains("kind: Ingress")) return appName + "-ingress.yaml";
        if (yaml.contains("kind: HorizontalPodAutoscaler")) return appName + "-hpa.yaml";
        return "unknown-" + fallbackId + ".yaml";
    }

    private byte[] zipYamlFiles(Map<String, String> files) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);

        for (Map.Entry<String, String> entry : files.entrySet()) {
            ZipEntry zipEntry = new ZipEntry(entry.getKey());
            zipOut.putNextEntry(zipEntry);
            zipOut.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
            zipOut.closeEntry();
        }

        zipOut.close();
        return bos.toByteArray();
    }
}
