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
    
        public String getAppid()
        {
            return appid;
        }
    
        public void setAppid(String appid)
        {
            this.appid = appid;
        }
    
        public long getTimestamp()
        {
            return timestamp;
        }
    
        public void setTimestamp(long timestamp)
        {
            this.timestamp = timestamp;
        }
    }
    
    public String getOpenId()
    {
        return openId;
    }
    
    public void setOpenId(String openId)
    {
        this.openId = openId;
    }
    
    public String getNickName()
    {
        return nickName;
    }
    
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    
    public int getGender()
    {
        return gender;
    }
    
    public void setGender(int gender)
    {
        this.gender = gender;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getProvince()
    {
        return province;
    }
    
    public void setProvince(String province)
    {
        this.province = province;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getAvatarUrl()
    {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }
    
    public String getUnionId()
    {
        return unionId;
    }
    
    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }
    
    public Watermark getWatermark()
    {
        return watermark;
    }
    
    public void setWatermark(Watermark watermark)
    {
        this.watermark = watermark;
    }
}
