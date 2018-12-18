package com.green.movie_demo.entity;

public class WXUser
{
    private int user_id;
    //private String openid;  // 对于同一微信用户，微信平台下公众号、订阅号、小程序三者各自持有一个openid
    private String unionid; // 使用unionid 能够标识微信平台下公众号、订阅号、小程序的唯一用户
    
    public int getUser_id()
    {
        return user_id;
    }
    
    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }
    
    public String getUnionid()
    {
        return unionid;
    }
    
    public void setUnionid(String unionid)
    {
        this.unionid = unionid;
    }
}
