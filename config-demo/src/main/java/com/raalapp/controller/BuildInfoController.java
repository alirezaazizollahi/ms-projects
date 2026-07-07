package com.raalapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RefreshScope
@RestController
public class BuildInfoController {

    @Value("${build.id}")
    private String buildId;
    @Value("${build.version}")
    private String buildVersion;
    @Value("${build.name}")
    private String buildName;

    @GetMapping("/build-info")
    public String getBuildInfo(){

        Map<String, Function<Map<String, Object>, Object>> tools = new HashMap<>();
        Map.of(
                "tools", tools.keySet()
        );


        return "Build Id: " + buildId + ", Version: " + buildVersion + ", Name: " + buildName;
    }
}
