package com.sailing.es.test.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description fegin的日志
 * @Author Administrator
 * @Date 2019/11/7 18:05
 * @Version 1.0
 **/
@Configuration
public class FeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel(){
        //这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.FULL;
    }
}
