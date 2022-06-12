package com.fingerchar.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcUserToken;

/**
 * @Author： Zjm
 * @Date：2021/10/27 18:15
 */

@Service
public class FcUserTokenService {

    @Autowired
    IBaseService baseService;

    public FcUserToken getUserToken(String address){
        QueryWrapper<FcUserToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserToken.USER_ADDRESS, address).eq(FcUserToken.DELETED, false);
        return this.baseService.getByCondition(FcUserToken.class, wrapper);
    }

    public String getToken(String address) {
        QueryWrapper<FcUserToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserToken.USER_ADDRESS, address).eq(FcUserToken.DELETED, false);
        FcUserToken userToken = this.baseService.getByCondition(FcUserToken.class, wrapper);
        if (null == userToken){
            return null;
        }
        return userToken.getUserToken();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void saveOrUpdate(String address, String token) {
        FcUserToken userToken = new FcUserToken();
        userToken.setUserAddress(address);
        userToken.setUserToken(token);
        String sqlToken = getToken(address);
        if (StringUtils.isEmpty(sqlToken)){
            this.baseService.save(userToken);
        }else {
            UpdateWrapper<FcUserToken> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(FcUserToken.USER_ADDRESS, address).eq(FcUserToken.DELETED, false);
            updateWrapper.set(FcUserToken.USER_TOKEN, token);
            this.baseService.updateByCondition(FcUserToken.class, updateWrapper);
        }
    }
}
