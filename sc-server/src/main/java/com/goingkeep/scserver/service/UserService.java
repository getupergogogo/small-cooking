package com.goingkeep.scserver.service;


import com.goingkeep.scpojo.dto.UserLoginDTO;
import com.goingkeep.scpojo.entity.User;

public interface UserService {

    public User userLogin(UserLoginDTO userLoginDTO);
}
