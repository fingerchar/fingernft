package com.fingerchar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcNftCategory;

@Service
public class FcCategoryService {

	@Autowired
    private IBaseService baseService;

    /**
     *
     * @param name
     * @param page
     * @param sort
     * @param order
     * @return
     */
    public IPage<FcNftCategory> querySelective(String name,IPage<FcNftCategory> page , String sort, String order) {
        QueryWrapper<FcNftCategory> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
           wrapper.like(FcNftCategory.NAME,name);
        }
        wrapper.eq(BaseEntity.DELETED,false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            wrapper.orderByDesc(sort);
        }

        return baseService.findByPage(FcNftCategory.class,wrapper,page);
    }

    /**
     * add
     * @param category
     */
    public void add(FcNftCategory category) {
        baseService.save(category);
    }

    /**
     * 获取所有的非逻辑删除状态的记录
     *
     * @return 分类列表
     */
    public IPage<FcNftCategory> queryAllCategory(IPage<FcNftCategory> page) {
        QueryWrapper<FcNftCategory> wrapper = new QueryWrapper<>();
        return baseService.findByPage(FcNftCategory.class,wrapper,page);
    }

    /**
     * 更新
     * @param category
     * @return
     */
    public int updateById(FcNftCategory category) {
        category.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.update(category);
    }

    /**
     * 反转状态
     * @param category
     * @return
     */
    public int deleteById(FcNftCategory category) {
        Boolean deleted = category.getDeleted();
        if (null == deleted){
            deleted = true;
        }
        category.setDeleted(!deleted);
        category.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.update(category);
    }

    public  FcNftCategory findById(Long id){
        return baseService.getById(FcNftCategory.class,id);
    }

    public Map<Long,String> idAndNameMap() {
        Map<Long,String> map = new HashMap<>();
        QueryWrapper<FcNftCategory> wrapper = new QueryWrapper<>();
        wrapper.eq(BaseEntity.DELETED,false);
        List<FcNftCategory> nftCategories = baseService.findByCondition(FcNftCategory.class, wrapper);
        for (FcNftCategory nftCategory : nftCategories) {
            map.put(nftCategory.getId(),nftCategory.getName());
        }
        return map;
    }

}
