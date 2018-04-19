package com.yuan.dao;

import com.yuan.dto.ActivityDTO;
import com.yuan.dto.UserDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SubscribleDao {
    Integer saveSubscrible(Integer aid, Integer uid, Integer valid);


    //获取用户约定该活动
    Integer getValidByAidUid(@Param("aid") Integer aid, @Param("uid") Integer uid);

    List<UserDTO> listSubscribleByAid(Integer aid);

    Integer updateSubscribleValid(@Param("aid") Integer aid, @Param("uid") Integer uid, @Param("valid") Integer valid);


}
