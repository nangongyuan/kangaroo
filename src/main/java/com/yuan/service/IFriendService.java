package com.yuan.service;

import com.yuan.dto.DTO;

public interface IFriendService {
    DTO listMyFriend();

    DTO saveFriend(int uid1, int uid2);

    DTO removeFriend(int uid1, int uid2);

    DTO searchFriend(String wd, int pageNum, int pageSize);

    DTO friendRequest(int uid);

    DTO myFriendRequest();

    DTO agreeFriend(int uid, int param);

    DTO getFriendInfo(int uid);

    DTO updateFriendremark(int uid, String remark);
}
