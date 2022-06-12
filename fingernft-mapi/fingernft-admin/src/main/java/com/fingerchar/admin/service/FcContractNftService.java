package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.admin.vo.AdminNftParamVo;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.manager.FcContractNftManager;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.vo.NftInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


@Service
public class FcContractNftService {

	@Autowired
    private IBaseService baseService;

    @Autowired
    FcContractNftManager contractNftManager;

    @Autowired
    private com.fingerchar.admin.service.FcContractService contractService;


    public FcContractNft get(String address, String tokenId){
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.TOKEN_ID, tokenId)
                .eq(FcContractNft.DELETED, false);
        return this.baseService.getByCondition(FcContractNft.class, wrapper);
    }

    public Integer save(FcContractNft nft){
        FcContract contract = this.contractService.get(nft.getAddress());
        if (null == contract) {
            contract = new FcContract();
            contract.setAddress(nft.getAddress());
            contract.setDeleted(false);
            contract.setIsAdmin(false);
            contract.setVerify(false);
            this.contractService.save(contract);
        }
        return this.baseService.save(nft);
    }

    /**
     *
     * @param fcContractNft
     * @param page
     * @param isASC
     * @param sortType
     * @return
     */
    public IPage<FcContractNft> querySelective(FcContractNft fcContractNft, IPage<FcContractNft> page,boolean isASC, String sortType) {
        QueryWrapper<FcContractNft>  wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(fcContractNft.getName())) {
            wrapper.like(FcContractNft.NAME,fcContractNft.getName());
        }
        if (!StringUtils.isEmpty(fcContractNft.getAddress())) {
            wrapper.eq(FcContractNft.ADDRESS,fcContractNft.getAddress());
        }
        if (!StringUtils.isEmpty(fcContractNft.getCreator())) {
            wrapper.eq(FcContractNft.CREATOR,fcContractNft.getCreator());
        }
        wrapper.eq(BaseEntity.DELETED,false);
        if (isASC && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC,sortType);
        }

        return baseService.findByPage(FcContractNft.class,wrapper,page);
    }

    /**
     *
     * @param fcContractNft
     * @param page
     * @param isASC
     * @param sortType
     * @return
     */
    public IPage<FcContractNft> querySelective(FcContractNft fcContractNft, IPage<FcContractNft> page,boolean isASC, String sortType, Boolean nftVerify) {
        QueryWrapper<FcContractNft>  wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(fcContractNft.getName())) {
            wrapper.like(FcContractNft.NAME,fcContractNft.getName());
        }
        if (!StringUtils.isEmpty(fcContractNft.getAddress())) {
            wrapper.eq(FcContractNft.ADDRESS,fcContractNft.getAddress());
        }
        if (!StringUtils.isEmpty(fcContractNft.getCreator())) {
            wrapper.eq(FcContractNft.CREATOR,fcContractNft.getCreator());
        }
        //验证是否已通过审核
        wrapper.eq(FcContractNft.NFT_VERIFY,nftVerify);

        wrapper.eq(BaseEntity.DELETED,false);
        if (isASC && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC,sortType);
        }

        return baseService.findByPage(FcContractNft.class,wrapper,page);
    }

    /**
     * 获取nft集合
     * @param nftParamVo nft查询vo
     * @param page 分页
     * @return
     */
    public IPage<NftInfoVo> queryList(AdminNftParamVo nftParamVo, IPage<FcContractNft> page) {
        QueryWrapper<FcContractNft>  wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(nftParamVo.getCategoryId())) {
            wrapper.eq(FcContractNft.CATEGORY_ID, nftParamVo.getCategoryId());
        }
        if (!StringUtils.isEmpty(nftParamVo.getCreator())) {
            wrapper.eq(FcContractNft.CREATOR, nftParamVo.getCreator());
        }
        if (!StringUtils.isEmpty(nftParamVo.getAddress())) {
            wrapper.eq(FcContractNft.ADDRESS, nftParamVo.getAddress());
        }
        if (!StringUtils.isEmpty(nftParamVo.getTokenId())) {
            wrapper.eq(FcContractNft.TOKEN_ID, nftParamVo.getTokenId());
        }
        if (null != nftParamVo.getNftVerify()) {
            //验证是否已通过审核
            wrapper.eq(FcContractNft.NFT_VERIFY, nftParamVo.getNftVerify());
        }

        wrapper.eq(FcContractNft.DELETED,false);
        wrapper.orderByDesc(FcContractNft.ID);
        IPage<FcContractNft> iPage = contractNftManager.page(page, wrapper);
        IPage<NftInfoVo> nftInfoVoIPage = this.contractNftManager.getNftInfoList(iPage);
        return nftInfoVoIPage;

    }

    /**
     * 根据ID更新Verify
     *
     * @param contract Verify记录
     * @return 更新成功返回true，否则返回false
     */
    public boolean verifyContract(FcContractNft contract,Integer verify) {
        contract.setNftVerify(verify);
        contract.setUpdateTime(System.currentTimeMillis()/1000);
        Boolean  ok = baseService.update(contract) > 0;
        if(ok){
            QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
            FcNftItems fcNftItem = baseService.getById(FcNftItems.class, contract.getId());
            if(null != fcNftItem){
                FcNftItems fcNftItems = new FcNftItems();
                fcNftItems.setUpdateTime(System.currentTimeMillis()/1000);
                return baseService.update(fcNftItems) > 0;
            }
        }
        return  ok;
    }

    /**
     * 根据ID禁用
     *
     * @param contract
     * @return 更新成功返回true，否则返回false
     */
    public boolean disableContract(FcContractNft contract) {
        contract.setDeleted(true);
        contract.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.update(contract) > 0;
    }


    /**
     * 根据ID启用
     *
     * @param contract
     * @return 更新成功返回true，否则返回false
     */
    public boolean enableContract(FcContractNft contract) {
        contract.setDeleted(false);
        contract.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.update(contract) > 0;
    }

    /**
     *findById
     * @param id
     * @return
     */
    public FcContractNft findById(Long id) {
        return baseService.getById(FcContractNft.class,id);
    }


    public FcContractNft getByAddressAndTokenId(String address, String tokenId) {
    	QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
    	wrapper.eq(FcContractNft.ADDRESS, address)
    		.eq(FcContractNft.TOKEN_ID, tokenId)
    		.eq(FcContractNft.IS_SYNC, true)
    		.eq(BaseEntity.DELETED, false);
    	return this.baseService.getByCondition(FcContractNft.class, wrapper);
    }

}
