package com.fingerchar.api.web;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcNftItemsService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/contract")
public class FcContractController extends BaseController {

    @Autowired
    FcContractService contractService;

    @Autowired
    FcContractNftService contractNftService;

    @Autowired
    FcUserService userService;

    @Autowired
    FcNftItemsService nftItemsService;
    
    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    IBaseService baseService;

    @PostMapping("add")
    public Object add(FcContract contract) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        if (StringUtils.isEmpty(contract.getSigner())) {
            return ResponseUtil.fail(-1, "Signer is empty");
        }
        if(!this.systemConfigManager.getKeyValue(SysConfConstant.WEBSITE).equals(contract.getSigner())) {
        	return ResponseUtil.fail(-1, "Invalid Signer");
        }
        
        FcUser user = this.userService.getUserByAddress(userAddress);
        if (null == user) {
            return ResponseUtil.unauthz();
        }
        FcContract temp = this.contractService.getByAddress(contract.getAddress());
        if (null == temp) {
            contract.setDeleted(false);
            contract.setOwner(user.getAddress());
            contract.setIsAdmin(false);
            Integer count = this.contractService.save(contract);
            if (count == 0) {
                return ResponseUtil.fail(-1, "System error");
            } else {
                return ResponseUtil.ok(temp);
            }
        } else {
            return ResponseUtil.ok(temp);
        }
    }
    
    @PostMapping("listall")
    public Object listAll() {
        return ResponseUtil.okList(this.contractService.listAll(this.getPageInfo()));
    }

    @PostMapping("info")
    public Object info(String caddress) {
        if (StringUtils.isEmpty(caddress)) {
            return ResponseUtil.ok(new JSONObject());
        }
        FcContract contract = this.contractService.getByAddress(caddress);
        if (null == contract) {
            return ResponseUtil.ok(new JSONObject());
        }
        return ResponseUtil.ok(contract);
    }

    @PostMapping("getinfo")
    public Object getinfo(String address) {
        if (StringUtils.isEmpty(address)) {
            return ResponseUtil.ok(new JSONObject());
        }
        FcContract contract = this.contractService.getInfo(address);
        if (null == contract) {
            return ResponseUtil.ok(new JSONObject());
        }
        return ResponseUtil.ok(contract);
    }

    @PostMapping("list")
    public Object list(HttpServletRequest request) {
        return ResponseUtil.ok(this.contractService.findAll());
    }

    @PostMapping("onsales")
    public Object onsales(String address) {
    	if(null == address) {
			return ResponseUtil.okList(this.getPageInfo());
		}
		return contractNftService.findContractNft(this.getPageInfo(), address, true);
    }

    @PostMapping("collections")
    public Object collections(String address) {
        if (null == address) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        IPage<FcContractNft> fcContractNfts = contractNftService.findAllByAddress(address, this.getPageInfo());
        return ResponseUtil.okList(fcContractNfts);
    }

    @PostMapping("create")
    public Object create(FcContract contract) {
        Integer save = contractService.save(contract);
        if (save > 0) {
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(-1, "save fail");
        }
    }

    @PostMapping("listbyaddr")
    public Object listByAddress(String[] addresss) {
        if (null == addresss || addresss.length == 0) {
            return ResponseUtil.ok();
        }
        List<String> addrList = Arrays.asList(addresss);
        return ResponseUtil.ok(this.contractService.findListByAdress(addrList));
    }

    @PostMapping("listitems")
    public Object listContractItems(String address, Boolean isSell) {
        FcContract contract = this.contractService.getByAddress(address);
        if (null == contract) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        return contractNftService.findContractNft(this.getPageInfo(), address, isSell);
    }

    @PostMapping("stat")
    public Object stat(String address) {
        Map<String, Object> result = new HashMap<String, Object>();
        Integer saleCount = this.contractNftService.countContractOnsale(address);
        Integer collectionCount = this.contractNftService.countContractCollections(address);
        result.put("saleCount", saleCount);
        result.put("collectionCount", collectionCount);
        return ResponseUtil.ok(result);
    }

}
