package com.goingkeep.scserver.controller.user;

import com.goingkeep.sccommon.result.Result;
import com.goingkeep.scpojo.dto.UserLoginDTO;
import com.goingkeep.scpojo.vo.UserLoginVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "C端用户接口")
@Slf4j
@RequestMapping("/user/user")
public class UserController {
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("C端用户登录:{}", userLoginDTO.getCode());



        return Result.success();
    }
}
