package com.vitcheu.common.constants.api.rate_limiting;

import java.time.Duration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public enum APIUsageTiersEnum {
  FREE(25),
  BASIC(50),
  PROFESSIONAL(75);

  private int bucketCapacity;

  public Bandwidth getLimit() {
    APIUsageTiersEnum.log.info(
      "-----> API Rate Limiting : Bandwidth : " +
      Bandwidth.classic(
        this.bucketCapacity,
        Refill.intervally(this.bucketCapacity, Duration.ofMinutes(20))
      )
    );

    return Bandwidth.classic(
      this.bucketCapacity,
      Refill.intervally(this.bucketCapacity, Duration.ofMinutes(20))
    );
    //return Bandwidth.simple(1, Duration.ofSeconds(30));
  }

  public int getBucketCapacity() {
    APIUsageTiersEnum.log.info(
      "-----> API Rate Limiting : bucketCapacity : " + this.bucketCapacity
    );

    return this.bucketCapacity;
  }

  public static APIUsageTiersEnum resolvePlanFromApiKey(String apiKey) {
    if (apiKey == null || apiKey.isEmpty()) {
      APIUsageTiersEnum.log.info(
        "-----> API Rate Limiting : API Usage Tier : FREE 25"
      );
      return FREE;
    } else if (apiKey.startsWith("PX001-")) {
      APIUsageTiersEnum.log.info(
        "-----> API Rate Limiting : API Usage Tier : PROFESSIONAL 75"
      );
      return PROFESSIONAL;
    } else if (apiKey.startsWith("BX001-")) {
      APIUsageTiersEnum.log.info(
        "-----> API Rate Limiting : API Usage Tier : BASIC 50"
      );
      return BASIC;
    }

    return FREE;
  }
}
