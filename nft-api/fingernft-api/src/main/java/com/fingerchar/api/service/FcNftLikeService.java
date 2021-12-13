package com.fingerchar.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.constant.CommonStatus;
import com.fingerchar.api.constant.NoticeType;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcNftLikeExtMapper;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftLike;
import com.fingerchar.db.domain.FcUser;

@Service
public class FcNftLikeService {

	@Autowired
	FcNftLikeExtMapper nftLikeExtMapper;

	@Autowired
	FcNoticeService noticeService;
	
	@Autowired
	IBaseService baseService;

	public List<String> list(String userAddress, List<Long> nftIds) {
		QueryWrapper<FcNftLike> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNftLike.USER_ADDRESS, userAddress)
			.in(FcNftLike.NFT_ID, nftIds)
			.eq(BaseEntity.DELETED, false);
		List<FcNftLike> likeList = this.baseService.findByCondition(FcNftLike.class, wrapper);
		if(likeList.isEmpty()) {
			return new ArrayList<>();
		}
		Set<Long> likeNftIds = likeList.stream().map(FcNftLike::getNftId).collect(Collectors.toSet());
		QueryWrapper<FcContractNft> nftWrapper = new QueryWrapper<>();
		nftWrapper.in(FcContractNft.ID, likeNftIds)
			.eq(FcContractNft.IS_SYNC, true);
		List<FcContractNft> nftList = this.baseService.findByCondition(FcContractNft.class, nftWrapper);
		if(nftList.isEmpty()) {
			return new ArrayList<>();
		}
		List<String> lists = new ArrayList<>(nftList.size());
		for (FcContractNft nft : nftList) {
			lists.add(nft.getAddress() + ":" + nft.getTokenId());
		}
		return lists;
	}

	@Transactional(rollbackFor = Exception.class)
	public void add(FcContractNft nft, FcUser user) {
		FcNftLike fcNftLike = new FcNftLike();
		fcNftLike.setNftId(nft.getId());
		fcNftLike.setUserAddress(user.getAddress());
		this.baseService.save(fcNftLike);
		Map<String, Object> map = new HashMap<>();
		map.put("address", nft.getAddress());
		map.put("tokenId", nft.getTokenId());
		map.put("likerAddr", user.getAddress());
		map.put("likerName", user.getNickname());
		String type = CommonStatus.getStatusByName("Liked").getType().toString();
		String noticeType = NoticeType.getNoticeTypeByName("Like").getType().toString();
		this.noticeService.insertNotice(map, nft.getCreator(), type, nft.getImgUrl(), nft.getName(), noticeType, user.getAddress());
	}

	public int delete(FcNftLike fcNftLike) {
		QueryWrapper<FcNftLike> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNftLike.USER_ADDRESS, fcNftLike.getUserAddress())
			.eq(FcNftLike.NFT_ID, fcNftLike.getNftId());
		return this.baseService.deleteByCondition(FcNftLike.class, wrapper);
	}


	public FcNftLike queryByUserIdNftId(Long nftId, String userAddress) {
		QueryWrapper<FcNftLike> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNftLike.USER_ADDRESS, userAddress)
			.eq(FcNftLike.NFT_ID, nftId)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.getByCondition(FcNftLike.class, wrapper);
	}


	public Object listByAddress(List<String> paramList) {
		Map<String, Boolean> map = new HashMap<>();
		if (null == paramList || paramList.isEmpty()) {
			return ResponseUtil.ok(map);
		}
		int len = paramList.size();
		String param = null;
		FcNftLike like = null;
		String[] temp = null;
		for (int i = 0; i < len; i++) {
			param = paramList.get(i);
			temp = param.split("@");
			like = this.nftLikeExtMapper.getLike(temp[0], temp[1]);
			if (null != like && null != like.getDeleted() && !like.getDeleted().booleanValue()) {
				map.put(param, true);
			} else {
				map.put(param, false);
			}
		}
		return ResponseUtil.ok(temp);
	}

}
