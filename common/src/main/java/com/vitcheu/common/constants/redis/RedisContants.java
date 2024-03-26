
package com.vitcheu.common.constants.redis;

public class RedisContants {

    public static final String CACHE_NAME_AUTH = "auth";

    public static final String AUTH_KEY_PREFIX = CACHE_NAME_AUTH+":";

    public static final long AUTH_EXPIRE_TIME = 10*60L;

    public static final String CACHE_NAME_REFRESH_TOKEN = "refreshToken";

    public static final String CACHE_AUTH_KEY = "#username";

    public static final String DELEMITOR = ":";
}
