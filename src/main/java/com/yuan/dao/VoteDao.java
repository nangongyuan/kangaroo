package com.yuan.dao;

import com.yuan.entity.VoteDO;
import com.yuan.entity.VoteGroup;
import com.yuan.entity.VoteRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @program: kangaroo
 * @description: 投票相关的数据库操作
 * @author: yuan
 * @create: 2018-03-30 22:48
 **/

public interface VoteDao {
    //添加投票
    int saveVote(VoteDO voteDO);

    //添加投票分组
    int saveVoteGroup(VoteGroup voteGroup);

    //添加投票记录
    int saveVoteRecord(VoteRecord voteRecord);

    //该分组的票数加1
    int addVoteGroupNum(int id);

    //根据活动id获取投票
    List<VoteDO> listVoteDOByAid(int aid);

    //根据投票id获取投票分组
    List<VoteGroup> listVoteGroupByVid(int voteid);

    //根据投票分组id获取用户投票记录
    List<VoteRecord> listVoteRecordByGid(int groupid);

    //获取用户投给了哪个组
    List<Integer> getVotedGroup(@Param("voteid") int voteid, @Param("uid") int uid);

    VoteDO getVoteDOById(int id);


}
