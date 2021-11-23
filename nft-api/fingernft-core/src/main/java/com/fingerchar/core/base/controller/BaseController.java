package com.fingerchar.core.base.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.constant.SysConstant;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseController {

	public Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String BASICPACKAGE = "com.fingerchar.api";
	
	public static final String PREFIX_URL = "fingernft";
	
	@Resource
	protected HttpServletRequest request;

	@Resource
	protected HttpSession session;

	private Long getPageNum() {
		String page = request.getParameter(SysConstant.PAGE);
		if(null == page) {
			return 1L;
		} else {
			return Long.parseLong(page);
		}
	}

	private Long getPageSize() {
		String limit = request.getParameter(SysConstant.LIMIT);
		if(null == limit) {
			return 10L;
		} else {
			return Long.parseLong(limit);
		}
	}

	protected <T> Page<T> getPageInfo() {
		Page<T> page = new Page<>(this.getPageNum(), this.getPageSize());
		return page;
	}

	protected boolean isAsc(String order){
		if( StringUtils.isNotEmpty(order) ){
			if( order.equals("ASC") ){
				return true;
			}
		}
		return false;
	}
}
