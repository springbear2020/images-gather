package edu.whut.springbear.gather.config;

import edu.whut.springbear.gather.interceptor.AdminInterceptor;
import edu.whut.springbear.gather.interceptor.LoginInterceptor;
import edu.whut.springbear.gather.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 14:36 Thursday
 */
@Configuration
@EnableWebMvc
@ComponentScan(value = {"edu.whut.springbear.gather.controller"})
public class SpringMvcConfiguration implements WebMvcConfigurer {
    /**
     * Default servlet handler to handle with the static resources link .ccs, .js and so on
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /**
     * The next three methods is for view resolver
     */
    @Bean
    public ITemplateResolver templateResolver() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        assert webApplicationContext != null;
        ServletContext servletContext = webApplicationContext.getServletContext();
        assert servletContext != null;
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }

    /**
     * View controllers
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/record.html").setViewName("record");
        registry.addViewController("/reset.html").setViewName("reset");
        registry.addViewController("/user/complete.html").setViewName("user/complete");
        registry.addViewController("/admin/class.html").setViewName("admin/class");
    }

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
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/", "/login", "/logout", "/reset.html", "/reset", "/email", "/static/**");
        // UserInterceptor: Intercept user resources
        registry.addInterceptor(new UserInterceptor()).addPathPatterns("/user/**");
        // AdminInterceptor: Intercept admin resources
        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin/**");
    }
}