package com.hack2win.dynamicrabbits.dynamic.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * spring cache redis配置
 *
 * @author dongerkai
 * @since 2019/1/14 13:30 ，1.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "redis-cache")
public class RedisCacheProperties {

    private DefaultConfig defaultConfig;
    private Map<String,KeyConfig> keyConfigs;


    @Data
    public static class AutoKey{
        private String name;
        private Duration duration;
    }

    @SuppressWarnings("WeakerAccess")
    @Data
    public static class DefaultConfig{
        private String keyPrefix;
        private Duration duration;
    }

    @SuppressWarnings("WeakerAccess")
    @Data
    public static class KeyConfig{
        private Duration duration;
    }

    @SuppressWarnings("WeakerAccess")
    @Data
    public static class Duration{
        private ChronoUnit unit;
        private long time;
    }

}
