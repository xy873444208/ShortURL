package icu.wakuwaku.shorturl.controller;

import icu.wakuwaku.shorturl.annotation.AccessLimit;
import icu.wakuwaku.shorturl.entity.Res;
import icu.wakuwaku.shorturl.service.UrlService;
import icu.wakuwaku.shorturl.util.HashUtils;
import icu.wakuwaku.shorturl.util.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Spike
 * @Description
 * @Date 2023/3/13
 */
@Controller
public class IndexController {
    @Autowired
    UrlService urlService;
    private static String host;
    @Value("${server.host}")
    public void setHost(String host){
        this.host = host;
    }
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @AccessLimit(seconds = 10,maxCount = 1,msg = "10秒内只能生成一次短链接")
    @PostMapping("/generate")
    @ResponseBody
    public Res generateShortURL(@RequestParam String longURL){
        if (UrlUtils.checkURL(longURL)){
            if (!longURL.startsWith("http")){
                longURL="http://"+longURL;
            }
            String shortURL =  urlService.saveUrlMap(HashUtils.hashToBase62(longURL),longURL,longURL);
            return Res.ok("请求成功",host+shortURL);
        }
        return Res.create(400,"url有误");
    }
    @GetMapping("/{shortURL}")
    public String redirect(@PathVariable String shortURL){
        String longURL = urlService.getLongUrlByShortUrl(shortURL);
        if (longURL!=null){
            urlService.updateUrlViews(shortURL);//访问次数加一
            //查询到对应的原始链接，302重定向
            return "redirect:" + longURL;
        }
        //没有对应的原始链接，直接返回首页
        return "redirect:/";
    }
}
