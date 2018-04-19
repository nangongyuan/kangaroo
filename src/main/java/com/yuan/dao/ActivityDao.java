package com.yuan.dao;

import com.yuan.dto.ActivityDTO;
import com.yuan.dto.ActivityDetailDTO;
import com.yuan.entity.ActivityDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ActivityDao {
    List<ActivityDO> listActivityDO(@Param("map") Map map, @Param("pageNumKey") int pageNum,
                                    @Param("pageSizeKey") int pageSize);

    Integer saveActivity(ActivityDO activityDO);


    Integer deleteActivity(int aid);

    Integer updateActivity(ActivityDO activityDO);

    List<ActivityDTO> listActivityDTO(@Param("map") Map map, @Param("pageNumKey") int pageNum,
                                      @Param("pageSizeKey") int pageSize);

    List<ActivityDTO> listRecommendActivity(@Param("uid") int uid, @Param("pageNumKey") int pageNum,
                                            @Param("pageSizeKey") int pageSize);

    List<Integer> listStartActivity();

    List<Integer> listEndActivity();

    Integer updateStatus(int id, int status);

    List<ActivityDTO> searchActivity(@Param("wd") String wd, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    ActivityDetailDTO getActivityDetail(int aid);

    //修改互动密钥
    int updateActivitySecretkey(@Param("aid") int aid, @Param("secretkey") String secretkey);

    int updateActivityCoords(@Param("aid") int aid, @Param("coords") String coords);

    //获取用户约定的活动
    List<ActivityDTO> listActivityBySubscrible(int uid);

    //获取用户参加的活动
    List<ActivityDTO> listActivityBySign(int uid);

    //根据密钥获取活动信息
    ActivityDO getActivityDOBysecretkey(String secretkey);


}
