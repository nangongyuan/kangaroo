package com.yuan.web;

import com.yuan.dto.DTO;
import com.yuan.service.IFriendService;
import com.yuan.shiro.ShiroUser;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private IFriendService friendService;

    //获取当前登录用户的好友列表
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public DTO listMyFriend() {
        return friendService.listMyFriend();
    }

    //发送好友请求
    @PostMapping(value = "/{uid}")
    @ResponseBody
    public DTO friendRequest(@PathVariable("uid") int id) {
        return friendService.friendRequest(id);
    }

    //删除好友
    @PostMapping(value = "/{uid}/del")
    @ResponseBody
    public DTO removeFriend(@PathVariable("uid") int id) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return friendService.removeFriend(shiroUser.getId(), id);
    }

    //搜索好友
    @GetMapping(value = "/search")
    @ResponseBody
    public DTO searchFriend(String wd, int pagenum, int pagesize) {
        return friendService.searchFriend(wd, pagenum, pagesize);
    }

    //所取我的所有好友请求
    @GetMapping(value = "/request")
    @ResponseBody
    public DTO friendRequest() {
        return friendService.myFriendRequest();
    }

    //同意好友
    @PostMapping(value = "/{uid}/agree")
    @ResponseBody
    public DTO agreeFriend(@PathVariable("uid") int uid, int param) {
        return friendService.agreeFriend(uid, param);
    }

    //获取好友信息
    @GetMapping(value = "/{uid}")
    @ResponseBody
    public DTO getFriendInfo(@PathVariable("uid") int uid) {
        return friendService.getFriendInfo(uid);
    }

    //修改好友备注
    @PostMapping(value = "/friendremark")
    @ResponseBody
    public DTO updateFriendremark(int uid, String remark) {
        return friendService.updateFriendremark(uid, remark);
    }
}
