package com.green.movie_demo.controller;

import com.green.movie_demo.entity.User;
import com.green.movie_demo.service.UserMovieService;
import com.green.movie_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:9527", maxAge = 3600)
@CrossOrigin()
@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMovieService userMovieService;
    
    @PostMapping("/signUp")
    public Object signUp(@RequestBody User user)
    {
        return userService.signUp(user);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestBody User user)
    {
        return userService.login(user);
    }
    
    /**
     * 添加电影到用户的收藏夹
     * @param user_id
     * @param requestMap 包含movie_id的JSON请求体
     */
    @PostMapping(value = "/{user_id}/collections")
    //public Object addCollection(@PathVariable("user_id") int user_id, @RequestBody int movie_id)
    //public Object addCollection(@PathVariable("user_id") int user_id, @RequestBody Integer movie_id)
    public Object addCollection(@PathVariable("user_id") int user_id, @RequestBody Map<String, Object>requestMap)
    {
        int movie_id = (Integer)requestMap.get("movie_id");
        return userMovieService.addCollection(user_id, movie_id);
    }
    
    // 从收藏夹移除电影
    @DeleteMapping(value = "/{user_id}/collections")
    public Object removeCollection(@PathVariable("user_id") int user_id, @RequestParam("movie_id") int movie_id)
    {
        return userMovieService.removeCollection(user_id, movie_id);
    }
    
    /**
     * 获取用户收藏夹
     * @param user_id
     * @return 收藏的电影id列表
     */
    @GetMapping(value = "/{user_id}/collections")
    public Object getCollections(@PathVariable("user_id") int user_id)
    {
        return userMovieService.getCollections(user_id);
    }
    
    /**
     * 获取用户收藏夹的电影
     * @param user_id
     * @param page
     * @param per_page
     * @return 电影列表
     */
    @GetMapping(value = "/{user_id}/collections/movies")
    public Object getCollectedMovies(@PathVariable("user_id") int user_id,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "per_page", defaultValue = "10") int per_page)
    {
        return userMovieService.getCollectedMovies(user_id, page, per_page);
    }
    
    /**
     * 用户是否收藏该电影
     * @param user_id
     * @param movie_id
     * @return OK, NOT FOUND
     */
    @GetMapping(value = "/{user_id}/collections/{movie_id}")
    public Object checkCollectedMovie(@PathVariable int user_id, @PathVariable("movie_id") int movie_id)
    {
        return userMovieService.checkCollectedMovie(user_id, movie_id);
    }
    
    /**
     * 添加用户喜好的电影类别
     * @param user_id
     * @param requestMap 请求体包含的喜好电影类别的id: favor_category_ids
     */
    @PostMapping("/{user_id}/favor-categories")
    public Object addFavorCategories(@PathVariable int user_id, @RequestBody Map<String, Object>requestMap)
    {
        List<Integer> favor_category_ids = (List<Integer>)requestMap.get("favor_category_ids");
        return userMovieService.addFavorCategories(user_id, favor_category_ids);
    }
    
    /**
     * 获取用户喜欢的电影类别
     */
    @GetMapping("/{user_id}/favor-categories")
    public Object getFavorCategories(@PathVariable int user_id)
    {
        return userMovieService.getFavorCategories(user_id);
    }
    
    // 移除喜好的电影类别
    @DeleteMapping("/{user_id}/favor-categories")
    public Object removeFavorCategory(@PathVariable int user_id, @RequestParam int category_id)
    {
        return userMovieService.removeFavorCategory(user_id, category_id);
    }
    
    @GetMapping("/{user_id}/ratings")
    public Object getRatingsOfUser(@PathVariable int user_id,
                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "per_page", defaultValue = "10") int per_page)
    {
        return userMovieService.getRatingsOfUser(user_id, page, per_page);
    }
}
