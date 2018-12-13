package com.green.movie_demo.controller;

import com.green.movie_demo.entity.Result;
import com.green.movie_demo.entity.User;
import com.green.movie_demo.service.UserMovieService;
import com.green.movie_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result addCollection(@PathVariable("user_id") int user_id, @RequestBody Map<String, Object>map)
    {
        int movie_id = (Integer)map.get("movie_id");
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
    
//    @GetMapping(value = "/collection")
//    public Result checkCollected(@RequestParam(value = "username") String username, @RequestParam(value = "title") String title)
//    {
//        Result responseMsg;
//        if (movieService.checkMovieCollected(username, title))
//        {
//            responseMsg = Result.OK();
//        } else
//        {
//            responseMsg = Result.NotFound();
//        }
//        return responseMsg;
//    }
    
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
