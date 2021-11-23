package com.fingerchar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.dao.ext.FcContractNftExtMapper;
import com.fingerchar.domain.FcContractNft;
import com.fingerchar.domain.FcNftItems;
import com.fingerchar.vo.FcContractNftVo;


@Service
public class FcContractNftService {

	@Autowired
    private IBaseService baseService;

    @Autowired
    private FcContractNftExtMapper contractNftExtMapper;
    
    @Autowired
    private FcCategoryService categoryService;


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
        if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
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
        if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC,sortType);
        }

        return baseService.findByPage(FcContractNft.class,wrapper,page);
    }

    /**
     * 获取nft集合
     * @param categoryId 分类id
     * @param creator    作者
     * @param page       分页
     * @param isASC      是否升序
     * @param sortType   排列的字段
     * @param nftVerify  nft认证状态
     * @return
     */
    public IPage<FcContractNftVo> queryList(String categoryId, String creator, String address, String tokenId, IPage<FcContractNftVo> page, boolean isASC, String sortType, Integer nftVerify) {
        QueryWrapper<FcContractNft>  wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(categoryId)) {
            wrapper.eq("nft.category_id",categoryId);
        }
        if (!StringUtils.isEmpty(creator)) {
            wrapper.eq("nft.creator",creator);
        }
        if (!StringUtils.isEmpty(address)) {
            wrapper.eq("nft.address",address);
        }
        if (!StringUtils.isEmpty(tokenId)) {
            wrapper.eq("nft.token_id",tokenId);
        }
        if (nftVerify != null) {
            //验证是否已通过审核
            wrapper.eq("nft.nft_verify",nftVerify);
        }
        wrapper.eq("nft.deleted",false);
        if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC,sortType);
        }
        IPage<FcContractNftVo> iPage = contractNftExtMapper.getList(page, wrapper);
        List<FcContractNftVo> list = iPage.getRecords();
        //将页面展示的id取出
        List<Long> ids = list.stream().map(vo->vo.getId()).collect(Collectors.toList());
        if(ids.isEmpty()) {
        	return page;
        }
        QueryWrapper<FcNftItems> itemsWrapper = new QueryWrapper<>();
        itemsWrapper.eq(FcNftItems.DELETED, false).in(FcNftItems.NFT_ID, ids);
        List<FcNftItems> itemsList = this.baseService.findByCondition(FcNftItems.class, itemsWrapper);
        Map<Long, List<FcNftItems>> itemsMap = new HashMap<>();
        itemsList.stream().forEach(item-> {
        	if(null == itemsMap.get(item.getNftId())) {
        		itemsMap.put(item.getNftId(), new ArrayList<>());
        	}
            itemsMap.get(item.getNftId()).add(item);
        });
        Map<Long, String> categoryMap = categoryService.idAndNameMap();
        List<FcNftItems> tempList;
        FcNftItems item;
        int itemLen = 0;
        for (FcContractNftVo fcContractNftVo : list) {
        	tempList = itemsMap.get(fcContractNftVo.getId());
        	itemLen = tempList.size();
        	for(int i=0; i<itemLen; i++) {
        		item = tempList.get(i);
                if (null != item){
                    fcContractNftVo.setPrice(item.getPrice());
                    if(null != item.getOnsell() && item.getOnsell()) {
                        if(null == fcContractNftVo.getSellQuantity()) {
                            fcContractNftVo.setSellQuantity(item.getSellQuantity());
                        } else {
                            fcContractNftVo.setSellQuantity(fcContractNftVo.getSellQuantity() + item.getSellQuantity());
                        }
                    }
                }

        	}
            if (null != fcContractNftVo.getCategoryId()) {
                fcContractNftVo.setCategoryName(categoryMap.get(fcContractNftVo.getCategoryId()));
            }
        }
        iPage.setRecords(list);
        return iPage;
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
            wrapper.eq(FcNftItems.NFT_ID,contract.getId());
            FcNftItems fcNftItem = baseService.getById(FcNftItems.class, contract.getId());
            if(!StringUtils.isEmpty(fcNftItem)){
                FcNftItems fcNftItems = new FcNftItems();
                fcNftItems.setNftId(contract.getId());
                fcNftItems.setUpdateTime(System.currentTimeMillis()/1000);
                fcNftItems.setNftId(fcNftItem.getNftId());
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
     * 根据ID启用
     *
     * @param contract
     * @return 更新成功返回true，否则返回false
     */
    public boolean editVerify(FcContractNft contract,Integer verify) {
        contract.setNftVerify(verify);
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

	/**
	 * 
	 */
	public void setNftVerify() {
		UpdateWrapper<FcContractNft> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcContractNft.NFT_VERIFY, false);
		wrapper.set(FcContractNft.NFT_VERIFY, true);
		this.baseService.updateByCondition(FcContractNft.class, wrapper);
	}

    /**
     * 查询所有的类别
     *
     * */
    public List<String> getAllToken(Long staTime){
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.ge(FcContractNft.UPDATE_TIME,staTime).lt(FcContractNft.UPDATE_TIME,staTime + 24 * 60 * 60);
        return contractNftExtMapper.getAllAddress(wrapper);
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
