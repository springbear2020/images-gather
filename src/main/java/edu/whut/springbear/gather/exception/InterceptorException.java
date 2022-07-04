package edu.whut.springbear.gather.exception;

import org.springframework.stereotype.Component;


/**
 * @author Spring-_-Bear
 * @datetime 2022-07-02 17:28 Saturday
 */
@Component
public class InterceptorException extends Exception {
    private static final long serialVersionUID = -3484227643635007883L;

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
