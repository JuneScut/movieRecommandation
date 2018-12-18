package com.green.movie_demo.service;

import com.green.movie_demo.entity.Result;
import com.green.movie_demo.entity.User;
import com.green.movie_demo.entity.WXUserInfo;
import com.green.movie_demo.entity.WX_MP_User;
import com.green.movie_demo.feign.WXCode2SessionFeignClient;
import com.green.movie_demo.mapper.UserInfoMapper;
import com.green.movie_demo.session.WXSession;
import com.green.movie_demo.util.AESUtil;
import com.green.movie_demo.util.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService
{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Value("${WX-session-url}")
    private String WX_session_url;
    
    @Value("${appid}")
    private String appid;
    
    @Value("${secret}")
    private String secret;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private UserInfoMapper userMapper;
    
    
//    public Result signUp(User user)
//    {
//        if (user == null) return Result.BadRequest().build();
//        User tempUser = userMapper.getUserByName(user.getUsername());
//
//        if (tempUser == null)
//        {
//            user.setPassword(EncryptUtil.encrypt(user.getPassword()));
//            userMapper.insertUser(user);
//            user = userMapper.getUserByName(user.getUsername());
//            user.setPassword(null);
//            return Result.OK().data(user).build();
//        }
//        return Result.BadRequest().msg("用户名已被注册").build();
//    }
//
//    public Result login(User user)
//    {
//        if (user == null || user.getUsername() == null) return Result.BadRequest().build();
//        User tempUser = userMapper.getUserByName(user.getUsername());
//        if (tempUser == null || !tempUser.getPassword().equals(EncryptUtil.encrypt(user.getPassword())))
//            return Result.BadRequest().msg("用户不存在或密码错误").build();
//
//        tempUser.setPassword(null);
//        return Result.OK().data(tempUser).build();
//    }
    
    // 微信小程序登录，通过小程序特定的 openid 标识用户唯一性
    // 简单轻便开发，后续有需要再完善
    public Result wx_mpLogin(Map<String, Object> mpLoginRequestMap)
    {
        Map<String, Object> userInfoData = (Map<String, Object>) mpLoginRequestMap.get("userInfoData");
        // TODO: 通过 userInfoData.signature 校验 userinfo 信息是否被篡改
        
        // 访问微信接口服务，获取sessionKey
        StringBuilder urlBuilder = new StringBuilder(WX_session_url);
        urlBuilder.append("?appid="+appid)
                .append("&secret="+secret)
                .append("&js_code="+mpLoginRequestMap.get("code"))
                .append("&grant_type=authorization_code");
        
        WXSession  wxSession = this.restTemplate.getForObject(urlBuilder.toString(), WXSession.class);
        switch (wxSession.getErrcode())
        {
//            case -1:
//            {
//                logger.info("微信登录，系统繁忙，稍后再试");
//                return Result.BadRequest().msg("系统繁忙，稍后再试").build();
//            }
            case 0:{
                break;
            }
//            case 40029:{
//                logger.error("小程序登录，code无效");
//                return Result.BadRequest().msg("code无效").build();
//            }
//            case 45011: {
//                logger.error("小程序登录，用户刷新太快");
//                return Result.BadRequest().msg("用户刷新太快, 无法登录微信").build();
//            }
            default:{
                logger.error(wxSession.getErrmsg());
                return Result.BadRequest().msg(wxSession.getErrmsg()).build();
            }
        }
        
        String sessionKey = wxSession.getSession_key();
        
        WX_MP_User mp_user = userMapper.findWXMPUserByOpenId(wxSession.getOpenid());
        if(mp_user == null) // 用户第一次登录，需要新增数据
        {
            User user = new User();
            user.setGender((int)userInfoData.get("gender"));
            userMapper.insertUser(user);
            mp_user = new WX_MP_User(user.getId(), wxSession.getOpenid());
            userMapper.bindUserWithWXMPUser(mp_user);
        }
        
        // TODO: 将sessionKey等数据存Redis数据库，方便后续利用和权限管理
        
        
        return Result.OK().data(mp_user).build();
    }
    
    // 微信全平台接入登录，利用unionid
//    public Result wxUnionLogin(Map<String, Object>loginRequestMap)
//    {
//        //  通过 userinfo.signature 校验 userinfo 信息是否被篡改
//
//        // 访问微信接口服务，获取sessionKey
//        StringBuilder urlBuilder = new StringBuilder(WX_session_url);
//        urlBuilder.append("?appid="+appid)
//                .append("&secret="+secret)
//                .append("&js_code="+loginRequestMap.get("code"))
//                .append("&grant_type=authorization_code");
//
//
//        //        Map<String, Object>sessionRequestMap = new HashMap<>();
//        //        sessionRequestMap.put("appid", appid);
//        //        sessionRequestMap.put("secret", secret);
//        //        sessionRequestMap.put("js_code", loginRequestMap.get("code"));
//        //        sessionRequestMap.put("grant_type", "authorization_code");
//
//        WXSession  wxSession = this.restTemplate.getForObject(urlBuilder.toString(), WXSession.class);
//        // WXSession wxSession = wxCode2SessionFeignClient.code2Session(sessionRequestMap);
//        switch (wxSession.getErrcode())
//        {
//            case -1:
//            {
//                logger.info("微信登录，系统繁忙，稍后再试");
//                return Result.BadRequest().msg("系统繁忙，稍后再试").build();
//            }
//            case 0:{
//                break;
//            }
//            case 40029:{
//                logger.error("小程序登录，code无效");
//                return Result.BadRequest().msg("code无效").build();
//            }
//            case 45011: {
//                logger.error("小程序登录，用户刷新太快");
//                return Result.BadRequest().msg("用户刷新太快, 无法登录微信").build();
//            }
//        }
//
//        String sessionKey = wxSession.getSession_key();
//
//        // 利用sessionKey对userinfo数据进行解密，得到unionid
//        try
//        {
//            WXUserInfo wxUserInfo = AESUtil.WX_UserInfo_decode(appid, sessionKey, (Map<String, Object>) loginRequestMap.get("userInfoData"));
//        }catch (Exception ex)
//        {
//            ex.printStackTrace();
//            logger.error(ex.toString());
//        }
//
//
//        return Result.OK().build();
//    }
    
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
