package icu.wakuwaku.shorturl.mapper;

import icu.wakuwaku.shorturl.entity.UrlMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Spike
 * @Description
 * @Date 2023/3/13
 */
@Mapper
@Repository
public interface UrlMapper {
    String getLongUrlByShortUrl(String surl);

    int saveUrlMap(UrlMap urlMap);

    int updateUrlViews(String surl);
}
