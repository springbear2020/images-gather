package edu.whut.bear.gather.interceptor;

import edu.whut.bear.gather.exception.InterceptorException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 6:57 PM
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 查看用户是否登录
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        Object admin = session.getAttribute("admin");
        // 管理员和普通用户均为登录，抛出异常由视图解析器解析并跳转到登录页面
        if (user == null && admin == null) {
            throw new InterceptorException("亲爱的用户，请先登录您的账号");
        }
        return true;
    }
}
