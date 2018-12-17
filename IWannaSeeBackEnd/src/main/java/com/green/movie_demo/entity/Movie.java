package com.green.movie_demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @create: 2018/10/5
 * @desc:
 */

@Repository
public class Movie
{
    private Integer id;
    private String title;
    private String country;
    private int year;
    private String directors;
    private String stars;
    private String release_date;
    private String running_time;
    private String short_intro;
    private int douban_rank;
    private int douban_score;
    private int douban_votes;
    private String douban_quote;
    private String douban_url;
    private String pic_url;
    
    private String category;
    
    public int getDouban_rank()
    {
        return douban_rank;
    }
    
    public void setDouban_rank(int douban_rank)
    {
        this.douban_rank = douban_rank;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public int getDouban_score()
    {
        return douban_score;
    }
    
    public void setDouban_score(int douban_score)
    {
        this.douban_score = douban_score;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public int getYear()
    {
        return year;
    }
    
    public void setYear(int year)
    {
        this.year = year;
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    public int getDouban_votes()
    {
        return douban_votes;
    }
    
    public void setDouban_votes(int douban_votes)
    {
        this.douban_votes = douban_votes;
    }
    
    public String getDouban_url()
    {
        return douban_url;
    }
    
    public void setDouban_url(String douban_url)
    {
        this.douban_url = douban_url;
    }
    
    public String getPic_url()
    {
        return pic_url;
    }
    
    public void setPic_url(String pic_url)
    {
        this.pic_url = pic_url;
    }
    
    public String getDouban_quote()
    {
        return douban_quote;
    }
    
    public void setDouban_quote(String douban_quote)
    {
        this.douban_quote = douban_quote;
    }
    
    public String getDirectors()
    {
        return directors;
    }
    
    public void setDirectors(String directors)
    {
        this.directors = directors;
    }
    
    public String getStars()
    {
        return stars;
    }
    
    public void setStars(String stars)
    {
        this.stars = stars;
    }
    
    public String getRelease_date()
    {
        return release_date;
    }
    
    public void setRelease_date(String release_date)
    {
        this.release_date = release_date;
    }
    
    public String getRunning_time()
    {
        return running_time;
    }
    
    public void setRunning_time(String running_time)
    {
        this.running_time = running_time;
    }
    
    public Integer getId()
    {
        return id;
    }
    
//    public void setUser_id(Integer id)
//    {
//        this.id = id;
//    }
    
    public String getShort_intro()
    {
        return short_intro;
    }
    
    public void setShort_intro(String short_intro)
    {
        this.short_intro = short_intro;
    }
    
    @JsonIgnore
    public Map<String, Object> getInfo()
    {
        Map<String, Object>info = new HashMap<>();
        info.put("title", title);
        info.put("country", country);
        info.put("year", year);
        info.put("douban_rank", douban_rank);
        info.put("douban_score", douban_score);
        info.put("category", category);
        return info;
    }
}
