package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcUserFollow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * @Author： Zjm
 * @Date：2022/4/1 11:34
 */
@Service
public class FcUserFollowManager {
    @Autowired
    IBaseService baseService;


    public IPage<FcUserFollow> pageFollow(String address, IPage<FcUserFollow> pageInfo){
        QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserFollow.USER_ADDRESS, address)
                .eq(FcUserFollow.DELETED, false);
        return this.baseService.findByPage(FcUserFollow.class, wrapper, pageInfo);
    }

    public IPage<FcUserFollow> pageFollower(String address, IPage<FcUserFollow> pageInfo){
        QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserFollow.FOLLOWING_ADDRESS, address)
                .eq(FcUserFollow.DELETED, false);
        return this.baseService.findByPage(FcUserFollow.class,wrapper, pageInfo);
    }

    public Integer countFollow(String address){
        QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserFollow.USER_ADDRESS, address)
                .eq(FcUserFollow.DELETED, false);
        return this.baseService.counts(FcUserFollow.class, wrapper);
    }

    public Integer countFollower(String address){
        QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserFollow.FOLLOWING_ADDRESS, address)
                .eq(FcUserFollow.DELETED, false);
        return this.baseService.counts(FcUserFollow.class, wrapper);
    }

    public List<FcUserFollow> listbymulti(String userAddress, List<String> addressList){
        QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserFollow.USER_ADDRESS, userAddress)
                .in(FcUserFollow.FOLLOWING_ADDRESS, addressList)
                .eq(FcUserFollow.DELETED, false);
        return this.baseService.findByCondition(FcUserFollow.class, wrapper);
    }

    public FcUserFollow get(String userAddress, String followingAddress){
        QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserFollow.USER_ADDRESS, userAddress)
                .eq(FcUserFollow.FOLLOWING_ADDRESS, followingAddress)
                .eq(FcUserFollow.DELETED, false);
        return this.baseService.getByCondition(FcUserFollow.class, wrapper);
    }

    public Integer delete(FcUserFollow userFollow){
        return this.baseService.deleteById(FcUserFollow.class, userFollow.getId());
    }


    public Integer save(FcUserFollow userFollow){
        userFollow.setUserAddress(userFollow.getUserAddress().toLowerCase(Locale.ROOT));
        userFollow.setFollowingAddress(userFollow.getFollowingAddress().toLowerCase(Locale.ROOT));
        return this.baseService.save(userFollow);
    }
}
