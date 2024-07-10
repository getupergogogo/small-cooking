package com.goingkeep.scserver.mapper;


import com.goingkeep.scpojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT *from user where openid =#{openid}")
    User CheckLogin(String openid);

    void NewInsert(User user);
}
