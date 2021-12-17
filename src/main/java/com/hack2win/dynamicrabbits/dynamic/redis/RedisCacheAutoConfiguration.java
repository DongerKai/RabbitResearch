package com.hack2win.dynamicrabbits.dynamic.redis;

import com.hack2win.dynamicrabbits.dynamic.utils.JsonUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spring cache 配置
 * spring Session 配置
 * redis  配置
 *
 * @author DongerKai
 * @since 2019/1/14 13:30 ，1.0
 **/
@Slf4j
@Configuration
@EnableCaching
@AutoConfigureAfter(RedisAutoConfiguration.class)
@RequiredArgsConstructor
public class RedisCacheAutoConfiguration {
    @NonNull private final RedisCacheProperties redisCacheProperties;

    /**
     * 可以把对象存储进去，但取出为LinkedHashMap，不能直接强转
     */
    @Bean("redisCacheTemplate")
    public RedisTemplate<String, Object> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(JsonUtils.mapper));
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheProperties.DefaultConfig defaultConfig = redisCacheProperties.getDefaultConfig();
        RedisCacheProperties.Duration duration = defaultConfig.getDuration();
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.of(duration.getTime(),duration.getUnit()))
                .computePrefixWith(cacheName -> "hack"+cacheName+ "-")
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(JsonUtils.serializeMapper)));
    }

    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory redisConnectionFactory,RedisCacheConfiguration redisCacheConfiguration) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(redisCacheAutoConfig(redisCacheConfiguration))
                .transactionAware()
                .build();
    }

    /**
     * 对每一个缓存单独配置TTL
     */
    private Map<String,RedisCacheConfiguration> redisCacheAutoConfig(RedisCacheConfiguration redisCacheConfiguration){
        if(CollectionUtils.isEmpty(redisCacheProperties.getKeyConfigs()))
            return Collections.emptyMap();
        return redisCacheProperties.getKeyConfigs().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e->{
            RedisCacheProperties.Duration duration = e.getValue().getDuration();
            return redisCacheConfiguration.entryTtl(Duration.of(duration.getTime(),duration.getUnit()));
        }));
    }
}
