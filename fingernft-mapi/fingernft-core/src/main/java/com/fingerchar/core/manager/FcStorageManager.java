package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author： Zjm
 * @Date：2022/3/27 16:32
 */
@Service
public class FcStorageManager {
    @Autowired
    IBaseService baseService;

    public void add(FcStorage storageInfo) {
        this.baseService.save(storageInfo);
    }

    public FcStorage findByKey(String key) {
        QueryWrapper<FcStorage> wrapper = new QueryWrapper<>();
        wrapper.eq(FcStorage.KEY, key);
        return this.baseService.getByCondition(FcStorage.class, wrapper);
    }

    public int update(FcStorage storageInfo) {
        return this.baseService.update(storageInfo);
    }

    public FcStorage get(Long id){
        return this.baseService.getById(FcStorage.class, id);
    }

    public FcStorage findById(Long id) {
        return this.baseService.getById(FcStorage.class, id);
    }

}
