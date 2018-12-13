package com.green.movie_demo.mapper;

import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "userMapper")
public interface UserInfoMapper
{
    @Select("select * from user where username = #{username}")
   User getUserByName(@Param("username") String username);
    //List<User> getUserByName(@Param("username") String username);
    
    @Select("select min(`id`) from user where username = #{username}")
    int findIdByName(@Param("username") String username);
    
    @Insert("insert into user (username, password, gender, age) " +
            "values (#{username}, #{password}, #{gender}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertUser(User user);
    
    // --------- User Movie --------
    
    
}
