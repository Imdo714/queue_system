package com.queue.global.config

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Component

@Component
class RedisShutdownHook(
    private val redisConnectionFactory: RedisConnectionFactory
) : ApplicationListener<ContextClosedEvent> {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: ContextClosedEvent) {
        try {
            redisConnectionFactory.connection.use { it.serverCommands().flushDb() }
            log.info("Redis FLUSHDB 완료 — 서버 종료 전 초기화")
        } catch (e: Exception) {
            log.error("Redis FLUSHDB 실패: ${e.message}")
        }
    }
}
