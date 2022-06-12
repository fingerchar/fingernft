package com.fingerchar.admin.controller;

import com.fingerchar.core.manager.FcTxOrderManager;
import com.fingerchar.core.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/txorder")
public class AdminTxOrderController {
    @Autowired
    private FcTxOrderManager txOrderManager;

    @PostMapping("info")
    public Object info(){
        return ResponseUtil.ok(this.txOrderManager.info());
    }
}
