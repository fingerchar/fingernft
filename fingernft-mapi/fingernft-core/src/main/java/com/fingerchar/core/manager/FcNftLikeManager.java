package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.dao.ext.FcNftLikeExtMapper;
import com.fingerchar.db.domain.FcNftLike;
import com.fingerchar.db.vo.NftParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * @Author： Zjm
 * @Date：2022/4/1 13:03
 */
@Service
public class FcNftLikeManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcNftLikeExtMapper likeExtMapper;

    public List<FcNftLike> listbymulti(String userAddress, List<NftParamVO> paramVOList){
        return this.likeExtMapper.listByMulti(userAddress, paramVOList);
    }

    public FcNftLike get(String userAddress, String address, String tokenId){
        QueryWrapper<FcNftLike> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftLike.USER_ADDRESS, userAddress)
                .eq(FcNftLike.ADDRESS, address)
                .eq(FcNftLike.TOKEN_ID, tokenId)
                .eq(FcNftLike.DELETED, false);
        return this.baseService.getByCondition(FcNftLike.class, wrapper);
    }

    public Integer delete(FcNftLike nftLike){
        return this.baseService.deleteById(FcNftLike.class, nftLike.getId());
    }

    public Integer save(FcNftLike nftLike){
        nftLike.setUserAddress(nftLike.getUserAddress().toLowerCase(Locale.ROOT));
        nftLike.setAddress(nftLike.getAddress().toLowerCase(Locale.ROOT));
        return this.baseService.save(nftLike);
    }
}
