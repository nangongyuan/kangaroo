package com.yuan.service.impl;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.github.pagehelper.Page;
import com.yuan.dao.ChattingRecordDao;
import com.yuan.dao.FriendDao;
import com.yuan.dao.FriendRequestDao;
import com.yuan.dao.UserDao;
import com.yuan.dto.DTO;
import com.yuan.dto.FriendDTO;
import com.yuan.dto.FriendInfoDTO;
import com.yuan.dto.PageDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ChattingRecordDO;
import com.yuan.entity.FriendDO;
import com.yuan.entity.FriendRequestDO;
import com.yuan.entity.UserDO;
import com.yuan.service.IFriendService;
import com.yuan.shiro.ShiroUser;
import com.yuan.websocket.FriendWebSocketHandler;
import java.nio.channels.SelectableChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendServiceImpl implements IFriendService {
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendWebSocketHandler friendWebSocketHandler;

    @Autowired
    private FriendRequestDao friendRequestDao;

    @Autowired
    private ChattingRecordDao chattingRecordDao;

    public DTO listMyFriend() {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        List<FriendDTO> list = friendDao.listFriendDTOById(shiroUser.getId());
        if (list!=null && list.size()>0){
            for (int i=0;i <list.size();i++){
                List<ChattingRecordDO> chattingRecordDOS = chattingRecordDao.listUnreadChattingRecord(list.get(i).getId(),shiroUser.getId());
                if (chattingRecordDOS!=null && chattingRecordDOS.size()>0){
                    for (int j=0;j<chattingRecordDOS.size();j++){
                        if (chattingRecordDOS.get(j).getReceiver()==shiroUser.getId()){
                            list.get(i).setUnread(true);
                            break;
                        }
                    }
                }
            }
            dto.setSuccess(true);
            dto.setData(list);
        }
        return dto;
    }

    @Transactional
    public DTO saveFriend(int uid1, int uid2) {
        DTO dto = new DTO();
        FriendDO friendDO1 = new FriendDO();
        friendDO1.setUid1(uid1);
        friendDO1.setUid2(uid2);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",uid2);
        List<UserDO> list = userDao.listUserDO(map,1,1);
        if (list!=null && list.size()>0){
            friendDO1.setFriendremark(list.get(0).getName());
        }
        FriendDO friendDO2 = new FriendDO();
        friendDO2.setUid1(uid2);
        friendDO2.setUid2(uid1);
        map.put("id",uid1);
        list = userDao.listUserDO(map,1,1);
        if (list!=null && list.size()>0){
            friendDO2.setFriendremark(list.get(0).getName());
        }
        friendDO1.setRecentlychat(new Date());
        friendDO2.setRecentlychat(new Date());
        if (friendDao.saveFriend(friendDO1)>0 && friendDao.saveFriend(friendDO2)>0){
            dto.setSuccess(true);
        }
        return dto;
    }

    @Transactional
    public DTO removeFriend(int uid1, int uid2) {
        DTO dto = new DTO();
        if (uid2==0){
            return dto;
        }
        if (friendDao.removeFriend(uid1, uid2)>0 && friendDao.removeFriend(uid2, uid1)>0){
            dto.setSuccess(true);
        }
        return dto;
    }

    public DTO searchFriend(String wd, int pageNum,int pageSize) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        List<UserDTO> list = userDao.searchUser(wd,shiroUser.getId(),pageNum,pageSize);
        List<FriendDTO> friends = friendDao.listFriendDTOById(shiroUser.getId());
        if (list!=null && list.size()>0){
            if (friends!=null && friends.size()>0){
                Iterator<UserDTO> iterator = list.iterator();
                while (iterator.hasNext()){
                    UserDTO userDTO = iterator.next();
                    for (int i=0; i<friends.size();i++){
                        if (userDTO.getId().equals(friends.get(i).getId())){
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            dto.setSuccess(true);
            PageDTO<UserDTO> pageDTO = new PageDTO<UserDTO>();
            pageDTO.setTotal(((Page)list).getTotal());
            pageDTO.setRows(list);
            dto.setData(pageDTO);
        }
        return dto;
    }

    public DTO friendRequest(int uid) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        FriendRequestDO friendRequestDO = new FriendRequestDO(1,shiroUser.getId(),uid,0,null);
        if (uid==shiroUser.getId()){
            dto.setMsg("不能添加自己为好友");
            return dto;
        }
        FriendInfoDTO friendInfoDTO = friendDao.getFriendInfo(uid,shiroUser.getId());
        if (friendInfoDTO!=null){
            dto.setMsg("他已是您的好友");
            return dto;
        }
        List<UserDTO> list = friendRequestDao.listFriendRequest(uid);
        if (list!=null && list.size()>0){
            for (int i=0;i <list.size();i++){
                if (list.get(0).getId()==shiroUser.getId()){
                    dto.setMsg("你已发送请求");
                    return dto;
                }
            }
        }
        if (friendRequestDao.saveFriendRequest(friendRequestDO)>0){
            friendWebSocketHandler.sendFriendRequest(uid);
            dto.setSuccess(true);
        }
        return dto;
    }

    public DTO myFriendRequest() {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        List<UserDTO> list = friendRequestDao.listFriendRequest(shiroUser.getId());
        if (list!=null && list.size()>0){
            dto.setData(list);
            dto.setSuccess(true);
        }
        return dto;
    }

    @Transactional
    public DTO agreeFriend(int uid,int param) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (param==0){
            if (friendRequestDao.updateStatus(uid,shiroUser.getId(),1)>0){
                dto.setSuccess(true);
            }
            return dto;
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",uid);
        List<UserDTO> list = userDao.listUserDTO(map,1,1);
        FriendDO friendDO1 = new FriendDO(shiroUser.getId(),uid,list.get(0).getName(),new Date());
        FriendDO friendDO2 = new FriendDO(uid,shiroUser.getId(),shiroUser.getName(),new Date());
        if (friendDao.saveFriend(friendDO1)>0 && friendDao.saveFriend(friendDO2)>0 &&
                friendRequestDao.updateStatus(uid,shiroUser.getId(),1)>0){
                dto.setSuccess(true);
                friendWebSocketHandler.sendAgreeFriend(uid);
        }
        return dto;
    }

    public DTO getFriendInfo(int uid) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        FriendInfoDTO friendInfoDTO = friendDao.getFriendInfo(shiroUser.getId(),uid);
        if (friendInfoDTO!=null){
            dto.setSuccess(true);
            dto.setData(friendInfoDTO);
        }
        return dto;
    }

    public DTO updateFriendremark(int uid, String remark) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (friendDao.updateFriend(new FriendDO(shiroUser.getId(),uid,remark,new Date()))>0){
            dto.setSuccess(true);
        }
        return  dto;
    }


}
