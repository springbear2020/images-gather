package cn.edu.whut.springbear.gather.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-18 22:15 Thursday
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // User must sign in before access any resources
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        // Not login, throw an interceptor exception
        if (user == null) {
            throw new RuntimeException("拒绝访问，请先登录您的账号");
        }
        return true;
    }
}
