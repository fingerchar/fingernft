package com.fingerchar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.base.service.impl.BaseService;
import com.fingerchar.domain.FcNftItems;

@Service
public class FcNftItemsService {

	@Autowired
    private BaseService baseService;


    /**
     * 根据nft的id查找
     * @param nftId
     * @return
     */
    public List<FcNftItems> getByNftId(Long nftId){
        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.NFT_ID,nftId);
        wrapper.eq(FcNftItems.DELETED,false);
        return baseService.findByCondition(FcNftItems.class, wrapper);
    }



}
