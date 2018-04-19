package com.yuan.service;

import com.yuan.dto.SocketDTO;
import com.yuan.entity.ActivitySocket;

/**
 * @program: kangaroo
 * @description: 评分
 * @author: yuan
 * @create: 2018-03-31 22:38
 **/

public interface IGradeService {
    void startGrade(SocketDTO socketDTO, ActivitySocket activitySocket);

    void userGrade(SocketDTO socketDTO);
}
