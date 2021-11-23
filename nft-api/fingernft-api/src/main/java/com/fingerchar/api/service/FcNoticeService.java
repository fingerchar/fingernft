package com.fingerchar.api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcNotice;
import com.fingerchar.db.domain.FcUser;


@Service
public class FcNoticeService {
	
	@Autowired
	IBaseService baseService;

	public IPage<FcNotice> findAll(Integer isRead, String address, IPage<FcNotice> pageInfo) {
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
		return this.baseService.findByPage(FcNotice.class, wrapper, pageInfo);
	}

	public int updateReadByIds(FcNotice fcNotice) {
		fcNotice.setIsRead(true);
		return this.baseService.update(fcNotice);
	}

	public Integer findCountUnRead(String address) {
		QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNotice.OWNER, address)
			.eq(FcNotice.IS_READ, false)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.counts(FcNotice.class, wrapper);
	}

	public int updateAll(String address) {
		UpdateWrapper<FcNotice> wrapper = new UpdateWrapper<>();
		wrapper.set(FcNotice.IS_READ, true);
		wrapper.eq(FcNotice.OWNER, address)
			.eq(FcNotice.IS_READ, false);
		return this.baseService.updateByCondition(FcNotice.class, wrapper);
	}

	public FcNotice findById(Long id) {
		return this.baseService.getById(FcNotice.class, id);
	}

	public void insertNotice(Map<String, Object> content, String owner, String type, String image, String name, String noticeType, String operator) {
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUser.ADDRESS, owner);
		FcUser user = this.baseService.getByCondition(FcUser.class, wrapper);
		//如果不是在本站用户，不进行通知
		if(null == user) {
			return;
		}
		FcNotice notice = new FcNotice();
		notice.setContent(JSON.toJSONString(content));
		notice.setOwner(owner);
		notice.setType(noticeType);
		notice.setSubType(type);
		notice.setImage(image);
		notice.setName(name);
		notice.setIsRead(false);
		notice.setUserId(user.getId());
		notice.setOperator(operator);
		this.baseService.save(notice);
	}

	/**
	 * @param address
	 * @return
	 */
	public Object getCount(String address) {
		QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNotice.OWNER, address)
			.eq(BaseEntity.DELETED, false);
		Integer totalCount = this.baseService.counts(FcNotice.class, wrapper);
		wrapper.eq(FcNotice.IS_READ, false);
		Integer unreadCount = this.baseService.counts(FcNotice.class, wrapper);
		Map<String, Integer> map = new HashMap<>();
		map.put("unreadCount", unreadCount);
		map.put("totalCount", totalCount);
		return ResponseUtil.ok(map);
	}
}
