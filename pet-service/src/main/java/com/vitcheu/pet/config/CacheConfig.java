package com.vitcheu.pet.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  public static final String CACHE_PET_BASE = "pets:";

  public static final String CACHE_SINGLE_PET = CACHE_PET_BASE + "single-pet";

  public static final String CACHE_MULTI_PETS = CACHE_PET_BASE + "multi-pets";

  public static final String CACHE_PET_TYPES = CACHE_PET_BASE + "petTypes";

  public static final String CACHE_PET_TYPE= CACHE_PET_BASE + "petType";
}
