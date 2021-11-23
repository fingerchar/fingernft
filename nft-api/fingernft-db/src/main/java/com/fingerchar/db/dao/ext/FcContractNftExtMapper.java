package com.fingerchar.db.dao.ext;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.db.domain.FcContractNft;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FcContractNftExtMapper {

    public void setNftVerify();
    
    public IPage<FcContractNft> findVerifyNft(IPage<FcContractNft> page, @Param("address")String address,
			@Param("categoryId")Long categoryId,
			@Param("contracts")List<String> contracts,
			@Param("sort")String sort,
			@Param("order")String order,
			@Param("isSell")Boolean isSell);
    
    public IPage<FcContractNft> findContractNft(IPage<FcContractNft> page, @Param("address")String address, @Param("isSell")Boolean isSell);
	
	public IPage<FcContractNft> findOnsaleNft(IPage<FcContractNft> page, @Param("owner")String owner);
	
	public IPage<FcContractNft> findCollectionNft(IPage<FcContractNft> page, @Param("owner")String owner);
	
	public IPage<FcContractNft> searchNft(IPage<FcContractNft> page, @Param("name")String name);

	public IPage<FcContractNft> findCreatorNft(IPage<FcContractNft> page, @Param("creator")String creator);
	
	public Integer countCreatorNft(@Param("creator")String creator);

	public IPage<FcContractNft> findLikeNft(IPage<FcContractNft> page, @Param("userAddress")String userAddress);

	/**
	 * @param address
	 * @return
	 */
	public Integer countContractOnsale(@Param("address")String address);
}
