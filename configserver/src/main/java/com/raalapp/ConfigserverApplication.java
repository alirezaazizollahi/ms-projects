package com.raalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigserverApplication {

	public static void main(String[] args) {

        String aa = "salam/khobi?";
        System.out.println(aa.split("/"));
		SpringApplication.run(ConfigserverApplication.class, args);
	}

}
