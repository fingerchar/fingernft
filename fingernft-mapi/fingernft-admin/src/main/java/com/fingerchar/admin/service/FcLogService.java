package com.fingerchar.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcAdminLog;


@Service
public class FcLogService {
    @Autowired
    private IBaseService baseService;

    public void add(FcAdminLog log) {
        baseService.save(log);
    }

    public IPage<FcAdminLog> querySelective(String name, IPage<FcAdminLog> page, boolean isASC, String sortType) {
        QueryWrapper<FcAdminLog> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like(FcAdminLog.ADMIN,name );
        }
        wrapper.eq(BaseEntity.DELETED,false);
        if (isASC && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC , sortType);
        }
        return baseService.findByPage(FcAdminLog.class,wrapper,page);

    }
}
