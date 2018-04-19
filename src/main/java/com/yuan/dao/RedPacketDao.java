package com.yuan.dao;

import com.yuan.entity.RedPacketDO;
import com.yuan.entity.RedPacketRecordDO;
import java.util.List;

/**
 * @program: kangaroo
 * @description: 红包
 * @author: yuan
 * @create: 2018-03-31 22:14
 **/

public interface RedPacketDao {
    Integer saveRedPacket(RedPacketDO redPacketDO);

    Integer saveRedPacketRecord(RedPacketRecordDO redPacketRecordDO);

    List<RedPacketDO> listRedPacketDOByAid(int aid);

    List<RedPacketRecordDO> listRedPacketRecordByRpid(int rpid);
}
