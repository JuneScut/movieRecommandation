package com.green.movie_demo.controller;

import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/movies")
public class MovieController
{
    @Autowired
    private MovieService movieService;
    
    @GetMapping(value = "/info")
    public Object getMoviesInfo(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "per_page", defaultValue = "20") int per_page)
    {
        return movieService.getMoviesInfo(page, per_page);
    }
    
    @GetMapping(value = "/{id}")
    public Object one(@PathVariable("id") int id)
    {
        return movieService.getAMoive(id);
    }
    
    @GetMapping(value = "/count")
    public Object moivesCnt()
    {
        return movieService.getMoivesCnt();
    }
    
    @GetMapping(value = "{movie_id}/short-comments")
    public Object shortComments(@PathVariable("movie_id") int movie_id)
    {
        return movieService.getShortComments(movie_id);
    }
}

