//package com.noman.order_service.com.noman.discoveryserver.config;
//
//import feign.Logger;
//import feign.Request;
//import feign.RequestInterceptor;
//import feign.Retryer;
//import feign.auth.BasicAuthRequestInterceptor;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//@EnableFeignClients(basePackages = "com.noman.order_service.client")
//public class InventoryFeignClientConfig {
//
//    @Bean
//    public Logger.Level loggerLevel() {
//        return Logger.Level.FULL;
//    }
//
//    @Bean
//    public Retryer retryer() {
//        return new Retryer.Default(1000, 2000, 4);
//    }
//
//    @Bean
//    public Request.Options options() {
//        return new Request.Options(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS, true);
//    }
//}
