package com.fingerchar.dao.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.domain.FcOrder;
import com.fingerchar.domain.FcOrderLog;
import com.fingerchar.dto.FcOrderDto;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FcOrderExtMapper {
    
	public List<FcOrderDto> listVo(FcOrder fcOrder);

    @SuppressWarnings("rawtypes")
    Integer countUser(@Param(Constants.WRAPPER) Wrapper ew);
    
    @SuppressWarnings("rawtypes")
    List<FcOrderLog> getAccumulatedMoney(@Param(Constants.WRAPPER) Wrapper ew);

    @SuppressWarnings("rawtypes")
    IPage<FcOrderLog> findByPage(IPage<FcOrderLog> page, @Param(Constants.WRAPPER) Wrapper ew);

    @SuppressWarnings("rawtypes")
    List<String> getAll(@Param(Constants.WRAPPER) Wrapper ew);


}
