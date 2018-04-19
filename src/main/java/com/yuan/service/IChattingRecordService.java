package com.yuan.service;

import com.yuan.dto.DTO;

public interface IChattingRecordService {
    DTO listFriendChattingRecord(int fid);

    DTO updateChattingRecordStatus(int uid);

    DTO listChattingRecord(int uid, int fid, int pagenum, int pagesize);

    DTO listBarrageByAid(int aid, int pagenum, int pagesize);

}
