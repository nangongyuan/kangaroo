package com.yuan.util;

public class SaltUtil {

    private static String randSouce = "1234567890-=][poiuytrewqasdfghjkl;'/.,mnbvcxz";

    public static String getRandSalt() {
        int len = (int) (Math.random() * 8) + 3;
        char[] temp = randSouce.toCharArray();
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int rnd = (int) (Math.random() * randSouce.length());
            salt.append(temp[rnd]);
        }
        return salt.toString();
    }

}
