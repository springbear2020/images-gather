package cn.edu.whut.springbear.gather.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Replace web.xml
 *
 * @author Spring-_-Bear
 * @datetime 2022-08-08 09:43 Monday
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringMvcConfiguration.class};
    }

    /**
     * Url pattern of DispatchServlet
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.do"};
    }

    /**
     * Filters
     */
    @Override
    protected Filter[] getServletFilters() {
        // Chinese garbled solution
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);
        // Support RESTful style
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        return new Filter[]{characterEncodingFilter, hiddenHttpMethodFilter};
    }
}