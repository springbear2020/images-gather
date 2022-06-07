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
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 查看普通用户是否登录
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        // 用户尚未登录，拒绝访问
        if (user == null) {
            throw new InterceptorException("请登录您的个人账号");
        }
        return true;
    }
}
