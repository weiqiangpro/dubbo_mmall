package com.wq.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Author: weiqiang
 * Time: 2020/4/10 上午11:37
 */

@EnableJpaAuditing
@SpringBootApplication
@EnableSwagger2
@EnableDiscoveryClient
public class AllApplication {
    public static void main(String[] args) {
        SpringApplication.run(AllApplication.class, args);
    }
}