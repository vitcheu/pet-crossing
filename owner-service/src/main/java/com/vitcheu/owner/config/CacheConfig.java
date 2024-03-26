package com.vitcheu.owner.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
@Profile({
  "production",
  "dev"
})
public class CacheConfig {

  public static final String CACHE_OWNER_BASE = "owner:";

  public static final String CACHE_OWNER_ALL_PETS = CACHE_OWNER_BASE + "pets";

  public static final String CACHE_OWNER_REPO = CACHE_OWNER_BASE + "one";

  public static final String CACHE_REMOTE = CACHE_OWNER_BASE + "remote:";

  public static final String CACHE_REMOTE_SINGLE_PET =
    CACHE_REMOTE + "single-pet";

  public static final String CACHE_REMOTE_MULTI_PET =
    CACHE_REMOTE + "multi-pet";

  public static final String CACHE_REMOTE_PROFILE = CACHE_REMOTE + "propfile";

  public static final String SINGLE_PROP = CACHE_REMOTE + "single-prop";
}
