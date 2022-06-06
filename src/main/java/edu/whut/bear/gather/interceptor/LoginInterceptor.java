package edu.whut.bear.gather.interceptor;

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
        // 管理员和普通用户均为登录，拒绝访问所有资源
        if (user == null && admin == null) {
            response.setContentType("text/html; charset=UTF-8");
            response.getOutputStream().write("<h1 style=\"text-align: center\">亲爱的用户，请先登录您的账号</h1>".getBytes(StandardCharsets.UTF_8));
            return false;
        }
        return true;
    }
}
