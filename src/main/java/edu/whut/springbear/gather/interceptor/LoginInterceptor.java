package edu.whut.springbear.gather.interceptor;

import edu.whut.springbear.gather.exception.InterceptorException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-02 17:27 Saturday
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // User must sign in before access any resources
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        Object admin = session.getAttribute("admin");
        // Not login, throw an interceptor exception
        if (user == null && admin == null) {
            throw new InterceptorException("服务器拒绝了您的请求，请先登录您的账号");
        }
        return true;
    }
}