package com.vibecoding.shop.infrastructure.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class QueryDslConfig {

    @PersistenceContext EntityManager em;

    @Bean public JPAQueryFactory jpaQueryFactory() { return new JPAQueryFactory(em); }

    @Bean(name = "eventExecutor")
    public Executor eventExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(5); ex.setMaxPoolSize(10);
        ex.setQueueCapacity(500); ex.setThreadNamePrefix("Event-");
        ex.initialize();
        return ex;
    }
}
