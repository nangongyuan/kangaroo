package com.yuan.dao;

import com.yuan.entity.ResourceDO;
import java.util.List;

/**
 * @program: kangaroo
 * @description: 获取url权限
 * @author: yuan
 * @create: 2018-04-05 13:01
 **/

public interface ResourceDao {
    List<ResourceDO> listResource();
}
