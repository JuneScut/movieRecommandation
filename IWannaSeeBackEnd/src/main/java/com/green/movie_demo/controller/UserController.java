package com.green.movie_demo.controller;

import com.green.movie_demo.entity.Result;
import com.green.movie_demo.entity.User;
import com.green.movie_demo.service.UserMovieService;
import com.green.movie_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:9527", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMovieService userMovieService;
    
    @PostMapping("/signUp")
    public Result signUp(@RequestBody User user)
    {
        return userService.signUp(user);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user)
    {
        return userService.login(user);
    }
    
    @PostMapping(value = "/{user_id}/collections")
    //public Result addCollection(@PathVariable("user_id") int user_id, @RequestBody int movie_id)
    //public Result addCollection(@PathVariable("user_id") int user_id, @RequestBody Integer movie_id)
    public Result addCollection(@PathVariable("user_id") int user_id, @RequestBody Map<String, Object>requestMap)
    {
        int movie_id = (Integer)requestMap.get("movie_id");
        return userMovieService.addCollection(user_id, movie_id);
    }
    
    @DeleteMapping(value = "/{user_id}/collections/{movie_id}")
    public Result removeCollection(@PathVariable("user_id") int user_id, @PathVariable("movie_id") int movie_id)
    {
        return userMovieService.removeCollection(user_id, movie_id);
    }
    
    @GetMapping(value = "/{user_id}/collections")
    public Result getCollectedMovies(@PathVariable("user_id") int user_id)
    {
        return userMovieService.getCollections(user_id);
    }
    
    @GetMapping(value = "/{user_id}/collections")
    public Result checkCollectedMovie(@PathVariable int user_id, @RequestParam("movie_id") int movie_id)
    {
        return userMovieService.checkCollectedMovie(user_id, movie_id);
    }
    
    @PostMapping("/{user_id}/favor-categories/")
    public Object addFavoriteCategories(@PathVariable int user_id, @RequestBody List<Integer> favor_category_ids)
    {
        return userMovieService.addFavoriteCategories(user_id, favor_category_ids);
    }
    
    @GetMapping("/{user_id}/favor-categories")
    public Object getFavoriteCategories(@PathVariable int user_id)
    {
        return userMovieService.getFavoriteCategories(user_id);
    }
    
    //    @RequestMapping(value = "/find", method = RequestMethod.GET)
    //    public User findByName(@RequestParam(value = "username") String username)
    //    {
    //        return userService.find(username);
    //    }
    
    //    @GetMapping(value = "/id")
    //    public Result findIdByName(@RequestParam(value = "username") String username)
    //    {
    //        Result responseMsg;
    //        try{
    //            Integer id = userService.findIdByName(username);
    //            if(id != null)
    //            {
    //                responseMsg =  Result.OK();
    //                responseMsg.setData((int)id);
    //            }else
    //                responseMsg = Result.NotFound();
    //        }catch (Exception ex)
    //        {
    //            responseMsg = Result.BadRequest();
    //            System.out.println(ex);
    //        }
    //        return responseMsg;
    //    }
}
