package com.goingkeep.scserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.goingkeep.sccommon.constant.MessageConstant;
import com.goingkeep.sccommon.exception.LoginFailedException;
import com.goingkeep.sccommon.properties.WeChatProperties;
import com.goingkeep.sccommon.utils.HttpClientUtil;
import com.goingkeep.scpojo.dto.UserLoginDTO;
import com.goingkeep.scpojo.entity.User;
import com.goingkeep.scserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    private static final String wx_login = "http://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User userLogin(UserLoginDTO userLoginDTO) {
        //调用微信接口服务，获取当前微信用户的openid
        String openid = getString(userLoginDTO.getCode());
        //判断是否为空，为空则异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user = userMapper.CheckLogin(openid);
        //如果查询数据库为空，就插入一条新数据
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now()).build();
            userMapper.NewInsert(user);
        }
        return user;
    }


    private String getString(String code){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(wx_login, paramMap);
        JSONObject jsonObject = JSON.parseObject(json);

        String openId = jsonObject.getString("openId");
        return openId;

    }
}
