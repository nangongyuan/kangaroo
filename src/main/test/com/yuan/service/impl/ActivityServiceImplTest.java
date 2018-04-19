package com.yuan.service.impl;

import com.yuan.dto.DTO;
import com.yuan.service.IActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class ActivityServiceImplTest {
    @Autowired
    private IActivityService activityService;
    @Test
    public void searchActivity() throws Exception {
        DTO dto = activityService.searchActivity("123",1,100);
        System.out.println(dto);
    }

}