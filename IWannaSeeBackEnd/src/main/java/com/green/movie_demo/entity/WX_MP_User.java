package com.green.movie_demo.entity;

public class WX_MP_User
{
    private int user_id;
    private String openid;  // 对于同一微信用户，微信平台下公众号、订阅号、小程序三者各自持有一个openid
    
    public WX_MP_User()
    {
    }
    
    public WX_MP_User(int user_id, String openid)
    {
        this.user_id = user_id;
        this.openid = openid;
    }
    
    public int getUser_id()
    {
        return user_id;
    }
    
    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }
    
    public String getOpenid()
    {
        return openid;
    }
    
    public void setOpenid(String openid)
    {
        this.openid = openid;
    }
}
