package com.fingerchar.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcNotice;
import com.fingerchar.domain.FcUser;
import com.fingerchar.utils.ResponseUtil;

@Service
public class FcNoticeService {

	@Autowired
	private IBaseService baseService;

	public IPage<FcNotice> findAll(IPage<FcNotice> page, Integer isRead, String address) {
		QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
		if (null == isRead) {
			wrapper.eq(FcNotice.OWNER, address).eq(BaseEntity.DELETED, false);
		} else {
			if (isRead == 1) {
				wrapper.eq(FcNotice.OWNER, address).eq(FcNotice.IS_READ, true).eq(BaseEntity.DELETED, false);
			} else {
				wrapper.eq(FcNotice.OWNER, address).eq(FcNotice.IS_READ, false).eq(BaseEntity.DELETED, false);
			}
		}
		return baseService.findByPage(FcNotice.class, wrapper, page);
	}

	public int updateReadByIds(FcNotice fcNotice) {
		fcNotice.setIsRead(true);
		return baseService.update(fcNotice);
	}

	public Integer findCountUnRead(String address) {
		QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNotice.OWNER, address).eq(FcNotice.IS_READ, false).eq(BaseEntity.DELETED, false);
		return baseService.counts(FcNotice.class, wrapper);
	}

	public int updateAll(String address) {
		FcNotice fcNotice = new FcNotice();
		fcNotice.setIsRead(true);
		UpdateWrapper<FcNotice> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcNotice.OWNER, address).eq(FcNotice.IS_READ, false);
		return baseService.updateByCondition(FcNotice.class, fcNotice, wrapper);
	}

	public FcNotice findById(Long id) {
		return baseService.getById(FcNotice.class, id);
	}

	public void insertNotice(Map<String, Object> content, String owner, String type, String image, String name,
			String noticeType, String operator) {
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUser.ADDRESS, owner);
		FcUser user = baseService.getByCondition(FcUser.class, wrapper);
		// 如果不是在本站用户，不进行通知
		if (null == user) {
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
		/* this.noticeMapper.insert(notice); */
		baseService.save(notice);
	}

	/**
	 * @param address
	 * @return
	 */
	public Object getCount(String address) {
		QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNotice.OWNER, address).eq(FcNotice.IS_READ, false).eq(BaseEntity.DELETED, false);
		Integer unreadCounts = baseService.counts(FcNotice.class, wrapper);
		wrapper = new QueryWrapper<>();
		wrapper.eq(FcNotice.OWNER, address).eq(BaseEntity.DELETED, false);
		Integer totalCount = baseService.counts(FcNotice.class, wrapper);
		Map<String, Integer> map = new HashMap<>();
		map.put("unreadCount", unreadCounts);
		map.put("totalCount", totalCount);
		return ResponseUtil.ok(map);
	}
}
