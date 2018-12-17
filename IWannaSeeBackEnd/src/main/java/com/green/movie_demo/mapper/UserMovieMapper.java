package com.green.movie_demo.mapper;

import com.green.movie_demo.entity.Category;
import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.entity.Rating;
import com.green.movie_demo.util.SqlUtil;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface UserMovieMapper
{
    @Insert("insert into `collection` (`user_id`, `movie_id`) values (#{user_id}, #{movie_id})")
    int insertIntoCollection(@Param("user_id") int user_id, @Param("movie_id") int movie_id);
    
    @Delete("delete from `collection` where `user_id` = #{user_id} and `movie_id` = #{movie_id}")
    int deleteFromCollection(@Param("user_id") int user_id, @Param("movie_id") int movie_id);
    
    @Select("select `movie_id` " +
            "from `collection` " +
            "where `user_id` = #{user_id} ")
    List<Integer> getCollections(@Param("user_id") int user_id);
    
    @Select("select count(*) " +
            "from `collection` " +
            "where `user_id` = #{user_id} ")
    Integer findTotal_Collections(@Param("user_id") int user_id);
    
    //    @Select("select * from `movie` where `id` in (select `movie_id` from `collection` where `user_id` = #{user_id})")
    @Select("select M.* " +
            "from `movie` as M " +
            "inner join " +
            "(select `movie_id` from `collection` where `user_id` = #{user_id}) as CM " +
            "on M.id = CM.movie_id " + SqlUtil.LIMIT_OFFSET)
    List<Movie> getCollectedMovies(int user_id, int offset,@Param("limit") int per_page);
    
    @Select("select count(*) " +
            "from `collection` " +
            "where `user_id` = #{user_id} and `movie_id` = #{movie_id} ")
    Integer checkCollectedMovie(int user_id, int movie_id);
    
    @Select("select C.* " +
            "from `category` as C inner join " +
            "( " +
            "select * " +
            "from`favor_category` " +
            "where `user_id` = #{user_id} " +
            ") " +
            "as FC on C.id = FC.category_id ")
    List<Category> getFavorCategories(int user_id);
    
    @Select("select `category_id` " +
            "from `favor_category` " +
            "where `user_id` = #{user_id} ")
    List<Integer> getFavorCategoryIds(int user_id);
    
    @Insert("<script> " +
            "insert into `favor_category` (`user_id`, `category_id`) values " +
            "<foreach item=\"category_id\" collection=\"category_ids\" separator=\",\">" +
            "(#{user_id}, #{category_id})" +
            "</foreach> " +
            "</script> ")
    Integer insertFavorCategories(Map<String, Object>paramMap);
    //Integer insertFavorCategories(int user_id, List<Integer>categoryIds);
    
    @Delete("delete from `favor_category` " +
            "where `user_id` = #{user_id} and `category_id` = #{category_id} ")
    Integer deleteFavorCategory(int user_id, int category_id);
    
    @Insert("insert into `rating` (`user_id`, `movie_id`, `score`, `comment`) " +
            "values (#{user_id}, #{movie_id}, #{score}, #{comment}) ")
    Integer insertRating(Rating rating);
    
    @Select("select * from `rating` where `movie_id` = #{movie_id} " + SqlUtil.LIMIT_OFFSET)
    List<Rating> findRatingsOfMovie(int movie_id, int offset, int limit);
    
    @Select("select count(*) from `rating` where `movie_id` = #{movie_id} ")
    Integer findTotal_RatingsOfMovie(int movie_id);
    
    @Select("select * from `rating` where `user_id` = #{user_id} " + SqlUtil.LIMIT_OFFSET)
    List<Rating> findRatingsOfUser(int user_id, int offset, int limit);
    
    @Select("select count(*) from `rating` where `user_id` = #{user_id} ")
    Integer findTotal_RatingsOfUser(int user_id);
    
    @Delete("delete from `rating` " +
            "where `user_id` = #{user_id} and `movie_id` = #{movie_id} ")
    Integer deleteRating(int user_id, int movie_id);
    
    //    @Select("select count(*) from `collection` where `user_id` = #{user_id} and `movie_id` = #{movie_id};")
    //    int getCollectionCnt(@Param("user_id")int user_id, @Param("movie_id") int movie_id);
}
