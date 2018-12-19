package com.green.movie_demo.mapper;

import com.green.movie_demo.entity.Category;
import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.util.SqlUtil;
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
    
    @Select("select * " +
            "from `movie` as M inner join `movie_category` as MC on M.id = MC.movie_id " +
            "where `category_id` = #{category_id} " +
            "limit #{limit} offset #{offset}")
    List<Movie> findMoviesUnderCategory(int category_id, int offset, @Param("limit") int per_page);
    
    @Select("select R.*, `category` " +
            "from (select * from `movie` where `id` = #{id}) as R join `movie_categories_v` as S " +
            "on R.id = S.movie_id;")
    Movie findAMovieById(@Param("id") Integer id);
    
    @Select("select `content` from `short_comment` where `movie_id` = #{movie_id}")
    List<String> findShortCommentsByMovieId(@Param("movie_id") int movie_id);
    
    @Select("select count(*) from `movie`")
    Integer findTotal_Movies();
    
    @Select("select C.* " +
            "from `category` as C inner join `movie_category` as MC on C.id = MC.category_id " +
            "group by `category_id` " +
            "order by count(`movie_id`) desc;")
    List<Category> findAllCategories();
    
    @Select("select count(*) from `category`")
    Integer findTotal_Categories();
    
    @Select("select M.*" +
            "from `movie` as M inner join `movie_category` as MC on M.id = MC.movie_id " +
            "where `category_id` = #{category_id} " +
            "order by `douban_rank` " +
            "limit #{limit} ")
    List<Movie> findTopKMoviesUnderCategory(@Param("category_id") int category_id, @Param("limit") int K);
    
    @Select("select count(*) " +
            "from `movie_category` " +
            "where `category_id` = #{category_id} ")
    Integer findTotal_MoviesUnderCategory(@Param("category_id") int category_id);
    
    @Select("select M.* " +
            "from `movie` as M inner join `movie_category` as MC on M.id = MC.movie_id " +
            "where `category_id` = #{category_id} " +
            "order by rand() " +
            "limit #{limit}")
    List<Movie> findRndKMoviesUnderCategory(@Param("category_id") int category_id, @Param("limit") int K);
    
    @Select("select count(*) from `movie` where `douban_rank` is null;")
    Integer findTotal_HotMovies();
    
    @Select("select R.*, `category` " +
            "from (select * from `movie` where `douban_rank` is null order by `douban_score` desc" + SqlUtil.LIMIT_OFFSET + ") as R join `movie_categories_v` as S " +
            "on R.id = S.movie_id;")
    List<Movie> findHotMoviesOrderByScore(int offset, @Param("limit") int per_page);
    
    // -----------------------
    
    @Select("select * from movie where `title` = #{title}")
    Movie findMovieByTitle(@Param("title") String title);
    
    @Select("select min(`id`) from `movie` where `title` = #{title}")
    int findMovieIdByTitle(@Param("title") String title);
    
    
}
