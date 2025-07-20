package com.rtb.dockerandk8deploymentconfiggenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceLimits {

    private String cpu;
    private String memory;
}
