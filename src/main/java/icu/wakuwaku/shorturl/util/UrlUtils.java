package icu.wakuwaku.shorturl.util;

import java.util.regex.Pattern;

/**
 * @author Spike
 * @Description URL校验
 * @Date 2023/3/13
 */
public class UrlUtils {
    private final static Pattern URL_REG = Pattern.compile("^(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+" +
            "([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?$");

    public static boolean checkURL(String url){
        return URL_REG.matcher(url).matches();
    }
}
