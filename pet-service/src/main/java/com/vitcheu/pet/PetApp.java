package com.vitcheu.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PetApp {

  public static void main(String[] args) {
    SpringApplication.run(PetApp.class, args);
  }
}
