package com.green.movie_demo;

import com.green.movie_demo.config.WxMappingJackson2HttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableFeignClients
@SpringBootApplication
public class MovieDemoApplication
{
//    @Bean
//    public WebMvcConfigurerAdapter corsConfigurer()
//    {
//        return new WebMvcConfigurerAdapter()
//        {
//            @Override
//            public void addCorsMappings(CorsRegistry registry)
//            {
//                registry.addMapping("/frontend-config").allowedOrigins("http://localhost:8082");
//            }
//        };
//    }
    
    public static void main(String[] args)
    {
        SpringApplication.run(MovieDemoApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
