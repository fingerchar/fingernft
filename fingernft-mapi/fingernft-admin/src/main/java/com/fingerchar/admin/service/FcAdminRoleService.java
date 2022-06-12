package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcAdminRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FcAdminRoleService {

    @Autowired
    private IBaseService baseService;

    public Set<String> queryByIds(Collection<Long> roleIds) {
        Set<String> roles = new HashSet<String>();
        if(null == roleIds || roleIds.isEmpty()){
            return roles;
        }
        QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
        wrapper.eq(BaseEntity.DELETED, false)
                .eq(FcAdminRole.ENABLED, true)
                .in(BaseEntity.ID, roleIds);
        List<FcAdminRole> roleList = baseService.findByCondition(FcAdminRole.class, wrapper);

        for(FcAdminRole role : roleList){
            roles.add(role.getName());
        }

        return roles;

    }

    public IPage<FcAdminRole> querySelective(String name, IPage<FcAdminRole> page, boolean isASC, String orderType) {
        QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like(FcAdminRole.NAME,name);
        }
        wrapper.eq(BaseEntity.DELETED,false);
        if (isASC && !StringUtils.isEmpty(orderType)) {
            wrapper.orderBy(true,isASC, orderType);
        }
        return baseService.findByPage(FcAdminRole.class,wrapper,page);
    }

    public FcAdminRole findById(Long id)
    {
        /*return roleMapper.selectByPrimaryKey(id);*/
        return baseService.getById(FcAdminRole.class,id);
    }

    public void add(FcAdminRole role) {
        role.setCreateTime(System.currentTimeMillis()/1000);
        role.setUpdateTime(System.currentTimeMillis()/1000);
        /*roleMapper.insertSelective(role);*/
        baseService.save(role);
    }

    public void deleteById(Long id) {

        /*roleMapper.logicalDeleteByPrimaryKey(id);*/
        baseService.deleteById(FcAdminRole.class,id);
    }

    public void updateById(FcAdminRole role) {
        role.setUpdateTime(System.currentTimeMillis()/1000);
        /*roleMapper.updateByPrimaryKeySelective(role);*/
        baseService.update(role);
    }

    public boolean checkExist(String name) {
      /*  FcAdminRoleExample example = new FcAdminRoleExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return roleMapper.countByExample(example) != 0;*/
      QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
      wrapper.eq(FcAdminRole.NAME,name)
              .eq(BaseEntity.DELETED,false);
      return baseService.counts(FcAdminRole.class,wrapper) != 0;
    }

    public List<FcAdminRole> queryAll() {
        /*
        FcAdminRoleExample example = new FcAdminRoleExample();
        example.or().andDeletedEqualTo(false);
        return roleMapper.selectByExample(example);*/
        QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
        wrapper.or().eq(BaseEntity.DELETED,false);
        return baseService.findByCondition(FcAdminRole.class,wrapper);
    }
}
