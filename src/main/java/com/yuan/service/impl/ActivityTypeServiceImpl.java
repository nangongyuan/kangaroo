package com.yuan.service.impl;

import com.yuan.dao.ActivityTypeDao;
import com.yuan.entity.ActivityTypeDO;
import com.yuan.dto.DTO;
import com.yuan.service.IActivityTypeService;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityTypeServiceImpl implements IActivityTypeService{

    @Autowired
    private ActivityTypeDao activityTypeDao;

    public DTO listActivityType() {
        DTO result = new DTO();
        List<ActivityTypeDO> list = activityTypeDao.listActivityTypeDO(new HashMap<String, Object>());
        if (list!= null && list.size()>0){
            result.setSuccess(true);
            result.setData(list);
        }
        return result;
    }

    public DTO listUserActivityType(int uid){
        DTO result = new DTO();
        List<ActivityTypeDO> list = activityTypeDao.listUserActivityType(uid);
        if (list!= null && list.size()>0){
            result.setSuccess(true);
            result.setData(list);
        }
        return result;
    }

    public DTO saveActivityType(ActivityTypeDO activityTypeDO) {
        DTO result = new DTO();
        if (activityTypeDao.saveActivityType(activityTypeDO) > 0) {
            result.setSuccess(true);
        }
        return result;
    }

    public DTO updateUserActivityType(int uid, String interest) {
        DTO result = new DTO();
        String temp[] = interest.split(",");
        activityTypeDao.deleteUserActivityType(uid);
        if (temp!=null){
            for (int i=0;i<temp.length;i++){
                if (activityTypeDao.saveUserActivityType(uid,Integer.valueOf(temp[i]))<=0){
                    return result;
                }
            }
        }
        result.setSuccess(true);
        return result;
    }

    public DTO deleteActivityType(int typeid) {
        DTO result = new DTO();
        if (activityTypeDao.deleteActivityType(typeid) > 0) {
            result.setSuccess(true);
        }
        return result;
    }

    public DTO updateActivityType(ActivityTypeDO activityTypeDO) {
        DTO result = new DTO();
        if (activityTypeDao.updateActivityType(activityTypeDO) > 0) {
            result.setSuccess(true);
        }
        return result;
    }

}
