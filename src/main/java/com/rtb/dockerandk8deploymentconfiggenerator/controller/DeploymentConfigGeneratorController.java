package com.rtb.dockerandk8deploymentconfiggenerator.controller;

import com.rtb.dockerandk8deploymentconfiggenerator.model.DeploymentRequest;
import com.rtb.dockerandk8deploymentconfiggenerator.service.DeploymentConfigAiGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/generator")
@RequiredArgsConstructor
public class DeploymentConfigGeneratorController {

    private final DeploymentConfigAiGenerationService deploymentConfigAiGenerationService;
    public static final String CONTENT_DISPOSITION_HEADER_VALUE_FOR_ZIP_FILE = "attachment; filename=\"%s.zip\"";

    @PostMapping(value = "/k8-deployment-configs", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> zipYamlsFromText(@RequestBody DeploymentRequest deploymentRequest) throws IOException {

        byte[] zipBytes = deploymentConfigAiGenerationService.getK8ConfigZip(deploymentRequest);

        ByteArrayResource resource = new ByteArrayResource(zipBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_HEADER_VALUE_FOR_ZIP_FILE, deploymentRequest.getAppName()));
        headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipBytes.length)
                .body(resource);
    }
}
