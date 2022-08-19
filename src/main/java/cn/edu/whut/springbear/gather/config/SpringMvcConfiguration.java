package cn.edu.whut.springbear.gather.config;

import cn.edu.whut.springbear.gather.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
     * Interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // LoginInterceptor: Intercept all requests except some special request
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login.do", "/logout.do", "/reset.do", "/email.do");
    }
}
