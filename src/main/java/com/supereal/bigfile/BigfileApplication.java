package com.supereal.bigfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author bitmain
 */
@SpringBootApplication
public class BigfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigfileApplication.class, args);
    }



    /**
     * 用于第三方调用
     */
    @Bean(name="remoteRestTemplate")
    public RestTemplate remoteRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60000);

        return new RestTemplate(requestFactory);
    }

}

