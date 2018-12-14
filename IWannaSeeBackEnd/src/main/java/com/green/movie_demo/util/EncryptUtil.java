package com.green.movie_demo.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil
{
    private final static String KEY_SHA = "SHA";
    private final static String KEY_MD5 = "MD5";
    private final static int radix = 16;
    
    private static String encrypt(String KEY, String message)
    {
        BigInteger bigInteger = null;
        try{
            MessageDigest sha = MessageDigest.getInstance(KEY);
            byte[] inputData = message.getBytes();
            sha.update(inputData);
            bigInteger = new BigInteger(sha.digest());
        }catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
            //System.out.println(ex);
        }
        return bigInteger.toString(radix); // 转化为16位字符串
    }
    
    public static String encrypt(String message)
    {
        return sha(message);
    }
    
    public static String sha(String message)
    {
        return encrypt(KEY_SHA, message);
    }
    
    public static String md5(String message)
    {
        return encrypt(KEY_MD5, message);
    }
}
