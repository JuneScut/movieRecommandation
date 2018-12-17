package com.green.movie_demo.service;

import com.green.movie_demo.entity.Result;
import com.green.movie_demo.entity.User;
import com.green.movie_demo.feign.WXCode2SessionFeignClient;
import com.green.movie_demo.mapper.UserInfoMapper;
import com.green.movie_demo.session.WXSession;
import com.green.movie_demo.util.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService
{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserInfoMapper userMapper;
    
    @Autowired
    private WXCode2SessionFeignClient wxCode2SessionFeignClient;
    
    @Value("#{appid}")
    private String appid;
    
    @Value("#{secret}")
    private String secret;
    
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
    
    public Result wxLogin(Map<String, Object>loginRequestMap)
    {
        // TODO: 通过 userinfo.signature 校验 userinfo 信息是否被篡改
        
        // 访问微信接口服务，获取sessionKey
        Map<String, Object>sessionRequestMap = new HashMap<>();
        sessionRequestMap.put("appid", appid);
        sessionRequestMap.put("secret", secret);
        sessionRequestMap.put("js_code", loginRequestMap.get("code"));
        sessionRequestMap.put("grant_type", "authorization_code");
        
        WXSession wxSession = wxCode2SessionFeignClient.code2Session(sessionRequestMap);
        switch (wxSession.getErrcode())
        {
            case -1:
            {
                logger.info("微信登录，系统繁忙，稍后再试");
                return Result.BadRequest().msg("系统繁忙，稍后再试").build();
            }
            case 0:{
                break;
            }
            case 40029:{
                logger.error("小程序登录，code无效");
                return Result.BadRequest().msg("code无效").build();
            }
            case 45011: {
                logger.error("小程序登录，用户刷新太快");
                return Result.BadRequest().msg("用户刷新太快, 无法登录微信").build();
            }
        }
        
        String sessionKey = wxSession.getSession_key();
        
        // 利用sessionKey对userinfo数据进行解密，得到unionid
        
        
        // TODO:
        
        
        return Result.OK().build();
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
