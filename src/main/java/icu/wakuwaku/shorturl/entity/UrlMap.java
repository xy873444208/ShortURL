package icu.wakuwaku.shorturl.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Spike
 * @tips 好好学习，天天向上！！！
 * @time 2023/3/13 15:27
 */
@Data
@NoArgsConstructor
public class UrlMap {
    private Long id;
    private String surl;//短链接
    private String lurl;//长连接
    private Integer views;//访问次数
    private Date createTime;//创建时间
    public UrlMap(String surl,String lurl){
        this.surl=surl;
        this.lurl=lurl;
        this.views=0;
        this.createTime=new Date();
    }
}
