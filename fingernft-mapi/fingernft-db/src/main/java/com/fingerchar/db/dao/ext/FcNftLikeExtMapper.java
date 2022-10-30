package com.fingerchar.db.dao.ext;

import com.fingerchar.db.domain.FcNftLike;
import com.fingerchar.db.vo.NftParamVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FcNftLikeExtMapper {

	@Select(value="select t1.* from fc_nft_like t1 left join fc_contract_nft t2 on t2.id = t1.nft_id where t2.address=#{address} and t2.token_id = #{tokenId}")
	FcNftLike getLike(@Param("address")String address, @Param("tokenId")String tokenId);

	public List<FcNftLike> listByMulti(@Param("userAddress") String userAddress, @Param("params") List<NftParamVO> params);
}
