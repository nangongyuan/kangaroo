package com.yuan.web;

import com.yuan.dto.DTO;
import com.yuan.entity.ActivityDO;
import com.yuan.service.IActivityService;
import com.yuan.util.OtherUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: kangaroo
 * @description: 活动的请求
 * @author: yuan
 * @create: 2018-03-29 14:06
 **/

@Controller
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    //创建活动
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public DTO saveActivity(String s, String e, ActivityDO activityDO, HttpServletRequest request) {
        activityDO.setStarttime(OtherUtil.string2Date(s));
        activityDO.setEndtime(OtherUtil.string2Date(e));
        return activityService.saveActivity(activityDO, request);
    }

    //修改活动
    @RequestMapping(value = "/put", method = RequestMethod.POST)
    @ResponseBody
    public DTO updateActivity(String s, String e, ActivityDO activityDO) {
        activityDO.setStarttime(OtherUtil.string2Date(s));
        activityDO.setEndtime(OtherUtil.string2Date(e));
        return activityService.updateActivity(activityDO);
    }


    //删除活动
    @RequestMapping(value = "/{aid}/del", method = RequestMethod.POST)
    @ResponseBody
    public DTO deleteActivity(@PathVariable("aid") int aid) {
        return activityService.deleteActivity(aid);
    }


    //约定活动
    @RequestMapping(value = "/{aid}/subscrible", method = RequestMethod.POST)
    @ResponseBody
    public DTO subscribleActivity(@PathVariable("aid") int aid) {
        return activityService.subscribleActivity(aid);
    }

    //取消约定
    @RequestMapping(value = "/{aid}/cancel", method = RequestMethod.POST)
    @ResponseBody
    public DTO cancelSubscrible(@PathVariable("aid") int aid) {
        return activityService.cancelSubscrible(aid);
    }

    //查看会议的报名人
    @RequestMapping(value = "/{aid}/subscrible-user", method = RequestMethod.GET)
    @ResponseBody
    public DTO listActivitySubscribleUser(@PathVariable("aid") int aid) {
        return activityService.listActivitySubscribleUser(aid);
    }

    //获取所有活动
    @RequestMapping(value = "/type/0", method = RequestMethod.GET)
    @ResponseBody
    public DTO getActivityAll(int pagenum, int pagesize) {
        return activityService.listActivity(pagenum, pagesize);
    }

    //根据类型获取活动
    @RequestMapping(value = "/type/{typeid}", method = RequestMethod.GET)
    @ResponseBody
    public DTO getActivityTypeId(@PathVariable("typeid") int typeid, int pagenum, int pagesize) {
        return activityService.listActivityByTypeId(typeid, pagenum, pagesize);
    }

    //获取推荐
    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    @ResponseBody
    public DTO getRecommendActivity(int pagenum, int pagesize) {
        return activityService.listRecommendActivity(pagenum, pagesize);
    }


    /**
     * @Description: 搜索活动
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/1 0001
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public DTO searchActivity(String wd, int pagenum, int pagesize) {
        return activityService.searchActivity(wd, pagenum, pagesize);
    }

    //点击某个活动
    @GetMapping(value = "/{aid}/click")
    @ResponseBody
    public DTO clickActivity(@PathVariable("aid") int aid, String password, HttpSession session) {
        return activityService.clickActivity(aid, password, session);
    }

    //查看活动详细信息
    @RequestMapping(value = "/{aid}/detail", method = RequestMethod.GET)
    @ResponseBody
    public DTO getActivityDetail(@PathVariable("aid") int aid) {
        return activityService.getActivityDetail(aid);
    }

    //根据id获取活动信息
    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    @ResponseBody
    public DTO getActivityById(@PathVariable("aid") int aid) {
        return activityService.getActivityById(aid);
    }

    //获取当前登录用户参加过的活动
    @RequestMapping(value = "/attended", method = RequestMethod.GET)
    @ResponseBody
    public DTO getAttendedActivity() {
        return activityService.getAttendedActivity();
    }

    //获取当前用户创建的活动
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public DTO getMyActivity() {
        return activityService.getMyActivity();
    }

    //获取当前用户订阅的活动
    @RequestMapping(value = "/subscribled", method = RequestMethod.GET)
    @ResponseBody
    public DTO getSubscribledActivity() {
        return activityService.getSubscribledActivity();
    }

    //获取活动的投票信息
    @RequestMapping(value = "/{aid}/votes", method = RequestMethod.GET)
    @ResponseBody
    public DTO getVotedActivity(@PathVariable("aid") int aid) {
        return activityService.getVotedActivity(aid);
    }
}
