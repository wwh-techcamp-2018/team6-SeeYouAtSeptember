package com.woowahan.moduchan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:aws.properties", ignoreResourceNotFound = true)
public class AmazonConfig {
}