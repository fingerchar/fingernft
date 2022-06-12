package com.fingerchar.db.dao.ext;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.fingerchar.db.domain.FcOrderLog;

public interface FcOrderLogExtMapper {

	@Update(value="update fc_order_log set expired = true where order_id = #{orderId}")
	void updateByOrderId(@Param("orderId")Long orderId);
	
	
	FcOrderLog getOne(@Param("address")String address, @Param("type")Integer type);
}
