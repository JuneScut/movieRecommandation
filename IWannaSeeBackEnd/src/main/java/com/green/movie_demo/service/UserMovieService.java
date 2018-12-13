package com.green.movie_demo.service;

import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.entity.Result;
import com.green.movie_demo.mapper.UserMovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMovieService
{
    @Autowired
    private UserMovieMapper userMovieMapper;
    
    public Result addCollection(int user_id, int movie_id)
    {
       int affectedRow = userMovieMapper.insertIntoCollection(user_id, movie_id);
       if(affectedRow == 1)
       {
           String data = "{" +
                   "user_id: " + user_id +
                   ",movie_id: " + movie_id +
                   "}";
           return Result.OK().data(data).build();
       }else
           return Result.BadRequest().build();
    }
    
    public Result removeCollection(int user_id, int movie_id)
    {
        int affectedRow = userMovieMapper.deleteFromCollection(user_id, movie_id);
        return affectedRow == 1 ? Result.OK().build() : Result.BadRequest().build();
    }
    
    public Result getCollections(int user_id)
    {
        List<Movie> movies = userMovieMapper.getCollections(user_id);
        return Result.OK().data(movies).build();
    }
}
