package com.green.movie_demo.service;

import com.green.movie_demo.entity.Result;
import com.green.movie_demo.entity.User;
import com.green.movie_demo.mapper.UserInfoMapper;
import com.green.movie_demo.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserInfoMapper userMapper;
    
    public Result signUp(User user)
    {
        if (user == null) return Result.BadRequest().build();
        User tempUser = userMapper.getUserByName(user.getUsername());
        
        if (tempUser == null)
        {
            user.setPassword(EncryptUtil.encrypt(user.getPassword()));
            userMapper.insertUser(user);
            user = userMapper.getUserByName(user.getUsername());
            user.setPassword(null);
            return Result.OK().data(user).build();
        }
        return Result.BadRequest().msg("用户名已被注册").build();
    }
    
    public Result login(User user)
    {
        if (user == null || user.getUsername() == null) return Result.BadRequest().build();
        User tempUser = userMapper.getUserByName(user.getUsername());
        if (tempUser == null || !tempUser.getPassword().equals(EncryptUtil.encrypt(user.getPassword())))
            return Result.BadRequest().msg("用户不存在或密码错误").build();
        
        tempUser.setPassword(null);
        return Result.OK().data(tempUser).build();
    }
    
//    public List<User> findUsers(String username)
//    {
//        if (username == null) return null;
//        List<User> users = userMapper.getUserByName(username);
//        return users;
//    }

//    public User find(String username)
//    {
//        List<User> users = findUsers(username);
//        if(users.size() == 1) return users.get(0);
//        return null;
//    }
}
