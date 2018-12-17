package com.green.movie_demo.entity;

public class Rating
{
    private int user_id;
    private int movie_id;
    private int score;
    private String comment;
    
    public int getUser_id()
    {
        return user_id;
    }
    
    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }
    
    public int getMovie_id()
    {
        return movie_id;
    }
    
    public void setMovie_id(int movie_id)
    {
        this.movie_id = movie_id;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public void setScore(int score)
    {
        this.score = score;
    }
    
    public String getComment()
    {
        return comment;
    }
    
    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
