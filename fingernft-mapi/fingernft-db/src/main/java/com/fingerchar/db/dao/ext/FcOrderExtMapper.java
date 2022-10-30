package com.fingerchar.db.dao.ext;

import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.vo.NftParamVO;

import java.util.List;

/**
 * @author: Black_Dragon
 * @date: 2021/6/3
 */
public interface FcOrderExtMapper {
	
    public List<FcOrder> salelistbymulti(@Param("params") List<NftParamVO> params);

    public List<FcOrder> bidlistbymulti(@Param("params") List<NftParamVO> params);

}
