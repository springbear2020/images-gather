package cn.edu.whut.springbear.gather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 22:21 Wednesday
 */
@Configuration
@EnableWebMvc
@ComponentScan(value = {"cn.edu.whut.springbear.gather.controller"})
public class SpringMvcConfiguration implements WebMvcConfigurer {
    /**
     * File upload resolver
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    /**
     * Access to XMLHttpRequest
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}
