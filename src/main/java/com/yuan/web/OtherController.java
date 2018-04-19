package com.yuan.web;

import com.yuan.dto.DTO;
import com.yuan.service.IOtherService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class OtherController {

    @Autowired
    private IOtherService otherService;

    //上传头像
    @RequestMapping(value = "/avatars", method = RequestMethod.POST)
    @ResponseBody
    public DTO uploadHeadpic(MultipartFile file, HttpServletRequest request) {
        return otherService.uploadHeadpic(file, request);
    }

    //上传宣传图
    @RequestMapping(value = "/advertising", method = RequestMethod.POST)
    @ResponseBody
    public DTO uploadAdvertising(MultipartFile file, HttpServletRequest request) {
        return otherService.uploadAdvertising(file, request);
    }

    //好友聊天图片上传
    @RequestMapping(value = "/chatimages", method = RequestMethod.POST)
    @ResponseBody
    public DTO uploadChatimage(MultipartFile file, HttpServletRequest request) {
        return otherService.uploadChatimage(file, request);
    }

    //大屏幕登录
    @RequestMapping(value = "/screen/login", method = RequestMethod.GET)
    @ResponseBody
    public DTO loginScreen(String secretkey, HttpSession session) {
        return otherService.loginScreen(secretkey, session);
    }

    //上传大屏幕图片
    @RequestMapping(value = "/displaypic", method = RequestMethod.POST)
    @ResponseBody
    public DTO uploadDisplaypic(MultipartFile file, HttpServletRequest request) {
        return otherService.uploadDisplaypic(file, request);
    }

}
