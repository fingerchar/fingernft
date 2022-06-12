package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.NoticeType;
import com.fingerchar.core.manager.FcContractNftManager;
import com.fingerchar.core.manager.FcNftLikeManager;
import com.fingerchar.core.manager.FcNoticeManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcNftLikeExtMapper;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftLike;
import com.fingerchar.db.vo.NftParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FcNftLikeService {

	@Autowired
	FcNftLikeExtMapper nftLikeExtMapper;

	@Autowired
	FcNoticeManager noticeManager;

	@Autowired
	FcContractNftManager nftManager;

	@Autowired
	FcNftLikeManager likeManager;
	
	@Autowired
	IBaseService baseService;

	public List<String> list(String userAddress, List<Long> nftIds) {
		QueryWrapper<FcNftLike> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNftLike.USER_ADDRESS, userAddress)
			.in(FcNftLike.NFT_ID, nftIds)
			.eq(BaseEntity.DELETED, false);
		List<FcNftLike> likeList = this.baseService.findByCondition(FcNftLike.class, wrapper);
		List<String> Lists = new ArrayList<>(likeList.size());
		for (FcNftLike fcNftLike : likeList) {
			FcContractNft fcContractNft = this.baseService.getById(FcContractNft.class, fcNftLike.getNftId());
			String likeStr = "";
			if (fcContractNft != null) {
				likeStr = fcContractNft.getAddress() + ":" + fcContractNft.getTokenId().toString();
			}
			Lists.add(likeStr);
		}
		return Lists;
	}

	public List<String> listbymulti(String userAddress, List<NftParamVO> nftParamVOList){
		List<FcNftLike> likeList = this.likeManager.listbymulti(userAddress, nftParamVOList);
		List<String> keyList = likeList.stream().map(vo->vo.getAddress() + ":" + vo.getTokenId()).collect(Collectors.toList());
		return keyList;
	}

	public Object add(String userAddress, String address, String tokenId) {
		FcContractNft nft = this.nftManager.get(address, tokenId);
		if(null == nft){
			return ResponseUtil.fail(-1, "nft is not existed");
		}
		FcNftLike nftLike = this.likeManager.get(userAddress, address, tokenId);
		if(null != nftLike){
			return ResponseUtil.ok();
		}
		nftLike = new FcNftLike();
		nftLike.setAddress(address);
		nftLike.setTokenId(tokenId);
		nftLike.setUserAddress(userAddress);
		this.likeManager.save(nftLike);

		Map<String, Object> map = new HashMap<>();
		map.put("address", nft.getAddress());
		map.put("tokenId", nft.getTokenId());
		Integer type = CommonStatus.LIKED.getType();
		Integer noticeType = NoticeType.LIKE.getType();
		this.noticeManager.add(map, nft.getCreator(), type, noticeType, userAddress);

		return ResponseUtil.ok();
	}

	public Object delete(String userAddress, String address, String tokenId) {
		FcNftLike nftLike = this.likeManager.get(userAddress, address, tokenId);
		if(null == nftLike){
			return ResponseUtil.ok();
		}
		this.likeManager.delete(nftLike);
		return ResponseUtil.ok();
	}

}
