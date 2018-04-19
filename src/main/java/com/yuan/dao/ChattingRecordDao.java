package com.yuan.dao;

import com.yuan.entity.ChattingRecordDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChattingRecordDao {
    //添加聊天记录
    Integer saveChattingRecord(ChattingRecordDO chattingRecordDO);

    //获取好友发给我的未读消息
    List<ChattingRecordDO> listUnreadChattingRecord(@Param("sender") int sender, @Param("receiver") int receiver);

    //查询聊天记录
    List<ChattingRecordDO> listChattingRecord(@Param("uid1") int uid1, @Param("uid2") int uid2, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    //查询已经读过的消息
    List<ChattingRecordDO> listReadChattingRecord(@Param("uid1") int uid1, @Param("uid2") int uid2, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    //将未读消息改为已读
    Integer updateChattingRecordStatus(@Param("sender") int sender, @Param("receiver") int receiver);
}
