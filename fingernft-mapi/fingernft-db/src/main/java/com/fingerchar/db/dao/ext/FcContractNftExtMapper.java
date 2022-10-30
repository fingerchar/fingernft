package com.fingerchar.db.dao.ext;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftLike;
import com.fingerchar.db.dto.SearchNftParamDto;
import com.fingerchar.db.vo.FcContractNftVo;
import com.fingerchar.db.vo.FcContractNftWithBidVO;
import com.fingerchar.db.vo.NftParamVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface FcContractNftExtMapper {

    public void setNftVerify();
    
    public IPage<FcContractNft> findVerifyNft(IPage<FcContractNft> page, @Param("address")String address,
			@Param("categoryId")Long categoryId,
			@Param("contracts")List<String> contracts,
			@Param("payToken")String payToken,
			@Param("minPrice")BigDecimal minPrice,
			@Param("maxPrice")BigDecimal maxPrice,
			@Param("search")String search,
			@Param("sort")String sort,
			@Param("order")String order,
			@Param("isSell")Boolean isSell,
			@Param("onsellType")Integer onsellType
	);
    
    public IPage<FcContractNft> findContractNft(IPage<FcContractNft> page, @Param("address")String address, @Param("isSell")Boolean isSell);
	
	public IPage<FcContractNft> findOnsaleNft(IPage<FcContractNft> page, @Param("owner")String owner);
	
	public IPage<FcContractNft> findCollectionNft(IPage<FcContractNft> page, @Param("params")SearchNftParamDto params);

	public List<FcContractNft> nftlist(@Param("params")SearchNftParamDto params);

	public IPage<FcContractNft> searchNft(IPage<FcContractNft> page, @Param("name")String name);

	public IPage<FcContractNft> findCreatorNft(IPage<FcContractNft> page, @Param("creator")String creator);
	
	public Integer countCreatorNft(@Param("creator")String creator);

	public IPage<FcContractNft> findLikeNft(IPage<FcContractNft> page, @Param("userAddress")String userAddress);

	public List<FcContractNft> listByMulti(@Param("params") List<NftParamVO> params);
	/**
	 * @param address
	 * @return
	 */
	public Integer countContractOnsale(@Param("address")String address);

	public IPage<FcContractNft> findOnAuctionNft(IPage<FcContractNft> page);
	
	 @SuppressWarnings("rawtypes")
	IPage<FcContractNftVo> getList(IPage<FcContractNftVo> page, @Param(Constants.WRAPPER) Wrapper ew);

    //获取所有合约分类
    @SuppressWarnings("rawtypes")
	List<String> getAllAddress(@Param(Constants.WRAPPER) Wrapper ew);

    @SuppressWarnings("rawtypes")
	List<FcContractNft> getByAddress(@Param(Constants.WRAPPER) Wrapper ew);
    
    /**
	 * @param params
	 * @param pageInfo
	 * @return
	 */
	public IPage<FcContractNft> listByTokenAndTokenId(@Param("params") Set<NftParamVO> params, IPage<FcContractNft> pageInfo);

	public List<FcContractNft> listByTokenAndTokenId(@Param("params") Set<NftParamVO> params);

	IPage<FcContractNftWithBidVO> findNftWithBidNum(@Param("nftName") String name, @Param("categoryId") Long categoryId, IPage<FcContractNftWithBidVO> page);

	Integer countContractLike(@Param("params") List<FcNftLike> params);

	/**
	 *
	 * @param time 最小创建时间
	 * @param type nft类型
	 * @return
	 */
	Integer countByCondition(@Param("time")Long time, @Param("type")Integer type);
}
