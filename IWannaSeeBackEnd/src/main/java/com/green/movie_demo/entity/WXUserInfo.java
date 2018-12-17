package com.green.movie_demo.entity;

public class WXUserInfo
{
    
    private String openId;
    private String nickName;
    private int gender; // 0：未知、1：男、2：女
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
    private Watermark watermark;
    
    public class Watermark
    {
        private String appid;
        private long timestamp;
    }
}
