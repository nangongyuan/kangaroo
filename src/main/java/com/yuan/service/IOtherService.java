package com.yuan.service;

import com.yuan.dto.DTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

public interface IOtherService {
    DTO uploadHeadpic(MultipartFile file, HttpServletRequest request);

    DTO uploadAdvertising(MultipartFile file, HttpServletRequest request);

    DTO uploadChatimage(MultipartFile file, HttpServletRequest request);

    DTO loginScreen(String secretkey, HttpSession session);

    DTO uploadDisplaypic(MultipartFile file, HttpServletRequest request);
}
