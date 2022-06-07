package edu.whut.bear.gather.exception;

import org.springframework.stereotype.Component;

/**
 * @author Spring-_-Bear
 * @datetime 6/7/2022 4:08 PM
 */
@Component
public class InterceptorException extends Exception {

    private String msg;

    public InterceptorException() {
    }

    public InterceptorException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
