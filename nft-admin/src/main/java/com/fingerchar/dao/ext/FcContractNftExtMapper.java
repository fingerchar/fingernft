package com.fingerchar.dao.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.domain.FcContractNft;
import com.fingerchar.vo.FcContractNftVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FcContractNftExtMapper {

    @SuppressWarnings("rawtypes")
	IPage<FcContractNftVo> getList(IPage<FcContractNftVo> page, @Param(Constants.WRAPPER) Wrapper ew);

    @SuppressWarnings("rawtypes")
    List<String> getAllAddress(@Param(Constants.WRAPPER) Wrapper ew);

    @SuppressWarnings("rawtypes")
    List<FcContractNft> getByAddress(@Param(Constants.WRAPPER) Wrapper ew);
}
