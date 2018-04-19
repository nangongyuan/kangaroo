package com.yuan.dao;

import com.yuan.dto.FriendDTO;
import com.yuan.dto.FriendInfoDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.FriendDO;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FriendDao {
    Integer saveFriend(FriendDO friendDO);

    List<FriendDTO> listFriendDTOById(int id);

    Integer updateFriend(FriendDO friendDO);

    Integer removeFriend(@Param("uid1") int uid1, @Param("uid2") int uid2);

    Integer updatRecentlychat(@Param("uid1") int uid1, @Param("uid2") int uid2);

    FriendInfoDTO getFriendInfo(@Param("uid1") int uid1, @Param("uid2") int uid2);

}
