package edu.whut.springbear.gather.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 15:07 Thursday
 */
@Configuration
public class MyBatisConfiguration {
    /**
     * MyBatis sql session factory
     */
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean getSqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // Set the datasource
        sqlSessionFactoryBean.setDataSource(dataSource);
        // The type alias of the pojo class
        sqlSessionFactoryBean.setTypeAliasesPackage("edu.whut.springbear.gather.pojo");
        // Camel case name auto convert between pojo property and the table field
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
        // Set the pagination plugin
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{getPageInterceptor()});
        return sqlSessionFactoryBean;
    }

    /**
     * MyBatis mapper interface scan
     */
    @Bean
    public MapperScannerConfigurer getMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("edu.whut.springbear.gather.mapper");
        return mapperScannerConfigurer;
    }

    /**
     * MyBatis pagination plugin
     */
    @Bean
    public PageInterceptor getPageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("value", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
