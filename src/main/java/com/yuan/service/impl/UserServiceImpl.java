package com.yuan.service.impl;

import com.github.pagehelper.Page;
import com.yuan.dao.ActivityDao;
import com.yuan.dao.FriendDao;
import com.yuan.dao.SignDao;
import com.yuan.dao.UserDao;
import com.yuan.dto.PageDTO;
import com.yuan.dto.UserDTO;
import com.yuan.dto.UserDetailDTO;
import com.yuan.entity.FriendDO;
import com.yuan.entity.UserDO;
import com.yuan.dto.DTO;
import com.yuan.service.IUserService;
import com.yuan.shiro.ShiroUser;
import com.yuan.util.SaltUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private SignDao signDao;

    public DTO login(String name, String password, int type) {
        DTO result = new DTO();
        if (name == null || name.equals("") || password == null || password.equals("")) {
            result.setSuccess(false);
            result.setMsg("用户名或密码不能为空");
            return result;
        }
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        try {
            token.setRememberMe(true);
            user.login(token);
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            if (type == 0 && shiroUser.getType() == 1) {
                result.setMsg("权限不足");
                result.setSuccess(false);
            } else {
                result.setSuccess(true);
                shiroUser.setLoginType(type);
            }
        } catch (UnknownAccountException e) {
            result.setMsg("帐号Id或帐号名不存在");
        } catch (DisabledAccountException e) {
            result.setMsg("账号已被禁用");
        } catch (IncorrectCredentialsException e) {
            result.setMsg("密码错误");
        } catch (Throwable e) {
            result.setMsg("密码错误");
        }
        return result;
    }

    private DTO checkUser(UserDO userDO) {
        DTO result = new DTO();
        if (userDO.getName().length() < 2 || userDO.getName().length() > 20 || userDO.getPassword().length() < 6 ||
                userDO.getPassword().length() > 15) {
            result.setSuccess(false);
            result.setMsg("用户名或密码长度不正确");
            return result;
        }
        byte[] names = userDO.getName().getBytes();
        int i;
        for (i = 0; i < names.length; i++) {
            if (names[i] < 48 || names[i] > 57) {
                break;
            }
        }
        if (i >= names.length) {
            result.setSuccess(false);
            result.setMsg("用户名不能为纯数字");
            return result;
        }
        result.setSuccess(true);
        return result;
    }

    @Transactional
    public DTO reg(UserDO userDO, int type) {
        DTO result = new DTO();
        result = checkUser(userDO);
        if (!result.getSuccess()) {
            return result;
        }
        userDO.setLocked(0);
        userDO.setSalt(SaltUtil.getRandSalt());
        userDO.setPassword(new Md5Hash(userDO.getPassword(), userDO.getSalt()).toString());
        userDO.setType(type);
        if (userDao.saveUser(userDO) > 0) {
            result.setSuccess(true);
        }
        //添加管理员为好友
        FriendDO friendDO = new FriendDO(0, userDO.getId(), userDO.getName(), new Date());
        friendDao.saveFriend(friendDO);
        FriendDO friend = new FriendDO(userDO.getId(), 0, "小袋", new Date());
        friendDao.saveFriend(friend);

        return result;
    }

    public DTO repeat(String username) {
        DTO result = new DTO();
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("name", username);
        List<UserDO> userDOInfos = userDao.listUserDO(userMap, 1, 1);
        if (userDOInfos != null && userDOInfos.size() > 0) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    public DTO getLoginUser() {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", shiroUser.getId());
        List<UserDO> userDOS = userDao.listUserDO(map, 1, 1);
        if (userDOS == null || userDOS.size() <= 0) {
            result.setSuccess(false);
        } else {
            result.setSuccess(true);
            UserDO userDO = userDOS.get(0);
            result.setData(new UserDTO(userDO.getId(), userDO.getName(), userDO.getSex(), userDO.getArea(), userDO.getPhone(),
                    userDO.getHeadpic()));
        }
        return result;
    }


    public DTO updateUser(UserDO userDO) {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (shiroUser.getLoginType() == 1) {
            userDO.setType(1);
            userDO.setName(shiroUser.getName());
            userDO.setLocked(0);
            userDO.setId(shiroUser.getId());
        }
        if (userDao.updateUser(userDO) > 0) {
            result.setSuccess(true);
        }
        return result;
    }


    public DTO listUser(int pageNum, int pageSize) {
        DTO result = new DTO();
        List<UserDO> userDOS = userDao.listUserDO(new HashMap(), pageNum, pageSize);
        if (userDOS != null && userDOS.size() > 0) {
            List<UserDTO> results = new ArrayList<UserDTO>();
            for (int i = 0; i < userDOS.size(); i++) {
                UserDO userDO = userDOS.get(i);
                results.add(new UserDTO(userDO.getId(), userDO.getName(), userDO.getSex(), userDO.getArea(), userDO.getPhone(),
                        userDO.getHeadpic()));
            }
            PageDTO<UserDTO> pageDTO = new PageDTO<UserDTO>();
            pageDTO.setTotal(((Page) userDOS).getTotal());
            pageDTO.setRows(results);
            result.setData(pageDTO);
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * @Description: 根据id获取用户的详细信息
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public DTO getUserById(int uid) {
        DTO result = new DTO();
        UserDetailDTO userDetailDTO = userDao.getUserDetailDTO(uid);
        if (userDetailDTO != null) {
            userDetailDTO.setFriends(friendDao.listFriendDTOById(uid));
            userDetailDTO.setParticipatingActivities(activityDao.listActivityBySign(uid));
            userDetailDTO.setSubscribleActivities(activityDao.listActivityBySubscrible(uid));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid", uid);
            userDetailDTO.setMyActivities(activityDao.listActivityDTO(map, 1, 100000));
            result.setData(userDetailDTO);
            result.setSuccess(true);
        }
        return result;
    }


    public DTO delete(int uid) {
        DTO result = new DTO();
        if (userDao.deleteUser(uid) > 0) {
            result.setSuccess(true);
        }
        return result;
    }


    public DTO listSignByAidTime(int aid, long s) {
        DTO result = new DTO();
        try {
            List<UserDTO> list = signDao.listSignUserByAidTime(aid, s/1000);
            result.setSuccess(true);
            result.setMsg(String.valueOf(new Date().getTime()));
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
