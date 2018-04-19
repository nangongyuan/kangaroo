package com.yuan.web;

import com.yuan.dto.DTO;
import com.yuan.service.IChattingRecordService;
import com.yuan.service.ILotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/chattingrecord")
public class ChattingRecordController {

    @Autowired
    private IChattingRecordService chattingRecordService;

    @Autowired
    private ILotteryService lotteryService;

    //获取和好友的聊天记录
    @GetMapping(value = "/{uid}/friend")
    @ResponseBody
    public DTO listFriendChattingRecord(@PathVariable("uid") int uid) {
        return chattingRecordService.listFriendChattingRecord(uid);
    }

    //修改聊天记录的状态（已看|未看）
    @PostMapping(value = "/{uid}/status")
    @ResponseBody
    public DTO updateChattingRecordStatus(@PathVariable("uid") int uid) {
        return chattingRecordService.updateChattingRecordStatus(uid);
    }

    //获取聊天记录
    @GetMapping()
    @ResponseBody
    public DTO listChattingRecord(int uid, int fid, int pagenum, int pagesize) {
        return chattingRecordService.listChattingRecord(uid, fid, pagenum, pagesize);
    }

    //获取弹幕记录
    @GetMapping(value = "/{aid}/barrage")
    @ResponseBody
    public DTO listBarrageByAid(@PathVariable("aid") int aid, int pagenum, int pagesize) {
        return chattingRecordService.listBarrageByAid(aid, pagenum, pagesize);
    }
    //获取抽奖记录
    @GetMapping(value = "/{aid}/lottery")
    @ResponseBody
    public DTO listLotteryByAid(@PathVariable("aid") int aid) {
        return lotteryService.listLotteryByAid(aid);
    }

}
