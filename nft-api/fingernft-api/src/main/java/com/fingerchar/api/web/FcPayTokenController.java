package com.fingerchar.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcPayTokenService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/paytoken")
public class FcPayTokenController extends BaseController {
	
	@Autowired
	FcPayTokenService payTokenService;

	@PostMapping(value = "/list")
	public Object list() {
		return ResponseUtil.ok(this.payTokenService.findAll());
	}
}
