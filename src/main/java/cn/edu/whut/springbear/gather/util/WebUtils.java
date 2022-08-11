package cn.edu.whut.springbear.gather.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:05 Thursday
 */
public class WebUtils {
    /**
     * Get the ip address from the request header
     *
     * @return IP string or ""
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String s : ips) {
                if (!("unknown".equalsIgnoreCase(s))) {
                    ip = s;
                    break;
                }
            }
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * Send a GET request to the map.baidu.com, then handle with the response json data,
     * get the ip location from the response finally
     *
     * @param requestUrl The request url for baidu.com
     * @return Ip location or "未知地点"
     */
    public static String parseIpLocation(String requestUrl) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            // Try to make a GET httpURLConnection with the request url
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(requestUrl).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            // Handle with the response json data, combine into line by line
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                }
            }
            // Convert the data into Map, then get location from it
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> content = (Map<String, String>) mapper.readValue(stringBuffer.toString(), Map.class).get("content");
            if (content == null || content.get("address") == null) {
                return "未知地点";
            }
            return content.get("address");
        } catch (IOException e) {
            return "未知地点";
        }
    }
}
