package edu.whut.bear.gather.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 3:17 PM
 */
@Data
@Component
public class Response {
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int INFO = 2;

    private Integer code;
    private String msg;
    private Map<String, Object> resultMap = new HashMap<>();

    public static Response success(String msg) {
        return getResponse(msg, SUCCESS);
    }

    public static Response error(String msg) {
        return getResponse(msg, ERROR);
    }

    public static Response info(String msg) {
        return getResponse(msg, INFO);
    }

    private static Response getResponse(String msg, int code) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public Response put(String key, Object value) {
        this.getResultMap().put(key, value);
        return this;
    }
}

