package icu.wakuwaku.shorturl.util;

import cn.hutool.core.lang.hash.MurmurHash;

/**
 * @author Spike
 * @tips 好好学习，天天向上！！！
 * @time 2023/3/13 15:41
 * @Description:Url hash并转换为base62
 */
public class HashUtils {
    private static final char[] CHARS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };
    private static final int SIZE = CHARS.length;


    private static String convertDecToBase62(long num){
        StringBuilder sb = new StringBuilder();
        while (num>0){
            int i = (int)(num%SIZE);
            sb.append(CHARS[i]);
            num/=SIZE;
        }
        return sb.reverse().toString();//将32位hash值num转换为62进制的字符串

    }

    public static String hashToBase62(String str){
        long i = (long)MurmurHash.hash32(str); //将str转化为32位的hash值
        long num = i<0?Integer.MAX_VALUE-i:i;
        return convertDecToBase62(num);
    }



}
