package com.vitcheu.prop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.vitcheu.prop.config.PropsProperties;


@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(PropsProperties.class)
public class PropApp {

	public static void main(String[] args) {
		SpringApplication.run(PropApp.class, args);
	}
}
