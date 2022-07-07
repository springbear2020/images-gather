package edu.whut.springbear.gather.interceptor;

import edu.whut.springbear.gather.exception.InterceptorException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-02 20:14 Saturday
 */
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // User must sign in before access any resources
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        // Not login, throw an interceptor exception
        if (user == null) {
            throw new InterceptorException("Access denied, please sign in your user account");
        }
        return true;
    }
}
