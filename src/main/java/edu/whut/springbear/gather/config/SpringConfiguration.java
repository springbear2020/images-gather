package edu.whut.springbear.gather.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 14:36 Thursday
 */
@Configuration
@Import(MyBatisConfiguration.class)
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"edu.whut.springbear.gather.mapper",
        "edu.whut.springbear.gather.service", "edu.whut.springbear.gather.util",})
public class SpringConfiguration {
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    /**
     * Druid data source
     */
    @Bean
    public DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }
}