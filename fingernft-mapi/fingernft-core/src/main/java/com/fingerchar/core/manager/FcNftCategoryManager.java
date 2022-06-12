package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcNftCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/21 19:18
 */
@Service
public class FcNftCategoryManager {
    @Autowired
    IBaseService baseService;

    public FcNftCategory getDefault(){
        QueryWrapper<FcNftCategory> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc(FcNftCategory.ORDER);
        wrapper.last("limit 1");
        List<FcNftCategory> categoryList = this.baseService.findByCondition(FcNftCategory.class, wrapper);
        if(categoryList.isEmpty()){
            return null;
        }
        return categoryList.get(0);
    }
}
