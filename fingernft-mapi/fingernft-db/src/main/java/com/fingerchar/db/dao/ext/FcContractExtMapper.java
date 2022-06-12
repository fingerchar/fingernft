package com.fingerchar.db.dao.ext;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface FcContractExtMapper {

	@Select(value = "select last_token_id from fc_contract  where address = #{address} for update")
	String getContractWithLock(@Param("address") String address);

	@Update(value = "update fc_contract t set t.last_token_id = #{lastTokenId} where t.address = #{address}")
	Integer updateLastTokenId(@Param("lastTokenId") String lastTokenId, @Param("address") String address);
	
}
