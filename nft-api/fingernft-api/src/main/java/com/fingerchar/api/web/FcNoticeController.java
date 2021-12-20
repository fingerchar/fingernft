package com.fingerchar.api.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.FcNoticeInfo;
import com.fingerchar.api.service.FcNoticeService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.ListUtils;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcNotice;
import com.fingerchar.db.domain.FcUser;

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
		FcUser user = userService.getUserByAddress(address);
		if (user == null) {
			return ResponseUtil.okList(this.getPageInfo());
		}
		IPage<FcNotice> list = this.noticeService.findAll(isRead, user.getAddress(), this.getPageInfo());
		List<FcNoticeInfo> infoList = this.processNoticeList(list);
		return ResponseUtil.okList(infoList, list);
	}
	
	@PostMapping("count")
	public Object count(String address) {
		if (StringUtils.isEmpty(address)) {
			return ResponseUtil.ok(new JSONObject());
		}
		FcUser user = userService.getUserByAddress(address);
		if (user == null) {
			return ResponseUtil.ok(new JSONObject());
		}
		return this.noticeService.getCount(user.getAddress());
	}
	
	private List<FcNoticeInfo> processNoticeList(IPage<FcNotice> list) {
		List<FcNoticeInfo> infoList = list.getRecords().stream().map(notice->new FcNoticeInfo(notice)).collect(Collectors.toList());
		List<String> addrs = list.getRecords().stream().map(notice->notice.getOperator()).collect(Collectors.toList());
		if(addrs.isEmpty()) {
			return infoList;
		}
		List<FcUser> userList = this.userService.findListByAddrs(addrs);
		
		infoList.stream().forEach(info -> {
			info.addOperatorInfo(ListUtils.getByPredicate(userList, user -> user.getAddress().toLowerCase().equals(info.getOperator().toLowerCase())));
		});
		return infoList;
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
			return ResponseUtil.fail(-1, "Invalid user");
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
		if (id == null) {
			return ResponseUtil.fail(-1, "notice id can not be empty");
		}
		FcNotice fcNotice = noticeService.findById(id);
		if (fcNotice == null) {
			return ResponseUtil.fail(-1, "can not found this notice");
		}

		this.noticeService.updateReadByIds(fcNotice);
		return ResponseUtil.ok();
	}

	@PostMapping("readall")
	public Object readall() {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if (null == user) {
			return ResponseUtil.unlogin();
		}
		this.noticeService.updateAll(user.getAddress());
		return ResponseUtil.ok();
	}

}
