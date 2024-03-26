package com.vitcheu.common.constants.api.rate_limiting;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

public interface APIUsageTiersService {
  public Bucket resolveBucket(String apiKey);

  public Bucket newBucket(String apiKey);

  public Bucket bucket(Bandwidth limit);
}
