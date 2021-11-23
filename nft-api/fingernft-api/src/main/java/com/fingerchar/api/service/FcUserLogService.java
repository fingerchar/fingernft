package com.fingerchar.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcUserLog;

/*
 *
 * @author zjm
 * */
@Service
public class FcUserLogService {
    @Autowired
    private IBaseService baseService;

    /**
     * 新增用户日志
     * @param userLog 用户日志
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(FcUserLog userLog){
        long time = System.currentTimeMillis() / 1000;
        userLog.setCreateTime(time);
        userLog.setUpdateTime(time);
        userLog.setDeleted(false);
        baseService.save(userLog);
    }
}
