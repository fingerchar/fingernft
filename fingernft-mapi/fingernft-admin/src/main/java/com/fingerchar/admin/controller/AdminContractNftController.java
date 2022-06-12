package com.fingerchar.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.admin.service.FcContractNftService;
import com.fingerchar.admin.service.FcNftItemsService;
import com.fingerchar.admin.vo.AdminNftParamVo;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.manager.FcNftItemsManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.vo.NftInfoVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/nft")
public class AdminContractNftController  extends BaseController {

    @Autowired
    private FcContractNftService contractNftService;

    @Autowired
    private FcNftItemsService nftItemsService;

    @Autowired
    private FcNftItemsManager nftItemsManager ;

    @RequiresPermissions("admin:nft:list")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "查询")
    @PostMapping("/list")
    public Object list(AdminNftParamVo nftParamVo) {
        IPage<NftInfoVo> iPage = contractNftService.queryList(nftParamVo, this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }


    @RequiresPermissions("admin:nft:detail")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "详情")
    @PostMapping("/detail")
    public Object detail(@NotNull String address, @NotNull String tokenId) {
        FcNftItems items = nftItemsManager.getByAddressAndTokenId(address, tokenId);
        return ResponseUtil.ok(items);
    }


    @RequiresPermissions("admin:nft:verify")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "认证")
    @PostMapping("/verify")
    public Object verify(@NotNull String address,@NotNull String tokenId, Integer nftVerify) {
        FcContractNft contractNft = contractNftService.getByAddressAndTokenId(address, tokenId);
        if (contractNft == null) {
        	return ResponseUtil.fail(-1, "NFT不存在！");
        }
        boolean ok=contractNftService.verifyContract(contractNft,nftVerify);
        return ResponseUtil.ok(ok);
    }


    @RequiresPermissions("admin:nft:enable")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "启用")
    @PostMapping("/enable")
    public Object enable(@NotNull Long id) {
        FcContractNft contractNft = contractNftService.findById(id);
        if (contractNft == null) {
        	return ResponseUtil.fail(-1, "NFT不存在！");
        }
        boolean ok=contractNftService.enableContract(contractNft);
        return ResponseUtil.ok(ok);
    }


    @RequiresPermissions("admin:nft:disable")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "禁用")
    @PostMapping("/disable")
    public Object disable(@NotNull Long id) {
        FcContractNft contractNft = contractNftService.findById(id);
        if (contractNft == null) {
        	return ResponseUtil.fail(-1, "NFT不存在！");
        }
        boolean ok=contractNftService.disableContract(contractNft);
        return ResponseUtil.ok(ok);
    }


}
