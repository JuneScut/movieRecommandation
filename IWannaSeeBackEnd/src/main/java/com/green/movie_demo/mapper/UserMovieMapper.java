package com.green.movie_demo.mapper;

import com.green.movie_demo.entity.Category;
import com.green.movie_demo.entity.Movie;
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
    
    //    @Select("select * from `movie` where `id` in (select `movie_id` from `collection` where `user_id` = #{user_id})")
    @Select("select R.* " +
            "from `movie` as R " +
            "inner join " +
            "(select `movie_id` from `collection` where `user_id` = #{user_id}) as S " +
            "on R.id = S.movie_id")
    List<Movie> getCollections(@Param("user_id") int user_id);
    
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
    List<Category> getFavoriteCategories(int user_id);
    
    @Select("select id " +
            "from `favor_category` " +
            "where `user_id` = #{user_id} ")
    List<Integer> getFavoriteCategoryIds(int user_id);
    
    @Insert("<script> " +
            "insert into `favor_category` (`user_id`, `category_id`) values " +
            "<foreach item=\"category_id\" collection=\"category_ids\" separator=\",\">" +
            "(#{user_id}, #{category_id})" +
            "</foreach> " +
            "</script> ")
    Integer insertFavoriteCategories(Map<String, Object>paramMap);
    //Integer insertFavoriteCategories(int user_id, List<Integer>categoryIds);
    
    //    @Select("select count(*) from `collection` where `user_id` = #{user_id} and `movie_id` = #{movie_id};")
    //    int getCollectionCnt(@Param("user_id")int user_id, @Param("movie_id") int movie_id);
}
