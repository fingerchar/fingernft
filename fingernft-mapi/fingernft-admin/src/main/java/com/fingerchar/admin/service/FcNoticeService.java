package com.fingerchar.admin.service;

import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FcNoticeService {


	@Autowired
	private IBaseService baseService;


	public FcNotice findById(Long id) {
		return baseService.getById(FcNotice.class,id);
	}

}
