package com.yuan.service;

import com.yuan.dto.DTO;
import com.yuan.entity.ActivityDO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;

public interface IActivityService {
    DTO saveActivity(ActivityDO activityDO, HttpServletRequest request);

    DTO updateActivity(ActivityDO activityDO);

    DTO deleteActivity(int aid);

    DTO subscribleActivity(int aid);

    DTO cancelSubscrible(int aid);

    DTO listActivitySubscribleUser(int aid);

    DTO getActivityById(int aid);

    DTO listActivity(int pageNum, int pageSize);

    DTO listActivityByTypeId(int typeid, int pageNum, int pageSize);

    DTO listRecommendActivity(int pageNum, int pageSize);

    DTO searchActivity(String wd, int pageNum, int pageSize);

    DTO clickActivity(int aid, String password, HttpSession session);

    DTO getActivityDetail(int aid);

    DTO getAttendedActivity();

    DTO getMyActivity();

    DTO getSubscribledActivity();

    DTO getVotedActivity(int aid);
}
