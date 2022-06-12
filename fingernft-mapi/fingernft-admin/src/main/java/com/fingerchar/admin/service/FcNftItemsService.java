package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.impl.BaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcNftItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FcNftItemsService {

	@Autowired
    private BaseService baseService;


    public FcNftItems get(String address, String tokenId, String owner){
        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.ADDRESS, address)
                .eq(FcNftItems.TOKEN_ID, tokenId)
                .eq(FcNftItems.ITEM_OWNER, owner)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcNftItems.class, wrapper);
    }

    public Integer update(FcNftItems nftItems){
        return this.baseService.update(nftItems);
    }



}
