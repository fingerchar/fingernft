package com.fingerchar.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.manager.FcContractManager;
import com.fingerchar.core.manager.FcContractNftManager;
import com.fingerchar.core.manager.FcNoticeManager;
import com.fingerchar.core.manager.FcUserManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNotice;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.dto.ExchangeBuyLog;
import com.fingerchar.db.dto.ExchangeCancelLog;
import com.fingerchar.db.dto.PrepareOrderInfo;
import com.fingerchar.db.dto.TransferLog;
import com.fingerchar.db.vo.UserBaseInfoVo;
import com.fingerchar.db.vo.notice.ContractVo;
import com.fingerchar.db.vo.notice.NftInfoVo;
import com.fingerchar.db.vo.notice.NoticeContentVo;
import com.fingerchar.db.vo.notice.NoticeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class FcNoticeService {
	
	@Autowired
	IBaseService baseService;

	@Autowired
	FcNoticeManager noticeManager;

	@Autowired
	FcUserManager userManager;

	@Autowired
	FcContractManager contractManager;

	@Autowired
	FcContractNftManager nftManager;

	public IPage<NoticeInfoVo> page(Integer isRead, String address, IPage<FcNotice> pageInfo) {
		QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNotice.OWNER, address)
			.eq(BaseEntity.DELETED, false);
		if(null != isRead) {
			if(isRead == 1) {
				wrapper.eq(FcNotice.IS_READ, true);
			} else {
				wrapper.eq(FcNotice.IS_READ, false);
			}
		}
		wrapper.orderByDesc(BaseEntity.CREATE_TIME);
		IPage<FcNotice> noticeIpage = this.baseService.findByPage(FcNotice.class, wrapper, pageInfo);
		List<FcNotice> noticeList = noticeIpage.getRecords();
		List<NoticeInfoVo> infoVoList = this.getNoticeInfoList(noticeList);
		IPage<NoticeInfoVo> infoVoIPage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
		infoVoIPage.setRecords(infoVoList);
		infoVoIPage.setPages(pageInfo.getPages());
		return infoVoIPage;
	}

	private List<NoticeInfoVo> getNoticeInfoList(List<FcNotice> noticeList) {
		List<String> operatorList = noticeList.stream().map(FcNotice::getOperator).collect(Collectors.toList());
		List<FcUser> userList = this.userManager.listByMulti(operatorList);
		Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
		List<NoticeInfoVo> infoVoList = new ArrayList<>();
		NoticeInfoVo infoVo = null;
		NoticeContentVo contentVo = null;
		FcUser operator = null;
		for (FcNotice notice: noticeList){
			contentVo = this.getContentVo(notice);
			operator = userMap.get(notice.getOperator());
			if(null == operator){
				infoVo = new NoticeInfoVo(notice, new UserBaseInfoVo(notice.getOperator()), contentVo);
			}else{
				infoVo = new NoticeInfoVo(notice, new UserBaseInfoVo(operator), contentVo);
			}
			infoVoList.add(infoVo);
		}
		return infoVoList;
	}

	private NoticeContentVo getContentVo(FcNotice notice) {
		CommonStatus status = CommonStatus.getStatusByType(notice.getSubType());
		String content = notice.getContent();
		switch (status){
			case LIKED:
				return this.likeNoticeInfo(content);
			case FOLLOWED:
				return this.followNoticeInfo(content);
			case CANCEL_SALE:
			case CANCEL_BID:
				return this.cancelNoticeInfo(content, notice.getSubType());
			case BID:
				return this.bidNoticeInfo(content);
			case BUY:
			case ACCEPT_BID:
				return this.buyNoticeInfo(content, notice.getSubType());
			case TRANSFER:
			case RECEIVE:
				return this.transferNoticeInfo(content);
		}
		return null;
	}

	public NoticeContentVo likeNoticeInfo(String content){
		NoticeContentVo contentVo = new NoticeContentVo();
		NftInfoVo infoVo = JSON.parseObject(content, NftInfoVo.class);
		FcContractNft nft = this.nftManager.get(infoVo.getAddress(), infoVo.getTokenId());
		if(null != nft){
			infoVo = new NftInfoVo(nft);
		}
		contentVo.setNft(infoVo);
		return contentVo;
	}

	public NoticeContentVo followNoticeInfo(String content){
		NoticeContentVo contentVo = new NoticeContentVo();
		UserBaseInfoVo infoVo = JSON.parseObject(content, UserBaseInfoVo.class);
		FcUser user = this.userManager.get(infoVo.getAddress());
		if(null != user){
			infoVo = new UserBaseInfoVo(user);
		}
		contentVo.setUser(infoVo);
		return contentVo;
	}

	public NoticeContentVo cancelNoticeInfo(String content, Integer type){
		ExchangeCancelLog cancelLog = JSON.parseObject(content, ExchangeCancelLog.class);
		NftInfoVo infoVo = new NftInfoVo();
		FcContractNft nft = null;
		if(CommonStatus.CANCEL_SALE.getType().equals(type)){
			nft = this.nftManager.get(cancelLog.getSellToken(), cancelLog.getSellTokenId().toString());
			infoVo.setAddress(cancelLog.getSellToken());
			infoVo.setTokenId(cancelLog.getSellTokenId().toString());
		}else{
			nft = this.nftManager.get(cancelLog.getBuyToken(), cancelLog.getBuyTokenId().toString());
			infoVo.setAddress(cancelLog.getBuyToken());
			infoVo.setTokenId(cancelLog.getBuyTokenId().toString());
		}
		if(null != nft){
			infoVo = new NftInfoVo(nft);
		}
		NoticeContentVo contentVo = new NoticeContentVo();
		contentVo.setNft(infoVo);
		contentVo.setTxHash(cancelLog.getTxHash());
		return contentVo;
	}

	public NoticeContentVo buyNoticeInfo(String content, Integer type){
		ExchangeBuyLog buyLog = JSON.parseObject(content, ExchangeBuyLog.class);
		NftInfoVo infoVo = new NftInfoVo();
		FcContractNft nft = null;
		if(CommonStatus.BUY.getType().equals(type)){
			nft = this.nftManager.get(buyLog.getSellToken(), buyLog.getSellTokenId().toString());
			infoVo.setAddress(buyLog.getSellToken());
			infoVo.setTokenId(buyLog.getSellTokenId().toString());
		}else{
			nft = this.nftManager.get(buyLog.getBuyToken(), buyLog.getBuyTokenId().toString());
			infoVo.setAddress(buyLog.getBuyToken());
			infoVo.setTokenId(buyLog.getBuyTokenId().toString());
		}
		if(null != nft){
			infoVo = new NftInfoVo(nft);
		}
		NoticeContentVo contentVo = new NoticeContentVo();
		contentVo.setNft(infoVo);
		contentVo.setTxHash(buyLog.getTxHash());
		return contentVo;
	}

	public NoticeContentVo bidNoticeInfo(String content){
		PrepareOrderInfo orderInfo = JSON.parseObject(content, PrepareOrderInfo.class);
		NftInfoVo infoVo = new NftInfoVo();
		FcContractNft nft = null;
		nft = this.nftManager.get(orderInfo.getBuyToken(), orderInfo.getBuyTokenId());
		if(null != nft){
			infoVo = new NftInfoVo(nft);
		}else{
			infoVo.setAddress(orderInfo.getBuyToken());
			infoVo.setTokenId(orderInfo.getBuyTokenId());
		}
		NoticeContentVo contentVo = new NoticeContentVo();
		contentVo.setNft(infoVo);
		return contentVo;
	}

	public NoticeContentVo transferNoticeInfo(String content){
		TransferLog log = JSON.parseObject(content, TransferLog.class);
		NftInfoVo infoVo = new NftInfoVo();
		FcContractNft nft = null;
		nft = this.nftManager.get(log.getAddress(), log.getTokenId().toString());
		if(null != nft){
			infoVo = new NftInfoVo(nft);
		}else{
			infoVo.setAddress(log.getAddress());
			infoVo.setTokenId(log.getTokenId().toString());
		}
		NoticeContentVo contentVo = new NoticeContentVo();
		contentVo.setNft(infoVo);
		contentVo.setTxHash(log.getTxHash());
		return contentVo;
	}



	public Integer read(Long id) {
		FcNotice notice = this.noticeManager.get(id);
		if(null == notice){
			return 0;
		}
		notice.setIsRead(true);
		return this.noticeManager.update(notice);
	}

	public Integer readAll(String address){
		return this.noticeManager.readAll(address);
	}

	public Integer findCountUnRead(String address) {
		return this.noticeManager.countUnread(address);
	}

	/**
	 * @param address
	 * @return
	 */
	public Object getCount(String address) {
		Integer total = this.noticeManager.countTotal(address);
		Integer unread = this.noticeManager.countUnread(address);

		Map<String, Integer> map = new HashMap<>();
		map.put("unreadCount", unread);
		map.put("totalCount", total);
		return ResponseUtil.ok(map);
	}
}
