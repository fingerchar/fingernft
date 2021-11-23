package com.fingerchar.dao.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.vo.FcUserVo;

import org.apache.ibatis.annotations.Param;

public interface FcUserExtMapper {
	
	@SuppressWarnings("rawtypes")
    IPage<FcUserVo> getList(IPage<FcUserVo> page, @Param(Constants.WRAPPER) Wrapper ew);

	@SuppressWarnings("rawtypes")
    IPage<FcUserVo> getList2(IPage<FcUserVo> page, @Param(Constants.WRAPPER) Wrapper ew);
}
