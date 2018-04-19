package com.yuan.web;

import com.yuan.dto.DTO;
import com.yuan.entity.UserDO;
import com.yuan.service.IUserService;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import static com.yuan.service.IUserService.LOGIN_TYPE_CLIENT;
import static com.yuan.service.IUserService.REGEDIT_TYPE_CLIENT;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    //客户端登录
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public DTO login(String user, String password) {
        return userService.login(user, password, LOGIN_TYPE_CLIENT);
    }

    //用户注册
    @RequestMapping(value = "/reg",method = RequestMethod.POST)
    @ResponseBody
    public DTO reg(UserDO userDO) {
        return userService.reg(userDO, REGEDIT_TYPE_CLIENT);
    }

    //检测用户名是否存在
    @RequestMapping(value = "/repeat", method = RequestMethod.GET)
    @ResponseBody
    public DTO repeat(String username) {
        return userService.repeat(username);
    }

    //获取登录用户信息
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public DTO getLoginUser() {
        return userService.getLoginUser();
    }

    //更新用户信息
    @RequestMapping(value = "/put", method = RequestMethod.POST)
    @ResponseBody
    public DTO updateUser(UserDO userDO) {
        return userService.updateUser(userDO);
    }

    //退出登录
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public DTO logout() {
        try {
            SecurityUtils.getSubject().logout();
        }catch (Exception e){
            e.printStackTrace();
        }
        DTO dto = new DTO();
        dto.setSuccess(true);
        return dto;
    }

    //根据开始时间获取弹幕信息
    @GetMapping(value = "/sign")
    @ResponseBody
    public DTO listSignByAidTime(int aid, long s) {
        return userService.listSignByAidTime(aid, s);
    }

}
