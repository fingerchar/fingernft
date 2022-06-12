package com.fingerchar.db.dao.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.db.vo.FcUserVo;
import org.apache.ibatis.annotations.Param;

/*
* 扩展userMapper
* */
public interface FcUserExtMapper {
	
    @SuppressWarnings("rawtypes")
	IPage<FcUserVo> getList2(IPage<FcUserVo> page, @Param(Constants.WRAPPER) Wrapper ew);
    
}
