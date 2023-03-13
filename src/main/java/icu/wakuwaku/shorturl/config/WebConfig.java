package icu.wakuwaku.shorturl.config;

import icu.wakuwaku.shorturl.interceptor.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Spike
 * @Description 配置拦截器 ，前后端在同一个端口下，不需要跨域支持。跨域问题是浏览器对于ajax请求的一种安全限制：一个页面发起的ajax
 * 请求，只能是于当前页同域名的路径，这能有效的阻止跨站攻击。
 * 因此：跨域问题 是针对ajax的一种限制。
 * @Date 2023/3/13
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);
    }
}
