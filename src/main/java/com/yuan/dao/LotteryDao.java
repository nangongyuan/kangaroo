package com.yuan.dao;

import com.yuan.dto.UserDTO;
import com.yuan.entity.LotteryDO;
import com.yuan.entity.LotteryRecordDO;
import java.util.List;

/**
 * @program: kangaroo
 * @description: 抽奖
 * @author: yuan
 * @create: 2018-03-31 12:13
 **/

public interface LotteryDao {
    Integer saveLottery(LotteryDO lotteryDO);

    Integer saveLotteryRecord(LotteryRecordDO lotteryRecordDO);

    List<UserDTO> listWinningUser(Integer lotteryid);

    List<LotteryDO> listLotteryDOByAid(Integer aid);
}
