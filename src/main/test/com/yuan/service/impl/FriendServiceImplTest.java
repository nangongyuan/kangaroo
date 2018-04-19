package com.yuan.service.impl;

import com.yuan.service.IFriendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class FriendServiceImplTest {
    @Autowired
    private IFriendService friendService;
    @Test
    public void saveFriend() throws Exception {
        friendService.saveFriend(10000116,10000144);
    }

}