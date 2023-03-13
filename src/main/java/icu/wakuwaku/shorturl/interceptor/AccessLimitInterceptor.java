package icu.wakuwaku.shorturl.interceptor;

import icu.wakuwaku.shorturl.annotation.AccessLimit;
import icu.wakuwaku.shorturl.entity.Res;
import icu.wakuwaku.shorturl.util.IpAddressUtils;
import icu.wakuwaku.shorturl.util.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @author Spike
 * @Description 访问控制拦截器
 * @Date 2023/3/13
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            //方法上没有accesslimit注解就直接return
            if (accessLimit==null){
                return true;
            }
            int secends = accessLimit.seconds();//访问周期（秒）
            int maxCount = accessLimit.maxCount();//周期内访问次数限制
            String ip = IpAddressUtils.getIpAddress(request);
            String method = request.getMethod();
            String requestURI = request.getRequestURI();

            String redisKey = ip+":"+method+":"+requestURI;
            Object redisResult = redisTemplate.opsForValue().get(redisKey);
            Integer count = JacksonUtils.convertValue(redisResult, Integer.class);
            if (count==null){
                //在规定周期内第一次访问，存入redis
                redisTemplate.opsForValue().increment(redisKey,1);
                redisTemplate.expire(redisKey,secends, TimeUnit.SECONDS); //设置redisKey的到期时间
            }else{
                if (count>=maxCount){
                    //超出访问限制次数
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    Res res = Res.create(403, accessLimit.msg());
                    out.write(JacksonUtils.writeValueAsString(res));
                    out.flush();
                    out.close();
                    return false;
                }else {
                    //没有超出访问限制次数
                    redisTemplate.opsForValue().increment(redisKey,1);
                }
            }

        }
        return true;//prehandler在请求处理之前执行.该方法的返回值是布尔值 Boolean 类型的，当它返回为 false 时，表示请求结束，
        // 后续的 Interceptor 和 Controller 都不会再执行；当返回值为 true 时，就会继续调用下一个 Interceptor 的 preHandle 方法
        // ，如果已经是最后一个 Interceptor 的时候，就会是调用当前请求的 Controller 中的方法。
        //postHandler 方法在当前请求进行处理之后，也就是在 Controller 中的方法调用之后执行，但是它会在 DispatcherServlet 进行视
        // 图返回渲染之前被调用，所以我们可以在这个方法中对 Controller 处理之后的 ModelAndView 对象进行操作。

       // afterCompletion该方法将在整个请求结束之后，也就是在 DispatcherServlet 渲染了对应的视图之后执行，这个方法的主要作用是用
        // 于进行资源清理的工作。像异常处理资源释放会放在这一步.

     //多个拦截器的执行顺序是: 拦截器A的preHandler-->拦截器B的preHandler-->B的postHandler-->A的postHandler-->
        // B的afterCompletion-->A的afterCompletion
    }
}
