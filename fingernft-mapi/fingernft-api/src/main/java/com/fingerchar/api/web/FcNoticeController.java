package com.fingerchar.api.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.service.FcNoticeService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.vo.notice.NoticeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/notices")
public class FcNoticeController extends BaseController {

	@Autowired
	FcNoticeService noticeService;

	@Autowired
	FcUserService userService;

	@PostMapping("list")
	public Object list(Integer isRead, String address) {
		if (StringUtils.isEmpty(address)) {
			return ResponseUtil.okList(this.getPageInfo());
		}
		IPage<NoticeInfoVo> list = this.noticeService.page(isRead, address, this.getPageInfo());
		return ResponseUtil.okList(list);
	}
	
	@PostMapping("count")
	public Object count(String address) {
		if (StringUtils.isEmpty(address)) {
			return ResponseUtil.ok(new JSONObject());
		}
		return this.noticeService.getCount(address);
	}

	
	@PostMapping("countunread")
	public Object countUnRead(String address) {
		if(StringUtils.isEmpty(address)) {
			return ResponseUtil.ok(new JSONObject());
		}
		Integer count = this.noticeService.findCountUnRead(address);
		return ResponseUtil.ok(count);
	}

	@PostMapping("unread")
	public Object unRead() {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if (null == user) {
			return ResponseUtil.badArgument();
		}
		Integer count = this.noticeService.findCountUnRead(user.getAddress());
		return ResponseUtil.ok(count);
	}

	@PostMapping("read")
	public Object read(Long id) {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		if (null == id) {
			return ResponseUtil.badArgument();
		}
		this.noticeService.read(id);
		return ResponseUtil.ok();
	}

	@PostMapping("readall")
	public Object readall() {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		this.noticeService.readAll(userAddress);
		return ResponseUtil.ok();
	}

}
