package com.fingerchar.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcNotice;
import com.fingerchar.domain.FcUser;


@Service
public class FcNoticeService {

	@Autowired
	private IBaseService baseService;


	@Transactional
	public void insertNotice(Map<String, Object> content, String owner, String type, String image, String name, String noticeType, String operator) {
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUser.ADDRESS, owner);
		FcUser user = baseService.getByCondition(FcUser.class, wrapper);
		//如果不是在本站用户，添加到用户列表，但是不进行通知
		if(null == user) {
			user = new FcUser();
			user.setAddress(owner);
			user.setIsWeb(false);
			this.baseService.save(user);
			return;
		} else {
			if(null == user.getIsWeb() || !user.getIsWeb()) {
				return;
			}
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
		baseService.save(notice);
	}
}
