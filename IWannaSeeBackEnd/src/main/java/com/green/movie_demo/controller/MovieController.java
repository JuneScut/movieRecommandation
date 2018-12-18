package com.green.movie_demo.controller;

import com.green.movie_demo.entity.Rating;
import com.green.movie_demo.entity.Result;
import com.green.movie_demo.service.MovieService;
import com.green.movie_demo.service.UserMovieService;
import com.green.movie_demo.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin()
@RestController
@RequestMapping("/movies")
public class MovieController
{
    @Autowired
    private MovieService movieService;
    
    @Autowired
    private UserMovieService userMovieService;
    
    // 获取电影简略信息
    @GetMapping(value = "/info")
    public Object getMoviesInfo(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "per_page", defaultValue = "20") int per_page)
    {
        return movieService.getMoviesInfo(page, per_page);
    }
    
    @GetMapping(value = "/{id}")
    public Object getAMovie(@PathVariable("id") int id)
    {
        return movieService.getAMovie(id);
    }
    
    // 数据库中所有电影数量
    @GetMapping(value = "/count")
    public Object getMoviesCnt()
    {
        return movieService.getMoviesCnt();
    }
    
    // 一部电影的豆瓣短评
    @GetMapping(value = "/{movie_id}/short-comments")
    public Object getShortComments(@PathVariable("movie_id") int movie_id)
    {
        return movieService.getShortComments(movie_id);
    }
    
    // 所有电影类别，一个类别下的电影数目越多，排序越靠前
    @GetMapping(value = "/categories")
    public Object getAllCategories()
    {
        return movieService.getAllCategories();
    }
    
    // TODO: 对page, per_page等参数的检查
    // 该类别下K部的电影
    // orderByRank:
    //  true 获取排名靠前的K部电影
    //  false 随机获取K部电影
    @GetMapping("/categories/{category_id}/some-movies")
    public Object getKMoviesFromCategory(@PathVariable("category_id") int category_id,
                                         @RequestParam("K") int K,
                                         @RequestParam("orderByRank") boolean orderByRank)
    {
        return movieService.findKMoviesUnderCategory(category_id, K, orderByRank);
    }
    
    // 该类别下的电影
    @GetMapping("/categories/{category_id}/movies")
    public Object getMoviesUnderCategory(@PathVariable("category_id") int category_id,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "per_page", defaultValue = "10") int per_page)
    {
        return movieService.getMoviesUnderCategory(category_id, page, per_page);
    }
    
    @PostMapping("/{movie_id}/ratings")
    public Object addARating(@PathVariable("movie_id") int movie_id, @RequestBody Rating rating)
    {
        if(movie_id != rating.getMovie_id()) return Result.BadRequest().msg("评分的movie_id和路径中的movie_id不一致").build();
        return userMovieService.addARating(rating);
    }
    
    @GetMapping("/{movie_id}/ratings")
    public Object getRatingsOfMovie(@PathVariable int movie_id,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "per_page", defaultValue = "10") int per_page)
    {
        return userMovieService.getRatingsOfMovie(movie_id, page, per_page);
    }
    
    @DeleteMapping("/{movie_id}/ratings")
    public Object removeARating(@PathVariable int movie_id, @RequestParam int user_id)
    {
        return userMovieService.removeARating(user_id, movie_id);
    }
    
    @PutMapping("/{movie_id}/ratings")
    public Object updateARating(@PathVariable int movie_id, @RequestBody Rating rating)
    {
        if(movie_id != rating.getMovie_id()) return Result.BadRequest().build();
        return userMovieService.updateARating(rating);
    }
}

