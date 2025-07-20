package com.rtb.dockerandk8deploymentconfiggenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DockerfileConfig {

    private String baseImage;
    private String artifactName;
    private Map<String, String> labels;
    private Map<String, String> arguments;
}
