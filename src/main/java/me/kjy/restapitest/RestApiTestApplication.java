package me.kjy.restapitest;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApiTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiTestApplication.class, args);
    }

    // 빈으로 등록함
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
