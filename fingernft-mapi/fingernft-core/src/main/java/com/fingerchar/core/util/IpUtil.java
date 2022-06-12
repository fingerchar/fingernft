
package com.fingerchar.core.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP获取工具类
 */
public class IpUtil {


    /**
     * 公司高德地图API-KEY
     */
    private static final String AMAP_WEB_API_KEY = "a457248cde62980328a9c6f6830fe1a0";


    private static final String AMAP_IP_LOCATION_API = "https://restapi.amap.com/v3/ip?key=" + AMAP_WEB_API_KEY + "&ip=";

    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

    private static final Pattern IP_PATTERN = Pattern.compile("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$");

    //java匹配内网ip正则
    private static final String INNER_IP_REGEX = "(127[.]0[.]0[.]1)|(localhost)|(10[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3})|(172[.]((1[6-9])|(2\\d)|(3[01]))[.]\\d{1,3}[.]\\d{1,3})|(192[.]168[.]\\d{1,3}[.]\\d{1,3})";

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        logger.error("获取用户的主机发生异常", e);
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
            logger.error("获取用户的ip地址发生异常", e);
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    /**
     * 根据IP获取位置信息
     * 如果IP地址格式不符合要求，将会抛出异常
     * 当前只支持ipv4
     *
     * @param ip ipv4的IP地址
     * @return
     */
    public static AmapLocation getLocationByIp(String ip) {
        if (!isIp(ip)) {
            logger.warn("IP格式不正确");
            return null;
        }
        if (ip == null) {
            return null;
        }
        //判断是否来自局域网
        Pattern localNetworkPattern = Pattern.compile(INNER_IP_REGEX);
        Matcher localNetworkMather = localNetworkPattern.matcher(ip);
        if (localNetworkMather.find()) {
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        AmapLocation location = restTemplate.getForObject(AMAP_IP_LOCATION_API + ip, AmapLocation.class);
        return location;
    }

    public static AmapLocation getLocationByIpForString(String ip) {
        if (!isIp(ip)) {
            logger.warn("IP格式不正确");
            return null;
        }
        if (ip == null) {
            return null;
        }
        //判断是否来自局域网
        Pattern localNetworkPattern = Pattern.compile(INNER_IP_REGEX);
        Matcher localNetworkMather = localNetworkPattern.matcher(ip);
        if (localNetworkMather.find()) {
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        String location = restTemplate.getForObject(AMAP_IP_LOCATION_API + ip, String.class);
        JSONObject jsonObject = JSONObject.parseObject(location);
        String province = jsonObject.getString("province");
        String city = jsonObject.getString("city");
        if (StringUtils.isEmpty(province) || province.equals("[]")) {

            jsonObject.put("province", null);
        }
        if (StringUtils.isEmpty(city) || city.equals("[]")) {
            jsonObject.put("city", null);
        }
        try {
            AmapLocation targetLocation = jsonObject.toJavaObject(AmapLocation.class);
            return targetLocation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断一个字符串是否为IP
     *
     * @param ip ip字符串
     * @return
     */
    public static boolean isIp(String ip) {
        Matcher matcher = IP_PATTERN.matcher(ip);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}

