package cn.edu.whut.springbear.gather.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 22:17 Wednesday
 */
@Configuration
@PropertySource("classpath:properties/jdbc.properties")
@ComponentScan(basePackages = {"cn.edu.whut.springbear.gather.mapper", "cn.edu.whut.springbear.gather.service.impl"})
public class SpringConfiguration {
    /**
     * Druid data source
     */
    @Bean
    public DataSource getDataSource(@Value("${jdbc.url}") String url, @Value("${jdbc.driver}") String driver, @Value("${jdbc.username}") String username, @Value("${jdbc.password}") String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }

    /**
     * MyBatis sql session factory
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean getSqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // Set the datasource
        sqlSessionFactoryBean.setDataSource(dataSource);
        // Type alias of the pojo class package
        sqlSessionFactoryBean.setTypeAliasesPackage("cn.edu.whut.springbear.gather.pojo");
        // Mapper location
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        // Camel case name auto convert between pojo property and the table field
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        // Pagination plugin interceptor
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("value", "true");
        pageInterceptor.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pageInterceptor});
        return sqlSessionFactoryBean;
    }

    /**
     * MyBatis mapper interface scan
     */
    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("cn.edu.whut.springbear.gather.mapper");
        return mapperScannerConfigurer;
    }
}

