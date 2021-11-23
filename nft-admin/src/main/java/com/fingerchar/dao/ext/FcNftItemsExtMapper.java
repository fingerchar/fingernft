package com.fingerchar.dao.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.domain.FcNftItems;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface FcNftItemsExtMapper {
	@SuppressWarnings("rawtypes")
    @MapKey("nftId")
    Map<Long, FcNftItems> getIdAndItemsMap(@Param(Constants.WRAPPER) Wrapper ew);
}
