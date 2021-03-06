package com.wq.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Author: weiqiang
 * Time: 2020/4/4 下午5:46
 */

@EnableJpaAuditing
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableSwagger2
@EnableDiscoveryClient
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class,args);
    }
}
