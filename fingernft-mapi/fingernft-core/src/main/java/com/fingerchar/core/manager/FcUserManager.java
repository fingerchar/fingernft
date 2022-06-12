package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.AbstractDocument;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/21 18:54
 */
@Service
public class FcUserManager {
    @Autowired
    IBaseService baseService;

    public List<FcUser> listByMulti(List<String> addresList){
        if (addresList.size() == 0){
            return new ArrayList<FcUser>();
        }
        QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
        wrapper.in(FcUser.ADDRESS, addresList)
                .eq(FcUser.DELETED, false);
        return this.baseService.findByCondition(FcUser.class, wrapper);
    }

    public FcUser get(String address){

        QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUser.ADDRESS, address)
                .eq(FcUser.DELETED, false);
        return this.baseService.getByCondition(FcUser.class, wrapper);
    }
}
