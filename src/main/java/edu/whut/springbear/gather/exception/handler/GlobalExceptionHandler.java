package edu.whut.springbear.gather.exception.handler;

import edu.whut.springbear.gather.exception.InterceptorException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * TODO Replace with @ControllerAdmin and @ExceptionHandler
 * @author Spring-_-Bear
 * @datetime 2022-07-03 15:26 Sunday
 */
@Component
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (ex instanceof InterceptorException) {
            modelAndView.addObject("loginMsg", ex);
        } else {
            modelAndView.addObject("loginMsg", "服务器维护中，请稍后重试");
        }
        return modelAndView;
    }
}