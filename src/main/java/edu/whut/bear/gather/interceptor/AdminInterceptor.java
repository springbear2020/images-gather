package edu.whut.bear.gather.interceptor;

import edu.whut.bear.gather.exception.InterceptorException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 6:57 PM
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 查看管理员是否登录
        HttpSession session = request.getSession();
        Object admin = session.getAttribute("admin");
        // 管理员尚未登录，抛出异常由视图解析器解析并跳转到登录页面
        if (admin == null) {
            throw new InterceptorException("请登录您的管理员账号");
        }
        return true;
    }
}
