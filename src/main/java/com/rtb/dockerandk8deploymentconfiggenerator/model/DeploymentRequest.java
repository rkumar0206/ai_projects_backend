package com.rtb.dockerandk8deploymentconfiggenerator.model;

import com.rtb.dockerandk8deploymentconfiggenerator.enums.Framework;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeploymentRequest{
        private Framework framework;
        private String appName;
        private String tag;
        private DockerfileConfig dockerfileConfig;
        private int port;
        private Map<String, String> envVariables;
        private Map<String, String> secrets;
        private int replicas;
        private ResourceLimits resourceLimits;
        private boolean enableIngress;
        private String ingressHost;
        private boolean enableHPA;
        private HpaConfig hpa;
}
