package com.green.movie_demo.mapper;

import com.green.movie_demo.entity.Movie;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

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
    
    //    @Select("select count(*) from `collection` where `user_id` = #{user_id} and `movie_id` = #{movie_id};")
    //    int getCollectionCnt(@Param("user_id")int user_id, @Param("movie_id") int movie_id);
}
