package edu.whut.bear.gather.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 12:06 PM
 */
public class SessionListener implements HttpSessionListener {
    private static int visits = 0;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        System.out.println("Online users: " + (++visits));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        System.out.println("Online users: " + (--visits));
    }
}