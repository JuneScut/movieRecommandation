package com.green.movie_demo.service;

import com.green.movie_demo.entity.Category;
import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.entity.Result;
import com.green.movie_demo.mapper.MovieMapper;
import com.green.movie_demo.util.ResultUtil;
import com.green.movie_demo.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @create: 2018/10/5
 * @desc:
 */
@Service
public class MovieService
{
    @Autowired
    private MovieMapper movieMapper;
    
    public Result getHotMovies(int page, int per_page)
    {
        int totalCount = movieMapper.findTotal_HotMovies();
        List<Movie> movies = movieMapper.findHotMoviesOrderByScore(SqlUtil.offset(page, per_page), per_page);
        Object data = ResultUtil.total(totalCount, movies);
        return Result.OK().data(data).build();
    }
    
    public Result getMoviesInfo(int page, int per_page)
    {
        int totalCount = movieMapper.findTotal_Movies();
        List<Movie> movies = movieMapper.findMoviesInfoByRank(SqlUtil.offset(page, per_page), per_page);
        List<Map<String, Object>> moviesInfoList = new LinkedList<>();
        for (Movie movie : movies)
            moviesInfoList.add(movie.getInfo());
        Object data = ResultUtil.total(totalCount, moviesInfoList);
        
        return Result.OK().data(data).build();
    }
    
    public Result getAMovie(int id)
    {
        if(id < 0) return Result.BadRequest().build();
        Movie movie = movieMapper.findAMovieById(id);
        Result result = movie != null? Result.OK().data(movie).build(): Result.NotFound().build();
        return result;
    }
    
    public Result getMoviesCnt()
    {
        int totalCount = movieMapper.findTotal_Movies();
        return Result.OK().data(totalCount).build();
    }
    
    public Result getShortComments(int movie_id)
    {
        if(movie_id < 0) return Result.BadRequest().build();
        List<String> shortComments = movieMapper.findShortCommentsByMovieId(movie_id);
        return Result.OK().data(shortComments).build();
    }
    
    public Result getAllCategories()
    {
        int total = movieMapper.findTotal_Categories();
        List<Category> categories = movieMapper.findAllCategories();
        Object data = ResultUtil.total(total, categories);
        return Result.OK().data(data).build();
    }
    
    public Result findKMoviesUnderCategory(int category_id, int K, boolean orderByRank)
    {
        int total = movieMapper.findTotal_MoviesUnderCategory(category_id);
        List<Movie> movies = orderByRank? movieMapper.findTopKMoviesUnderCategory(category_id, K)
                : movieMapper.findRndKMoviesUnderCategory(category_id, K);
        Object data = ResultUtil.total(total, movies);
        return Result.OK().data(data).build();
    }
    
//    public Result findTopKMoviesUnderCategory(int category_id, int K)
//    {
//        int total = movieMapper.findTotal_MoviesUnderCategory(category_id);
//        List<Movie> movies = movieMapper.findTopKMoviesUnderCategory(category_id, K);
//        Object data = ResultUtil.total(total, movies);
//        return Result.OK().data(data).build();
//    }
//
//    public Result findRndKMoviesUnderCategory(int category_id, int K)
//    {
//        int total = movieMapper.findTotal_MoviesUnderCategory(category_id);
//        List<Movie> movies = movieMapper.findRndKMoviesUnderCategory(category_id, K);
//        Object data = ResultUtil.total(total, movies);
//        return Result.OK().data(data).build();
//    }
    
    public Result getMoviesUnderCategory(int category_id, int page, int per_page)
    {
        int total = movieMapper.findTotal_MoviesUnderCategory(category_id);
        List<Movie> movies = movieMapper.findMoviesUnderCategory(category_id, SqlUtil.offset(page, per_page), per_page);
        Object data = ResultUtil.total(total, movies);
        return Result.OK().data(data).build();
    }
    
    public Movie getMovieDescription(String title)
    {
        return movieMapper.findMovieByTitle(title);
    }
    
//    @Override
//    public List<String> getShortComment(String title)
//    {
//        int movie_id = movieMapper.findMovieIdByTitle(title);
//        return movieMapper.findShortCommentsByMovieId(movie_id);
//    }
    
//    @Override
//    public boolean addCollectedMovie(String username, String title)
//    {
//        int user_id = userMapper.findIdByName(username);
//        int movie_id = movieMapper.findMovieIdByTitle(title);
//        if (user_id <= 0 || movie_id <= 0)
//            return false;
//        return movieMapper.insertCollectedMovie(user_id, movie_id) != 0;
////        return true;
//    }
//
//    @Override
//    public boolean removeCollectedMovie(String username, String title)
//    {
//        int user_id = userMapper.findIdByName(username);
//        int movie_id = movieMapper.findMovieIdByTitle(title);
//        if (user_id <= 0 || movie_id <= 0)
//            return false;
//        return movieMapper.deleteCollectedMovie(user_id, movie_id) != 0;
//    }
//
//    @Override
//    public List<Movie> getCollectedMovies(String username)
//    {
//        int user_id = userMapper.findIdByName(username);
//        return movieMapper.getCollectedMovies(user_id);
//    }
    
//    @Override
//    public boolean checkMovieCollected(String username, String title)
//    {
//        try
//        {
//            int user_id = userMapper.findIdByName(username);
//            int movie_id = movieMapper.findMovieIdByTitle(title);
//            if (user_id <= 0 || movie_id <= 0)
//                return false;
//            return movieMapper.getCollectedMovieCnt(user_id, movie_id) == 1;
//        } catch (Exception ex)
//        {
//            System.out.println(ex);
//            return false;
//        }
//
//    }
}
