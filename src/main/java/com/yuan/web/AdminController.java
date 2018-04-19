package com.yuan.web;

import com.yuan.dto.DTO;
import com.yuan.entity.UserDO;
import com.yuan.service.IUserService;
import com.yuan.util.VerifiCodeUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import static com.yuan.service.IUserService.LOGIN_TYPE_MANAGE;

@Controller
@RequestMapping(value = "/admins")
public class AdminController {
    @Autowired
    private IUserService userService;

    //后台登录
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public DTO loginAdmins(String user, String password, String verifiCode, HttpSession session) {
        String vc = (String) session.getAttribute("verifiCode");
        session.removeAttribute("verifiCode");
        if (vc != null && verifiCode != null && vc.toLowerCase().equals(verifiCode.toLowerCase())) {
            return userService.login(user, password, LOGIN_TYPE_MANAGE);
        } else {
            DTO result = new DTO();
            result.setSuccess(false);
            result.setMsg("验证码错误");
            return result;
        }
    }

    //获取验证码
    @RequestMapping(value = "/verification", method = RequestMethod.GET)
    public void GetVerifiCode(HttpServletRequest request, HttpServletResponse response) {
        VerifiCodeUtils.VerifiCodeStyle verifiCodeStyle = new VerifiCodeUtils.VerifiCodeStyle();
        verifiCodeStyle.setWidth(125);
        verifiCodeStyle.setHeight(40);
        verifiCodeStyle.setBgColor(new Color(231, 242, 249));
        VerifiCodeUtils vcImgObj = new VerifiCodeUtils(verifiCodeStyle);
        BufferedImage vcImg = vcImgObj.getVCImg();
        String reVerfiC = vcImgObj.getCodeValue().toString();
        request.getSession().setAttribute("verifiCode", reVerfiC);
        vcImgObj.outPutVCImg(response, vcImg);
    }

    //获取所有用户
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public DTO listUser(int pagenum, int pagesize) {
        return userService.listUser(pagenum, pagesize);
    }

    //获取某个用户的信息包括所有活动，所有好友
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public DTO getUserById(@PathVariable("uid") int uid) {
        return userService.getUserById(uid);
    }

    //添加用户
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public DTO saveUser(UserDO userDO) {
        return userService.reg(userDO, userDO.getType());
    }

    //修改某个用户的信息
    @RequestMapping(value = "/put", method = RequestMethod.POST)
    @ResponseBody
    public DTO updateUser(UserDO userDO) {
        return userService.updateUser(userDO);
    }

    //删除用户
    @RequestMapping(value = "/{uid}/del", method = RequestMethod.POST)
    @ResponseBody
    public DTO deleteUser(@PathVariable("uid") int uid) {
        return userService.delete(uid);
    }


}
