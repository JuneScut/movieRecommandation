package com.green.movie_demo.service;

import com.green.movie_demo.entity.Category;
import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.entity.Result;
import com.green.movie_demo.mapper.UserMovieMapper;
import com.green.movie_demo.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class UserMovieService
{
    private Logger logger = LoggerFactory.getLogger(UserMovieService.class);
    
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
    
    public Result checkCollectedMovie(int user_id, int movie_id)
    {
        Integer count = userMovieMapper.checkCollectedMovie(user_id, movie_id);
        return count == 1 ? Result.OK().build() : Result.NotFound().build();
    }
    
    public Result getFavoriteCategories(int user_id)
    {
        List<Category> categories = userMovieMapper.getFavoriteCategories(user_id);
        int total = categories.size();
        return Result.OK().data(ResultUtil.total(total, categories)).build();
    }
    
    public Result addFavoriteCategories(int user_id, List<Integer> category_ids)
    {
        List<Integer> oldCategoryIds = userMovieMapper.getFavoriteCategoryIds(user_id);
        List<Integer> newCategoryIds = new LinkedList<>();
        for(Integer category_id: category_ids)
        {
            if(!oldCategoryIds.contains(category_id))
                newCategoryIds.add(category_id);
        }
        try
        {
            int affectedRows = userMovieMapper.insertFavoriteCategories(insertFavoriteCategoriesHelper(user_id, newCategoryIds));
            return Result.OK().build();
        }catch (Exception ex)
        {
            logger.error(ex.toString());
        }
        
        return Result.BadRequest().build();
    }
    
    private static Map<String, Object> insertFavoriteCategoriesHelper(int user_id, List<Integer> category_ids)
    {
        Map<String, Object>paramMap = new HashMap<>();
        paramMap.put("user_id", user_id);
        paramMap.put("category_ids", category_ids);
        return paramMap;
    }
}
