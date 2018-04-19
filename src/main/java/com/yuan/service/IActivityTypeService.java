package com.yuan.service;

import com.yuan.entity.ActivityTypeDO;
import com.yuan.dto.DTO;

public interface IActivityTypeService {
    DTO listActivityType();

    DTO listUserActivityType(int uid);

    DTO saveActivityType(ActivityTypeDO activityTypeDO);

    DTO updateUserActivityType(int uid, String interest);

    DTO deleteActivityType(int typeid);

    DTO updateActivityType(ActivityTypeDO activityTypeDO);
}
