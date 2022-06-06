package edu.whut.bear.gather.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 7:38 PM
 */
@SuppressWarnings("unchecked")
public class WebUtils {
    private static String baiduMapIpAddressAccess;

    static {
        URL resource = WebUtils.class.getClassLoader().getResource("properties/project.properties");
        Properties properties = new Properties();
        try {
            assert resource != null;
            properties.load(new FileInputStream(resource.getPath()));
            baiduMapIpAddressAccess = properties.getProperty("baidu.access");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从请求头中获取 IP 地址
     *
     * @param request HttpServletRequest
     * @return IP
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
        return ip;
    }

    /**
     * 通过百度地图 API 根据 IP 获取归属地
     *
     * @param ip Ip
     * @return IP 归属地
     */
    public static String parseIp(String ip) {
        String responseContent;
        try {
            // 发送 GET 请求到百度地图进行 IP 解析
            responseContent = sendGetRequest(ip);
            if (responseContent == null) {
                return "未知地点";
            }
            // 解析返回的 JSON 数据，得到归属地
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> content = (Map<String, String>) mapper.readValue(responseContent, Map.class).get("content");
            return content.get("address");
        } catch (Exception e) {
            e.printStackTrace();
            return "未知地点";
        }
    }

    /**
     * 发送 GET 请求到百度地图查询 IP 归属地 JSON 数据
     *
     * @param ip Ip
     * @return 响应的 JSON 数据
     */
    private static String sendGetRequest(String ip) {
        String url = baiduMapIpAddressAccess + ip;
        // Try to make a connection with the request url
        URLConnection urlConnection;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            urlConnection = new URL(url).openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.setRequestMethod("GET");
            // 与百度地图服务器建立 GET 连接
            connection.connect();
            // 处理返回的数据
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stringBuffer.toString();
    }
}
