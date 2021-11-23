package com.fingerchar.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.annotation.RequiresPermissionsDesc;
import com.fingerchar.base.controller.BaseController;
import com.fingerchar.domain.FcContractNft;
import com.fingerchar.domain.FcNftItems;
import com.fingerchar.service.FcContractNftService;
import com.fingerchar.service.FcNftItemsService;
import com.fingerchar.utils.ResponseUtil;
import com.fingerchar.vo.FcContractNftVo;

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

    @RequiresPermissions("admin:nft:list")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "查询")
    @PostMapping("/list")
    public Object list(String categoryId,
                       String creator,
                       String order,
                       String sort,
                       String address,
                       String tokenId,
                       Integer nftVerify) {
        IPage<FcContractNftVo> iPage = contractNftService.queryList(categoryId, creator,address, tokenId, this.getPageInfo(), this.isAsc(order), sort, nftVerify);
        return ResponseUtil.okList(iPage);
    }

    @RequiresPermissions("admin:nft:detail")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "详情")
    @PostMapping("/detail")
    public Object detail(@NotNull Long id) {
        List<FcNftItems> items = nftItemsService.getByNftId(id);
        return ResponseUtil.ok(items);
    }




    @RequiresPermissions("admin:nft:verify")
    @RequiresPermissionsDesc(menu = {"NFT管理", "NFT列表"}, button = "认证")
    @PostMapping("/verify")
    public Object verify(@NotNull Long id, Integer nftVerify) {
        FcContractNft contractNft = contractNftService.findById(id);
        if (contractNft == null) {
            return ResponseUtil.badArgument();
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
            return ResponseUtil.badArgument();
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
            return ResponseUtil.badArgument();
        }
        boolean ok=contractNftService.disableContract(contractNft);
        return ResponseUtil.ok(ok);
    }

}
