package com.yuan.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.yuan.dao.ActivityDao;
import com.yuan.dao.FriendDao;
import com.yuan.dao.GradeDao;
import com.yuan.dao.LotteryDao;
import com.yuan.dao.RedPacketDao;
import com.yuan.dao.SignDao;
import com.yuan.dao.SubscribleDao;
import com.yuan.dao.UserDao;
import com.yuan.dao.VoteDao;
import com.yuan.dto.ActivityDTO;
import com.yuan.dto.ActivityDetailDTO;
import com.yuan.dto.ManagerActivityDTO;
import com.yuan.dto.PageDTO;
import com.yuan.dto.DTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ActivityDO;
import com.yuan.entity.GradeDO;
import com.yuan.entity.LotteryDO;
import com.yuan.entity.RedPacketDO;
import com.yuan.entity.RedPacketRecordDO;
import com.yuan.entity.VoteDO;
import com.yuan.entity.VoteGroup;
import com.yuan.entity.VoteRecord;
import com.yuan.service.IActivityService;
import com.yuan.shiro.ShiroUser;
import com.yuan.util.OtherUtil;
import com.yuan.websocket.ActivityWebSocketHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.input.NullInputStream;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SubscribleDao subscribleDao;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    @Autowired
    private RedPacketDao redPacketDao;

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private SignDao signDao;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private LotteryDao lotteryDao;

    /**
     * @Description: 创建活动
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    @Transactional
    public DTO saveActivity(ActivityDO activityDO, HttpServletRequest request) {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        activityDO.setUid(shiroUser.getId());
        activityDO.setStatus(1);

        if (activityDao.saveActivity(activityDO) > 0) {
            try {
                if (activityDao.updateActivitySecretkey(activityDO.getId(), OtherUtil.getSecretkey() + activityDO.getId()) > 0) {
                    Date now = new Date();
                    if (now.getTime() >= activityDO.getStarttime().getTime() &&
                            now.getTime() < activityDO.getEndtime().getTime()) {
                        activityDao.updateStatus(activityDO.getId(), 0);
                        activityWebSocketHandler.createActivity(activityDO.getId());
                    }
                    result.setSuccess(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @Description: 修改活动
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public DTO updateActivity(ActivityDO activityDO) {
        DTO result = new DTO();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", activityDO.getId());
        List<ActivityDO> list = activityDao.listActivityDO(map, 1, 1);
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (shiroUser.getType() == 1 && !list.get(0).getUid().equals(shiroUser.getId())) {
            result.setMsg("权限不足");
            return result;
        }
        try {
            if (activityDao.updateActivity(activityDO) > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 删除活动
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public DTO deleteActivity(int aid) {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", aid);
        List<ActivityDO> list = activityDao.listActivityDO(map, 1, 1);
        if (list != null && list.size() > 0) {
            if (shiroUser.getType() == 1 && list.get(0).getUid() != shiroUser.getId()) {
                result.setMsg("权限不足");
                return result;
            }
        }
        try {
            if (activityDao.deleteActivity(aid) > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @Description: 约定活动
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public DTO subscribleActivity(int aid) {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        try {
            if (subscribleDao.saveSubscrible(aid, shiroUser.getId(), 1) > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //取消约定
    public DTO cancelSubscrible(int aid) {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        try {
            if (subscribleDao.updateSubscribleValid(aid, shiroUser.getId(), 0) > 0) {
                result.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //活动约定人
    public DTO listActivitySubscribleUser(int aid) {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        try {
            if (shiroUser.getType() == 1) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", aid);
                List<ActivityDO> activities = activityDao.listActivityDO(map, 1, 1);
                if (activities != null && activities.size() > 0) {
                    if (activities.get(0).getUid().equals(shiroUser.getId())) {
                        result.setData(userDao.listActivitySubscribleUsers(aid));
                        result.setSuccess(true);
                    }
                }
            } else {
                result.setData(userDao.listActivitySubscribleUsers(aid));
                result.setSuccess(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 后台根据id获取活动的信息
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/1 0001
     */
    public DTO getActivityById(int aid) {
        DTO dto = new DTO();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", aid);
            List<ActivityDO> list = activityDao.listActivityDO(map, 1, 1);
            if (list != null && list.size() > 0) {
                ManagerActivityDTO managerActivityDTO = new ManagerActivityDTO();
                managerActivityDTO.setActivityDO(list.get(0));
                //获取所有红包记录
                List<RedPacketDO> redPackets = redPacketDao.listRedPacketDOByAid(aid);
                managerActivityDTO.setRedPackets(redPackets);
                for (RedPacketDO redPacketDO : redPackets) {
                    List<RedPacketRecordDO> redPacketRecords = redPacketDao.listRedPacketRecordByRpid(redPacketDO.getId());
                    redPacketDO.setRedPacketRecords(redPacketRecords);
                }
                //获取所有投票记录
                List<VoteDO> votes = voteDao.listVoteDOByAid(aid);
                managerActivityDTO.setVotes(votes);
                for (VoteDO voteDO : votes) {
                    List<VoteGroup> groups = voteDao.listVoteGroupByVid(voteDO.getId());
                    voteDO.setVoteGroups(groups);
                    for (VoteGroup voteGroup : groups) {
                        List<VoteRecord> records = voteDao.listVoteRecordByGid(voteGroup.getId());
                        voteGroup.setVoteRecords(records);
                    }
                }
                //获取所有签到的人
                List<UserDTO> signs = signDao.listSignUserByAid(aid);
                managerActivityDTO.setSigns(signs);
                //获取评分
                GradeDO grade = gradeDao.getGradeDOByAid(aid);
                if (grade != null && grade.getNumber() > 0) {
                    managerActivityDTO.setGrade((grade.getSum() * 1.0F) / grade.getNumber());
                }
                //获取抽奖
                List<LotteryDO> lotteries = lotteryDao.listLotteryDOByAid(aid);
                managerActivityDTO.setLotteries(lotteries);
                for (LotteryDO lotteryDO : lotteries) {
                    lotteryDO.setLotteryRecords(lotteryDao.listWinningUser(lotteryDO.getId()));
                }
                //获取预定的人
                List<UserDTO> subscribles = subscribleDao.listSubscribleByAid(aid);
                managerActivityDTO.setSubscrible(subscribles);
                dto.setData(managerActivityDTO);
            }
            dto.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }


    //获取所有活动
    public DTO listActivity(int pageNum, int pageSize) {
        DTO result = new DTO();
        try {
            List<ActivityDTO> activities = activityDao.listActivityDTO(new HashMap(), pageNum, pageSize);
            PageDTO<ActivityDTO> pageDTO = new PageDTO<ActivityDTO>();
            pageDTO.setTotal(((Page) activities).getTotal());
            pageDTO.setRows(activities);
            result.setData(pageDTO);
            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //根据活动类型获取活动
    public DTO listActivityByTypeId(int typeid, int pageNum, int pageSize) {
        DTO result = new DTO();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("typeid", typeid);
            List<ActivityDTO> activitys = activityDao.listActivityDTO(map, pageNum, pageSize);
            result.setSuccess(true);
            PageDTO<ActivityDTO> pageDTO = new PageDTO<ActivityDTO>();
            pageDTO.setRows(activitys);
            pageDTO.setTotal(((Page) activitys).getTotal());
            result.setData(pageDTO);
            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //获取推荐活动
    public DTO listRecommendActivity(int pageNum, int pageSize) {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        try {
            List<ActivityDTO> list = activityDao.listRecommendActivity(shiroUser.getId(), pageNum, pageSize);
            PageDTO<ActivityDTO> pageDTO = new PageDTO<ActivityDTO>();
            pageDTO.setRows(list);
            pageDTO.setTotal(((Page) list).getTotal());
            result.setData(pageDTO);
            result.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }



    //搜索活动
    public DTO searchActivity(String wd, int pageNum, int pageSize) {
        DTO dto = new DTO();
        try {
            List<ActivityDTO> list = activityDao.searchActivity(wd, pageNum, pageSize);
            dto.setSuccess(true);
            PageDTO<ActivityDTO> pageDTO = new PageDTO<ActivityDTO>();
            long l = ((Page) list).getTotal();
            pageDTO.setTotal(l);
            pageDTO.setRows(list);
            dto.setData(pageDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }

    //点击活动
    public DTO clickActivity(int aid,String password,HttpSession session) {
        DTO dto = new DTO();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id",aid);
        List<ActivityDO> list = activityDao.listActivityDO(map,1,1);
        if (list!=null && list.size()>0){
            dto.setSuccess(true);                 //0可以进入 1未开始  2已结束  3密码错误  4超过最大人数
            ActivityDO activityDO = list.get(0);
            if (activityDO.getStatus()==1){
                dto.setData(1);
            }else if (activityDO.getStatus()==2){
                dto.setData(2);
            }else if (activityDO.getStatus()==0){
                if (activityWebSocketHandler.getActivityPeopleNumber(aid)>=activityDO.getMax()) {
                    dto.setData(4);
                }else{
                    if (activityDO.getPassword()!=null && !activityDO.getPassword().equals("")){
                        if (password.equals(activityDO.getPassword())){
                            session.setAttribute("aid",aid);
                            dto.setData(0);
                        }else{
                            dto.setData(3);
                        }
                    }else{
                        session.setAttribute("aid",aid);
                        dto.setData(0);
                    }
                }
            }
        }
        return dto;
    }

    /**
    * @Description: 获得活动的详细信息
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/3/29 0029
    */
    public DTO getActivityDetail(int aid) {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        ActivityDetailDTO activityDetailDTO = activityDao.getActivityDetail(aid);
        if (!shiroUser.getId().equals(activityDetailDTO.getUid())){
            activityDetailDTO.setSecretkey(null);
        }
        if (activityDetailDTO!=null){
            dto.setSuccess(true);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",aid);
            List<ActivityDO> a = activityDao.listActivityDO(map,1,1);
            if (a.get(0).getPassword()!=null && !a.get(0).getPassword().equals("")){
                activityDetailDTO.setPassword(true);
            }
            if (a.get(0).getUid()==shiroUser.getId()){
                activityDetailDTO.setSecretkey(a.get(0).getSecretkey());
            }
            //当前活动预定人数
            List<UserDTO> list = userDao.listActivitySubscrible(aid);
            if (list!=null){
                activityDetailDTO.setSubscribeNum(list.size());
            }
            //当前用户是否已经预定
            Integer i = subscribleDao.getValidByAidUid(aid,shiroUser.getId());
            activityDetailDTO.setSubscribe(i);
            //如果是正在进行的活动获取在线人数
            if (activityDetailDTO.getStatus()==0){
                activityDetailDTO.setOnlineNum(activityWebSocketHandler.getActivityPeopleNumber(activityDetailDTO.getId()));
            }
            dto.setData(activityDetailDTO);
        }
        return dto;
    }
    
    /** 
    * @Description: 获取当前登录用户参加过的游戏
    * @Param:  
    * @return:  
    * @Author: yuan 
    * @Date: 2018/4/3 0003 
    */ 
    public DTO getAttendedActivity() {
        DTO dto = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        try {
            List<ActivityDTO> list = activityDao.listActivityBySign(shiroUser.getId());
            dto.setSuccess(true);
            dto.setData(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dto;
    }

    /** 
    * @Description: 获取当前登录用户创建的活动 
    * @Param:  
    * @return:  
    * @Author: yuan 
    * @Date: 2018/4/3 0003 
    */ 
    public DTO getMyActivity() {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uid",shiroUser.getId());
            List<ActivityDTO> list = activityDao.listActivityDTO(map,1,100000);
            result.setSuccess(true);
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public DTO getSubscribledActivity() {
        DTO result = new DTO();
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        try {
            List<ActivityDTO> list = activityDao.listActivityBySubscrible(shiroUser.getId());
            result.setSuccess(true);
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public DTO getVotedActivity(int aid) {
        DTO result = new DTO();
        try {
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            List<VoteDO> list = voteDao.listVoteDOByAid(aid);
            for (VoteDO voteDO : list){
                voteDO.setVoteIndexs(voteDao.getVotedGroup(voteDO.getId(),shiroUser.getId()));
                voteDO.setVoteGroups(voteDao.listVoteGroupByVid(voteDO.getId()));
            }
            result.setSuccess(true);
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
