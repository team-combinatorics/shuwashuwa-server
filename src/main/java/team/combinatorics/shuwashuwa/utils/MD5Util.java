package team.combinatorics.shuwashuwa.utils;

import org.springframework.util.DigestUtils;

public class MD5Util {
    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    /**
     * 生成md5
     *
     * @param str 要加密的字符串
     * @return 生成的md5字符串
     */
    public static String getMD5(String str) {
        String base = str + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
