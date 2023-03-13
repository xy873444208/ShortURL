package icu.wakuwaku.shorturl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Spike
 * @Description 访问控制
 * @Date 2023/3/13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    int seconds(); //访问周期
    int maxCount();//周期内访问次数限制
    String msg() default "操作过于频繁";//触发限制时的消息提示
}
