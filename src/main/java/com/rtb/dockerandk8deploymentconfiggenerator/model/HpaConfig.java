package com.rtb.dockerandk8deploymentconfiggenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HpaConfig {
    private int minReplicas;
    private int maxReplicas;
    private int targetCPUUtilizationPercentage;
}
