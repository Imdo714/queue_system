package com.queue.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Value("\${spring.data.redis.host}")
    private lateinit var host: String

    @Value("\${spring.data.redis.port}")
    private var port: Int = 6379

    @Value("\${spring.data.redis.password:}")
    private var password: String = ""

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val config = RedisStandaloneConfiguration(host, port)
        if (password.isNotBlank()) {
            config.password = RedisPassword.of(password)
        }

        // Upstash는 TLS 연결이 필수라 설정
        val clientConfig = LettuceClientConfiguration.builder()
            .useSsl()                    // SSL 활성화
            .disablePeerVerification()   // 인증서 검증 생략
            .build()

        return LettuceConnectionFactory(config)
    }

    /**
     * 키·값 모두 String 직렬화 — Lua 스크립트와 수동 JSON 직렬화 방식과 호환.
     * Jackson 3.x 의 GenericJackson2JsonRedisSerializer 호환 여부 불확실성을 피하기 위해
     * StringRedisSerializer + ObjectMapper 방식을 채택한다.
     */
    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        return RedisTemplate<String, String>().apply {
            connectionFactory = redisConnectionFactory
            val stringSerializer = StringRedisSerializer()
            keySerializer = stringSerializer
            valueSerializer = stringSerializer
            hashKeySerializer = stringSerializer
            hashValueSerializer = stringSerializer
        }
    }
}
