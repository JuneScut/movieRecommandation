package com.green.movie_demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class Result
{
    private int status;
    private String msg;
    private Object data;
    
    public Result()
    {
    }
    
//    public Result(int status, String msg)
//    {
//        this(status, msg, null);
//    }
    
    public Result(int status, String msg, Object data)
    {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    
    // 为了方便地统一消息格式，临时定义的静态方法
    // 正确地做法：改为工厂模式
    public static ResultBuilder OK()
    {
        return new ResultBuilder(200, "OK");
    }
    
    public static ResultBuilder BadRequest()
    {
        return new ResultBuilder(400, "Bad Request");
    }
    
    public static ResultBuilder Unauthorized()
    {
        return new ResultBuilder(401, "Unauthorized");
    }
    
    public static ResultBuilder Forbidden()
    {
        return new ResultBuilder(403, "Forbidden");
    }
    
    public static ResultBuilder NotFound()
    {
        return new ResultBuilder(404, "Not Found");
    }
    
    
    // 可以加个注解，不作为JSON格式化
    @JsonIgnore
    @Override
    public String toString()
    {
        return "{" +
                "status:" + this.status + ","
                + "msg:\'" + this.msg + "\',"
                + "data:" + this.data
                + "}";
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public String getMsg()
    {
        return msg;
    }
    
    public void setMsg(String msg)
    {
        this.msg = msg;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public void setData(Object data)
    {
        this.data = data;
    }
    
    public static class ResultBuilder
    {
        private final int status;
        //private final String msg;
        private String msg;
        private Object data;
    
        public ResultBuilder(int status, String msg)
        {
            this.status = status;
            this.msg = msg;
        }
    
        public ResultBuilder data(Object data)
        {
            this.data = data;
            return this;
        }
        
        public ResultBuilder msg(String msg)
        {
            this.msg = msg;
            return this;
        }
        
        public Result build()
        {
            return new Result(this.status, this.msg, this.data);
        }
    }
}
