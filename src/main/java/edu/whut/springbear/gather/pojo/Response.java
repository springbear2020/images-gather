package edu.whut.springbear.gather.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-01 09:49 Friday
 */
@Data
public class Response implements Serializable {
    private static final long serialVersionUID = 1992589378681096903L;
    public static final int SUCCESS = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;

    /**
     * Response code agreed with the client
     */
    private Integer code;
    /**
     * Response message
     */
    private String msg;
    /**
     * Response content
     */
    private Map<String, Object> resultMap = new HashMap<>();

    public static Response success(String msg) {
        return getResponse(msg, SUCCESS);
    }

    public static Response info(String msg) {
        return getResponse(msg, INFO);
    }

    public static Response warn(String msg) {
        return getResponse(msg, WARN);
    }

    public static Response error(String msg) {
        return getResponse(msg, ERROR);
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
