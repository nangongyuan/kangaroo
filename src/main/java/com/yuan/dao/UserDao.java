package com.yuan.dao;

import com.yuan.dto.UserDTO;
import com.yuan.dto.UserDetailDTO;
import com.yuan.entity.UserDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

    List<UserDO> listUserDO(@Param("map") Map map, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    Integer saveUser(UserDO userDO);

    Integer deleteUser(int uid);

    Integer updateUser(UserDO userDO);

    //查看会议的报名人
    List<UserDTO> listActivitySubscribleUsers(int aid);

    List<UserDTO> searchUser(@Param("wd") String wd, @Param("uid") int uid, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    List<UserDTO> listUserDTO(@Param("map") Map map, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    //获得约定活动的用户
    List<UserDTO> listActivitySubscrible(int aid);

    UserDetailDTO getUserDetailDTO(int uid);

}
