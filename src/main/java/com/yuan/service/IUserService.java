package com.yuan.service;

import com.yuan.entity.UserDO;
import com.yuan.dto.DTO;

public interface IUserService {
    //登录方式
    int LOGIN_TYPE_CLIENT = 1;
    int LOGIN_TYPE_MANAGE = 0;
    int REGEDIT_TYPE_CLIENT = 1;
    int REGEDIT_TYPE_MANAGE = 0;

    DTO login(String name, String password, int type);

    DTO reg(UserDO userDO, int type);

    DTO repeat(String username);

    DTO getLoginUser();

    DTO updateUser(UserDO userDO);

    DTO listUser(int pageNum, int pageSize);

    DTO getUserById(int uid);

    DTO delete(int uid);

    DTO listSignByAidTime(int aid, long s);
}
