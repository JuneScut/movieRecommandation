package com.green.movie_demo.mapper;

import com.green.movie_demo.entity.Movie;
import com.green.movie_demo.entity.User;
import com.green.movie_demo.entity.WX_MP_User;
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
    
//    @Select("select min(`id`) from user where username = #{username}")
//    int findIdByName(@Param("username") String username);
    
//    @Insert("insert into user (username, password, gender, age) " +
//            "values (#{username}, #{password}, #{gender}, #{age})")
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    int insertUser(User user);
    
    @Insert("insert into user (gender, age) " +
            "values (#{gender}, #{age})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertUser(User user);
    
    @Select("select * from `user` where `use_id` = #{user_id} ")
    User findUserById(int user_id);
    
    @Select("select * from `wx_mp_user` where `openid` = #{openid} ")
    WX_MP_User findWXMPUserByOpenId(String openid);
    
    @Insert("insert into `wx_mp_user` (`user_id`, `openid`) " +
            "values (#{user_id}, #{openid}) ")
    Integer bindUserWithWXMPUser(WX_MP_User wx_mp_user);
}
