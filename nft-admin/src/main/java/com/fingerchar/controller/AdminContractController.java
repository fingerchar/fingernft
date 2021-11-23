package com.fingerchar.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.annotation.RequiresPermissionsDesc;
import com.fingerchar.base.controller.BaseController;
import com.fingerchar.domain.FcContract;
import com.fingerchar.service.FcContractService;
import com.fingerchar.utils.ResponseUtil;


@RestController
@RequestMapping("/admin/contract")
public class AdminContractController extends BaseController {

    @Autowired
    private FcContractService contractService;

    @RequiresPermissions("admin:contract:list")
    @RequiresPermissionsDesc(menu = {"合约管理", "合约列表"}, button = "查询")
    @PostMapping("/list")
    public Object list(String address,
                       Boolean verify,
                       String name,
                       String sort,
                       String order) {
        IPage<FcContract>  page = contractService.querySelective(address, verify,name, this.getPageInfo(), this.isAsc(order),sort);
        return ResponseUtil.okList(page);
    }

    @RequiresPermissions("admin:contract:verify")
    @RequiresPermissionsDesc(menu = {"合约管理", "合约列表"}, button = "认证")
    @PostMapping("/verify")
    public Object verify(@NotNull Long id) {
        FcContract contract = contractService.findById(id);
        if (contract == null) {
            return ResponseUtil.badArgument();
        }
        boolean ok=contractService.VerifyContract(contract);
        return ResponseUtil.ok(ok);
    }


    @RequiresPermissions("admin:contract:enable")
    @RequiresPermissionsDesc(menu = {"合约管理", "合约列表"}, button = "禁用")
    @PostMapping("/enable")
    public Object enable(@NotNull Long id) {
        FcContract contract = contractService.findById(id);
        if (contract == null) {
            return ResponseUtil.badArgument();
        }
        boolean ok=contractService.disableContract(contract);
        return ResponseUtil.ok(ok);
    }

}
