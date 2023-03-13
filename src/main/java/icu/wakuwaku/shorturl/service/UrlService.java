package icu.wakuwaku.shorturl.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Spike
 * @Description
 * @Date 2023/3/13
 */

public interface UrlService {

    String getLongUrlByShortUrl(String shortURL);

    String saveUrlMap(String shortURL, String longURL, String originalURL);

    @Async
    void updateUrlViews(String shortURL);
}
