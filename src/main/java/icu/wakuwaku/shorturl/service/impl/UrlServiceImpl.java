package icu.wakuwaku.shorturl.service.impl;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import icu.wakuwaku.shorturl.entity.UrlMap;
import icu.wakuwaku.shorturl.mapper.UrlMapper;
import icu.wakuwaku.shorturl.service.UrlService;
import icu.wakuwaku.shorturl.util.HashUtils;
import icu.wakuwaku.shorturl.util.UrlUtils;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Spike
 * @Description
 * @Date 2023/3/13
 */
@Service
public class UrlServiceImpl implements UrlService {
    @Autowired
    UrlMapper urlMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    //自定义长链接防重复字符串
    private static final String DUPLICATE="*";
    //最近使用的短链接缓存过过期时间（分钟）
    private static final long TIMEOUT = 10;
    //创建布隆过滤器。某样东西一定不存在或者可能存在
    //布隆过滤器可以添加元素，但是不能删除元素。因为删掉元素会导致误判率增加。
    private static final BitMapBloomFilter FILTER = new BitMapBloomFilter(10);

    @Override
    public String getLongUrlByShortUrl(String shortURL) {
        //查找redis中是否有缓存
        String longURL = redisTemplate.opsForValue().get(shortURL);
        if (longURL != null){
            //有缓存，重制缓存时间
            redisTemplate.expire(shortURL,TIMEOUT, TimeUnit.MINUTES);
            return longURL;
        }
        //无缓存，从数据库查找
         longURL = urlMapper.getLongUrlByShortUrl(shortURL);
        if (longURL!=null){
            //数据库有这个短链接，于是加入redis缓存
            redisTemplate.opsForValue().set(shortURL,longURL,TIMEOUT, TimeUnit.MINUTES);
        }
        return longURL;
    }

    @Override
    public String saveUrlMap(String shortURL, String longURL, String originalURL) {
        if (shortURL.length()==1){
            longURL+=DUPLICATE;
            shortURL = saveUrlMap(HashUtils.hashToBase62(shortURL),longURL,originalURL);
        }//短链接长度必须大于1
        else if (FILTER.contains(shortURL)) {
            //String redisLongURL = redisTemplate.opsForValue().get(shortURL);

            String longUrlByShortUrl = urlMapper.getLongUrlByShortUrl(shortURL);
            if (originalURL.equals(longUrlByShortUrl)){
                redisTemplate.expire(shortURL,TIMEOUT,TimeUnit.MINUTES); //没有误判且没有哈希冲突
                urlMapper.updateUrlViews(shortURL);
                return shortURL;
            }else {
                //布隆过滤器误判（false判断为true）或者哈希冲突
                longURL+=DUPLICATE;
                shortURL = saveUrlMap(HashUtils.hashToBase62(shortURL),longURL,originalURL);
                urlMapper.updateUrlViews(shortURL);
            }
        }else {
            //布隆过滤器没有这个短链接
            try {
                FILTER.add(shortURL);
                urlMapper.saveUrlMap(new UrlMap(shortURL,originalURL));
                urlMapper.updateUrlViews(shortURL);
                redisTemplate.opsForValue().set(shortURL,originalURL,TIMEOUT, TimeUnit.MINUTES);
            }catch (Exception e){
                if (e instanceof DuplicateKeyException){
                    ////数据库已经存在此短链接，但是服务器重新启动会导致布隆过滤器清理
                    longURL+=DUPLICATE;
                    shortURL = saveUrlMap(HashUtils.hashToBase62(longURL),longURL,originalURL);
                }
            }

        }
        return shortURL;
    }

    @Override
    public void updateUrlViews(String shortURL) {
            urlMapper.updateUrlViews(shortURL);
    }
}
