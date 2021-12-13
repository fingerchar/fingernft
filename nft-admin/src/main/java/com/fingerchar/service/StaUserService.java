package com.fingerchar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcUserLog;
import com.fingerchar.domain.StaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/*
 *
 * @author zjm
 * */
@Service
public class StaUserService {
    @Autowired
    private IBaseService baseService;

    public Map<String, Integer> getList(Long staTime) {
        if (null == staTime) {
            staTime = System.currentTimeMillis() / 1000;
        }
        QueryWrapper<StaUser> wrapper = new QueryWrapper<>();
        Map<String, Integer> result = new HashMap<>();
        wrapper.eq(StaUser.STA_TIME, staTime );
        StaUser today = baseService.getByCondition(StaUser.class, wrapper);
        if (null == today){
            today = new StaUser();
            today.setLoginAmount(0);
        }
        QueryWrapper<StaUser> yesterdayWrapper = new QueryWrapper<>();
        yesterdayWrapper.eq(StaUser.STA_TIME, staTime - 24 * 60 * 60);
        StaUser yesterday = baseService.getByCondition(StaUser.class, yesterdayWrapper);
        if (null == yesterday){
            yesterday = new StaUser();
            yesterday.setLoginAmount(0);
        }
        result.put("today", today.getLoginAmount());
        result.put("yesterday", yesterday.getLoginAmount());
        return result;
    }


    /**
     * 统计用户登录
     *
     */
    @Transactional
    public void createStaUser() {
        Long nowTime = System.currentTimeMillis() / 1000;
        Long staTime = nowTime - nowTime % 3600;
        QueryWrapper<FcUserLog> wrapper = new QueryWrapper<>();
        long oneDay = 24 * 60 * 60;
        wrapper.select("DISTINCT address");
        wrapper.eq(FcUserLog.ACTION, "1").eq(FcUserLog.STATUS, true);
        StaUser staUser = new StaUser();
        wrapper.ge(FcUserLog.CREATE_TIME, staTime).lt(FcUserLog.CREATE_TIME, staTime + oneDay);
        staUser.setStaTime(staTime);
        Integer counts = baseService.counts(FcUserLog.class, wrapper);
        staUser.setLoginAmount(counts);
        if (staUser.getLoginAmount() > 0){
            baseService.save(staUser);
        }
    }
}
