package com.yuan.dao;

import com.yuan.entity.ActivityTypeDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ActivityTypeDao {
    List<ActivityTypeDO> listActivityTypeDO(@Param("map") Map map);

    Integer saveActivityType(ActivityTypeDO activityTypeDO);

    Integer deleteActivityType(int id);

    Integer updateActivityType(ActivityTypeDO activityTypeDO);

    List<ActivityTypeDO> listUserActivityType(int uid);

    Integer deleteUserActivityType(int uid);

    Integer saveUserActivityType(int uid, int typeid);

}
