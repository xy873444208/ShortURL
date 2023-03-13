package icu.wakuwaku.shorturl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Spike
 * @tips 好好学习，天天向上！！！
 * @time 2023/3/13 15:37
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Res {
    private Integer code;
    private String msg;
    private Object data;
    public Res(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public static Res ok(String msg,Object data){
        return new Res(200,msg,data);
    }
    public static Res create(Integer code,String msg){
        return new Res(code,msg);
    }
}
