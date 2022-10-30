package com.fingerchar.db.dao.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.vo.NftItemsVo;
import com.fingerchar.db.vo.NftParamVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FcNftItemsExtMapper {
	
    @SuppressWarnings("rawtypes")
	@MapKey("nftId")
    Map<Long, FcNftItems> getIdAndItemsMap(@Param(Constants.WRAPPER) Wrapper ew);

    public IPage<NftItemsVo> list(@Param("owner")String owner, @Param("tokenList")List<String> tokenList, @Param("token")String token, @Param("tokenId")String tokenId, IPage<NftItemsVo> page);

    Long count(@Param("owner")String owner, @Param("token")String token, @Param("tokenId")String tokenId);

    NftItemsVo get(@Param("owner")String owner, @Param("token")String token, @Param("tokenId")String tokenId);

    public List<FcNftItems> listByMulti(@Param("params") List<NftParamVO> params);

    Integer countByCondition(@Param("time")Long time, @Param("onsell")int onsell);
}
