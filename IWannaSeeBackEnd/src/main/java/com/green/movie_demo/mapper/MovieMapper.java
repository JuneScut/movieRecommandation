package com.green.movie_demo.mapper;

import com.green.movie_demo.entity.Movie;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "movieMapper")
public interface MovieMapper
{
    @Select("select * " +
            "from (select * from `movie` order by `douban_rank` limit #{base}, #{offset}) as R " +
            "inner join " +
            "movie_categories_v as S " +
            "on R.id=S.movie_id")
    List<Movie> findMoviesInfoByRank(@Param("base") int base, @Param("offset") int offset);
    
    @Select("select R.*, `category` " +
            "from (select * from `movie` where `id` = #{id}) as R join `movie_categories_v` as S " +
            "on R.id = S.movie_id;")
    Movie findAMovieById(@Param("id") Integer id);
    
    @Select("select `content` from `short_comment` where `movie_id` = #{movie_id}")
    List<String> findShortCommentsByMovieId(@Param("movie_id") int movie_id);
    
    @Select("select count(*) from `movie`")
    Integer findMoivesCnt();
    
    // -----------------------
    
    @Select("select * from movie where `title` = #{title}")
    Movie findMovieByTitle(@Param("title") String title);
    
    @Select("select min(`id`) from `movie` where `title` = #{title}")
    int findMovieIdByTitle(@Param("title") String title);
    
    
}
