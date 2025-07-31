package com.nmmc;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = {"com.nmmc.hrms.feignclient"})
@Configuration
public class BaseConfiguration {

}
