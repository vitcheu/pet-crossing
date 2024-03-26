package com.vitcheu.common.constants.api.rate_limiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class APIUsageTiersServiceImpl implements APIUsageTiersService {

  private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

  @Override
  public Bucket resolveBucket(String apiKey) {
    return this.cache.computeIfAbsent(apiKey, this::newBucket);
  }

  @Override
  public Bucket newBucket(String apiKey) {
    APIUsageTiersEnum pricingPlan = APIUsageTiersEnum.resolvePlanFromApiKey(
      apiKey
    );

    return this.bucket(pricingPlan.getLimit());
  }

  @Override
  public Bucket bucket(Bandwidth limit) {
    return Bucket4j.builder().addLimit(limit).build();
  }
}
