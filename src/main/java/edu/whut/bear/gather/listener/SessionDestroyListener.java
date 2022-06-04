package edu.whut.bear.gather.listener;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:48 PM
 */
public class SessionDestroyListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // HttpSession session = httpSessionEvent.getSession();
        // User user = (User) session.getAttribute("user");
        // // Spring application context
        // ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        // UserService userService = applicationContext.getBean("userServiceImpl", UserServiceImpl.class);
        // System.out.println(userService);
        // // Update the user's last login date
        // userService.updateLastLoginDate(new Date(), user.getId());
    }
}