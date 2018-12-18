package com.green.movie_demo.entity;

import org.springframework.stereotype.Repository;

@Repository
public class User
{
    private int id;
    private int gender;
    private int age;
    
    public int getId()
    {
        return id;
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