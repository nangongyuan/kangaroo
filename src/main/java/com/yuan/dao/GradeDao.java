package com.yuan.dao;

import com.yuan.entity.GradeDO;
import org.apache.ibatis.annotations.Param;

/**
 * @program: kangaroo
 * @description: 评分
 * @author: yuan
 * @create: 2018-03-31 22:37
 **/

public interface GradeDao {
    int saveGrade(GradeDO gradeDO);

    int addGrade(@Param("aid") int aid, @Param("num") int num);

    GradeDO getGradeDOByAid(int aid);

}
