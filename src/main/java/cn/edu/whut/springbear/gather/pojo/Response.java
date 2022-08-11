package cn.edu.whut.springbear.gather.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:52 Wednesday
 */
@Data
public class Response implements Serializable {
    private static final long serialVersionUID = 1992589378681096903L;

    private static final int SUCCESS = 0;
    private static final int INFO = 1;
    private static final int ERROR = 2;

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
