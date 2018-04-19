package com.yuan.service.impl;

import com.yuan.dao.ActivityDao;
import com.yuan.dto.ImageDTO;
import com.yuan.dto.DTO;
import com.yuan.entity.ActivityDO;
import com.yuan.service.IOtherService;
import com.yuan.util.OtherUtil;
import com.yuan.web.UserController;
import com.yuan.websocket.ActivityWebSocketHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import static com.yuan.constant.SessionConstant.SESSION_AID;
import static com.yuan.constant.SessionConstant.SESSION_SCREEN;

@Service
public class OtherServiceImpl implements IOtherService {


    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    private DTO uploadImg(MultipartFile file, HttpServletRequest request, String pathName){
        DTO result = new DTO();
        if (file!=null) {// 判断上传的文件是否为空
            String path=null;// 文件路径
            String type=null;// 文件类型
            String fileName=file.getOriginalFilename();// 文件原名称
            // 判断文件类型
            type=fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()):null;
            if (type!=null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"JPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath=request.getSession().getServletContext().getRealPath("/")+pathName;
                    String trueFileName = null;
                    do{
                        // 自定义的文件名称
                        trueFileName=String.valueOf(System.currentTimeMillis())+String.valueOf((int)Math.random()*10000)+"."+type;
                        // 设置存放图片文件的路径
                        path=realPath+trueFileName;
                    }while (new File(path).exists());
                    // 转存文件到指定的路径
                    try {
                        file.transferTo(new File(path));
                        result.setSuccess(true);
                        File picture = new File(path);
                        BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
                        result.setData(new ImageDTO(trueFileName,sourceImg.getWidth(),sourceImg.getHeight()));
                    } catch (IOException e) {
                        result.setSuccess(false);
                        return result;
                    }
                }else {
                    result.setSuccess(false);
                    result.setMsg("文件类型错误");
                }
            }else {
                result.setSuccess(false);
                result.setMsg("文件类型为空");
                System.out.println("文件类型为空");
            }
        }else {
            result.setSuccess(false);
            result.setMsg("没有找到相对应的文件");
        }
        return result;
    }
    public DTO uploadHeadpic(MultipartFile file, HttpServletRequest request) {
        return uploadImg(file,request,"headpics\\");
    }

    public DTO uploadAdvertising(MultipartFile file, HttpServletRequest request) {
        return uploadImg(file,request,"advertisings\\");
    }

    public DTO uploadChatimage(MultipartFile file, HttpServletRequest request) {
        return uploadImg(file,request,"chatimages\\");
    }

    public DTO uploadDisplaypic(MultipartFile file, HttpServletRequest request) {
        return uploadImg(file,request,"display-pictures\\");
    }

    public DTO loginScreen(String secretkey, HttpSession session) {
        DTO result = new DTO();
        ActivityDO activityDO = activityDao.getActivityDOBysecretkey(secretkey);
        if (activityDO!=null){
            if (activityWebSocketHandler.getActivitySocket(activityDO.getId())!=null){
                result.setSuccess(true);
                session.setAttribute(SESSION_AID,activityDO.getId());
                session.setAttribute(SESSION_SCREEN,true);
            }else{
                result.setMsg("当前活动不在进行状态");
            }
        }else{
            result.setMsg("密钥错误");
        }
        return result;
    }


}
