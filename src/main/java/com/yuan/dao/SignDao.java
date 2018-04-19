package com.yuan.dao;

import com.yuan.dto.UserDTO;
import com.yuan.entity.SignDO;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @program: kangaroo
 * @description: 签到
 * @author: yuan
 * @create: 2018-04-01 16:51
 **/

public interface SignDao {
    Integer saveSignDO(SignDO signDO);

    List<UserDTO> listSignUserByAid(int aid);

    List<UserDTO> listSignUserByAidTime(@Param("aid") int aid, @Param("s") Long s);

    //获取用户是否签到
    Integer getSign(@Param("aid") Integer aid,@Param("uid") Integer uid);


}
