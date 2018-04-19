package com.yuan.dao;

import com.yuan.dto.UserDTO;
import com.yuan.entity.FriendRequestDO;
import java.io.PrintWriter;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FriendRequestDao {
    Integer saveFriendRequest(FriendRequestDO friendRequestDO);

    List<UserDTO> listFriendRequest(int uid);

    Integer updateStatus(@Param("sender") int sender, @Param("receiver") int receiver, @Param("status") int status);

}
