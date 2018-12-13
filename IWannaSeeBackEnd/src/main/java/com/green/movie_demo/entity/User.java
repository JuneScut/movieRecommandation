package com.green.movie_demo.entity;

import org.springframework.stereotype.Repository;

@Repository
public class User
{
    private int id;
    private String username;
    private String password;
    private int gender;
    private int age;
    
    public int getId()
    {
        return id;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getGender()
    {
        return gender;
    }
    
    public void setGender(int gender)
    {
        this.gender = gender;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }
}