package com.yuan.service.impl;

import com.github.pagehelper.Page;
import com.yuan.dao.BarrageDao;
import com.yuan.dao.ChattingRecordDao;
import com.yuan.dto.BarrageDTO;
import com.yuan.dto.DTO;
import com.yuan.dto.PageDTO;
import com.yuan.entity.ChattingRecordDO;
import com.yuan.service.IChattingRecordService;
import com.yuan.shiro.ShiroUser;
import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.SHA;

@Service
public class ChattingRecordServiceImpl implements IChattingRecordService {

    @Autowired
    private ChattingRecordDao chattingRecordDao;

    @Autowired
    private BarrageDao barrageDao;

    public DTO listFriendChattingRecord(int uid) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        List<ChattingRecordDO> list1 = chattingRecordDao.listUnreadChattingRecord(uid,shiroUser.getId());
        if (list1!=null && list1.size()>0){
            int count = 10 - list1.size();
            if (count>0){
                List<ChattingRecordDO> list2 = chattingRecordDao.listReadChattingRecord(uid,shiroUser.getId(),1,count);
                if (list1!=null && list1.size()>0){
                    list1.addAll(list2);
                }
            }
            chattingRecordDao.updateChattingRecordStatus(uid,shiroUser.getId());
            dto.setSuccess(true);
            dto.setData(list1);
        }else{
            list1 = chattingRecordDao.listReadChattingRecord(uid,shiroUser.getId(),1,10);
            if (list1!=null && list1.size()>0){
                dto.setSuccess(true);
                dto.setData(list1);
            }
        }
        return dto;
    }

    public DTO updateChattingRecordStatus(int uid) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (chattingRecordDao.updateChattingRecordStatus(uid,shiroUser.getId())>0){
            dto.setSuccess(true);
        }
        return dto;
    }

    /**
    * @Description: 管理员获取聊天记录
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/3/29 0029
    */
    public DTO listChattingRecord(int uid, int fid, int pagenum, int pagesize) {
        DTO dto = new DTO();
        List<ChattingRecordDO> list = chattingRecordDao.listChattingRecord(uid,fid,pagenum,pagesize);
        if (list!=null && list.size()>0){
            PageDTO<ChattingRecordDO> pageDTO = new PageDTO<ChattingRecordDO>();
            pageDTO.setTotal(((Page)list).getTotal());
            pageDTO.setRows(list);
            dto.setSuccess(true);
            dto.setData(pageDTO);
        }
        return dto;
    }

    /**
    * @Description: 获取活动的弹幕
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/3 0003
    */
    public DTO listBarrageByAid(int aid,int pagenum,int pagesize) {
        DTO result = new DTO();
        List<BarrageDTO> list = barrageDao.listBarrageDTOByAid(aid,pagenum,pagesize);
        if (list!=null && list.size()>0){
            result.setSuccess(true);
            PageDTO<BarrageDTO> page = new PageDTO<BarrageDTO>();
            page.setRows(list);
            page.setTotal(((Page)list).getTotal());
            result.setData(page);
        }
        return result;
    }




}
