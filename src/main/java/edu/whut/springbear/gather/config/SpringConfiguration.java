package edu.whut.springbear.gather.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
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
@ComponentScan(basePackages = {"edu.whut.springbear.gather.pojo", "edu.whut.springbear.gather.mapper",
        "edu.whut.springbear.gather.service", "edu.whut.springbear.gather.util",
        "edu.whut.springbear.gather.exception", "edu.whut.springbear.gather.interceptor"})
public class SpringConfiguration {
    /**
     * Database connection info
     */
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    /**
     * Qiniu access key
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;

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

    /**
     * Upload manager of Qiniu cloud
     */
    @Bean
    public UploadManager getUploadManager() {
        return new UploadManager(new com.qiniu.storage.Configuration(Region.region2()));
    }

    /**
     * Auth of Qiniu cloud
     */
    @Bean
    public Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }
}