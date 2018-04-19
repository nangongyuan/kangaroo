package com.yuan.web;

import com.yuan.entity.ActivityTypeDO;
import com.yuan.dto.DTO;
import com.yuan.service.IActivityTypeService;
import com.yuan.shiro.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/activity-type")
public class ActivityTypeController {

    @Autowired
    private IActivityTypeService activityTypeService;

    //获取所有活动类型
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public DTO getAllActivityType() {
        return activityTypeService.listActivityType();
    }

    //添加活动类型
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public DTO addActivityType(ActivityTypeDO activityTypeDO) {
        return activityTypeService.saveActivityType(activityTypeDO);
    }

    //获取当前用户感兴趣的活动类型
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public DTO getLoginUserActivityType() {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return activityTypeService.listUserActivityType(shiroUser.getId());
    }

    //根据id获取用户感兴趣的活动类型
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public DTO getUserActivityType(@PathVariable("uid") int uid) {
        return activityTypeService.listUserActivityType(uid);
    }

    //修改用户感兴趣的活动类型
    @RequestMapping(value = "/{uid}/put", method = RequestMethod.POST)
    @ResponseBody
    public DTO updateUserActivityType(@PathVariable("uid") int uid, String interest) {
        return activityTypeService.updateUserActivityType(uid, interest);
    }

    //删除活动类型
    @RequestMapping(value = "/{typeid}/del", method = RequestMethod.POST)
    @ResponseBody
    public DTO deleteActivityType(@PathVariable("typeid") int typeid) {
        return activityTypeService.deleteActivityType(typeid);
    }

    //修改当前用户感兴趣的活动类型
    @RequestMapping(value = "/me-put", method = RequestMethod.POST)
    @ResponseBody
    public DTO updateLoginUserActivityType(String interest) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return activityTypeService.updateUserActivityType(shiroUser.getId(), interest);
    }

    //修改活动类型
    @RequestMapping(value = "/put", method = RequestMethod.POST)
    @ResponseBody
    public DTO updateActivityType(ActivityTypeDO activityTypeDO) {
        return activityTypeService.updateActivityType(activityTypeDO);
    }
}
