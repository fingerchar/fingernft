package com.fingerchar.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.admin.service.FcContractService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
        	return ResponseUtil.fail(-1, "合约信息不能为空！");
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
        	return ResponseUtil.fail(-1, "合约不存在！");
        }
        boolean ok=contractService.disableContract(contract);
        return ResponseUtil.ok(ok);
    }


    @RequiresPermissions("admin:contract:create")
    @RequiresPermissionsDesc(menu = {"合约管理", "合约列表"}, button = "创建")
    @PostMapping("/create")
    public Object create(FcContract contract) {
        if(null == contract) {
            return ResponseUtil.fail(-1, "contract is null");
        }
        if(StringUtils.isEmpty(contract.getAddress()) ||
                StringUtils.isEmpty(contract.getName()) ||
                StringUtils.isEmpty(contract.getSymbol()) ||
                StringUtils.isEmpty(contract.getOwner())) {
            return ResponseUtil.fail(-1, "invalid paramater");
        }
        return this.contractService.save(contract);
    }

    @RequiresPermissions("admin:paytoken:update")
    @RequiresPermissionsDesc(menu = {"合约管理", "合约列表"}, button = "更新")
    @PostMapping("/update")
    public Object update(FcContract contract) {
        if(null == contract || null == contract.getId()) {
            return ResponseUtil.fail(-1, "invalid contract");
        }
        if(StringUtils.isEmpty(contract.getAddress()) ||
                StringUtils.isEmpty(contract.getName()) ||
                StringUtils.isEmpty(contract.getSymbol()) ||
                StringUtils.isEmpty(contract.getOwner())) {
            return ResponseUtil.fail(-1, "invalid paramater");
        }
        return this.contractService.update(contract);
    }

    @RequiresPermissions("admin:contract:delete")
    @RequiresPermissionsDesc(menu = {"合约管理", "合约列表"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(String address) {
        if(StringUtils.isEmpty(address)){
            return ResponseUtil.fail(-1, "address is empty");
        }

        return contractService.delete(address);
    }

}
