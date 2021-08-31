package com.wjb.shop.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {

        public static String md5Digest(String source, Integer salt) {
            char[] chars = source.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i]=(char) (chars[i] + salt);
            }
            String s = new String(chars);
            String s1 = DigestUtils.md5Hex(s);
            return s1;
        }
}
