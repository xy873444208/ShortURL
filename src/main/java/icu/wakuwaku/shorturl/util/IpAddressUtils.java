package icu.wakuwaku.shorturl.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Spike
 * @Description 在nginx等代理之后获取用户真是ip地址
 * @Date 2023/3/13
 */
@Slf4j
@Component
public class IpAddressUtils {

    public static String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip=request.getHeader("x-forwarded-for");
        }
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
            ip=request.getRemoteAddr();//如果使用了反向代理软件，用 request.getRemoteAddr（）方法获取的IP地址是：127.0.0.1或
            // 192.168.1.110，而并不是客户端的真实IP。
            if ("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                }catch (UnknownHostException e){
                    log.error("getIpAddress exception:", e);
                }
                if (inetAddress!=null){
                    ip = inetAddress.getHostAddress();//本机ip地址
                }
            }

        }
        return StringUtils.substringBefore(ip,",");//返回ip字符串里面第一个ip，也就是第一个逗号之前的。

    }

}
