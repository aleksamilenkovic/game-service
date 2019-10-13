package com.gameservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@MapperScan(basePackages = { "com.mozzartbet.gameservice.mapper" })
@EnableAsync
public class BaseConfig {

}
