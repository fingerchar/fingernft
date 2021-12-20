package com.fingerchar.api.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.constant.ContractType;
import com.fingerchar.api.dto.NftItemsInfo;
import com.fingerchar.api.dto.UserVo;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcNftItemsService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.ListUtils;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcUser;

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
    IBaseService baseService;

    @PostMapping("add")
    public Object add(FcContract contract) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        if (StringUtils.isEmpty(contract.getAddress())) {
            return ResponseUtil.fail(-1, "contract address can not be empty");
        }
        FcUser user = this.userService.getUserByAddress(userAddress);
        if (null == user) {
            return ResponseUtil.unauthz();
        }
        FcContract temp = this.contractService.getByAddress(contract.getAddress());
        if (null == temp) {
            ContractType type = ContractType.getContractType(contract.getType());
            if (null == type) {
                return ResponseUtil.fail(-1, "Unkown contract type");
            } else {
                contract.setType(type.getType());
            }
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
        if (null == address) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        IPage<FcContractNft> list = contractNftService.findListForIndex(this.getPageInfo(), address, new Long(0), null, "", "");
        List<Map<String, Object>> result = new ArrayList<>(list.getRecords().size());
        if (list != null && !list.getRecords().isEmpty()) {
            List<Long> nftIds = list.getRecords().stream().map(FcContractNft::getId).collect(Collectors.toList());
            List<String> contractAddrs = list.getRecords().stream().map(FcContractNft::getAddress).collect(Collectors.toList());
            List<FcContract> contractList = this.contractService.findListByAdress(contractAddrs);
            List<FcNftItems> itemsList = this.nftItemsService.findListByNftIds(nftIds);
            List<String> userAddrs = itemsList.stream().map(FcNftItems::getItemOwner).collect(Collectors.toList());
            List<FcUser> userList = this.userService.findListByAddrs(userAddrs);
            Map<String, Object> res = null;
            FcContract fcContract = null;
            FcUser fcUser = null;
            List<FcNftItems> tempItemsList = null;
            Map<String, Object> itemsMap = null;
            List<Map<String, Object>> tempList = null;
            QueryWrapper<FcOrder> orderWrapper = null;
            FcOrder order = null;
            for (FcContractNft nft : list.getRecords()) {
                res = new HashMap<>(3);
                fcContract = ListUtils.getByPredicate(contractList, contract -> contract.getAddress().toLowerCase().equals(nft.getAddress().toLowerCase()));
                tempItemsList = itemsList.stream().filter(item -> item.getNftId().equals(nft.getId())).collect(Collectors.toList());
                if (!tempItemsList.isEmpty()) {
                    itemsMap = new HashMap<>(2);
                    tempList = new ArrayList<>();
                    for (FcNftItems item : tempItemsList) {
                        NftItemsInfo itemInfo = new NftItemsInfo(item);
                        fcUser = ListUtils.getByPredicate(userList, user -> user.getAddress().toLowerCase().equals(item.getItemOwner().toLowerCase()));
                        if (null != fcUser) {
                            itemsMap.put("user", new UserVo(fcUser));
                        } else {
                            itemsMap.put("user", new UserVo(item.getItemOwner()));
                        }
                        if (null != item.getOnsell() && item.getOnsell().booleanValue()) {
                            orderWrapper = new QueryWrapper<>();
                            orderWrapper.eq(FcOrder.OWNER, item.getItemOwner())
                                    .eq(FcOrder.SELL_TOKEN, nft.getAddress())
                                    .eq(FcOrder.SELL_TOKEN_ID, nft.getTokenId())
                                    .eq(FcOrder.STATUS, 0)
                                    .eq(FcOrder.EXPIRED, false)
                                    .eq(BaseEntity.DELETED, false);
                            order = this.baseService.getByCondition(FcOrder.class, orderWrapper);
                            if (null != order) {
                                itemInfo.setCompleted(order.getSells());
                                itemInfo.setSellValue(order.getSellValue());
                                itemInfo.setBuyValue(order.getBuyerValue());
                                itemInfo.setPayTokenAddress(order.getBuyerToken());
                            } else {
                                itemInfo.setCompleted(0L);
                                itemInfo.setSellValue("0");
                                itemInfo.setBuyValue("");
                                itemInfo.setPayTokenAddress("");
                            }
                        } else {
                            itemInfo.setCompleted(0L);
                            itemInfo.setSellValue("0");
                            itemInfo.setBuyValue("");
                            itemInfo.setPayTokenAddress("");
                        }
                        itemsMap.put("item", itemInfo);
                        tempList.add(itemsMap);
                    }
                }
                res.put("contract", fcContract);
                res.put("nft", nft);
                res.put("itemList", tempList);
                result.add(res);
            }
        }
        return ResponseUtil.okList(result, list);
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

    @PostMapping("checkurl")
    public Object checkShortUrl(String url) {
        if (!StringUtils.isEmpty(url)) {
            FcContract aShort = contractService.getByShort(url);
            if (null == aShort) {
                return ResponseUtil.ok();
            }
        }
        return ResponseUtil.fail(-1, "short url is already exists");
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

        IPage<FcContractNft> list = contractNftService.findContractNft(this.getPageInfo(), address, isSell);
        List<Map<String, Object>> result = new ArrayList<>(list.getRecords().size());
        if (list != null && !list.getRecords().isEmpty()) {
            List<Long> nftIds = list.getRecords().stream().map(FcContractNft::getId).collect(Collectors.toList());
            List<FcNftItems> itemsList = this.nftItemsService.findListByNftIds(nftIds);
            List<String> userAddrs = itemsList.stream().map(FcNftItems::getItemOwner).collect(Collectors.toList());
            List<FcUser> userList = this.userService.findListByAddrs(userAddrs);
            Map<String, Object> res = null;
            FcUser fcUser = null;
            List<FcNftItems> tempItemsList = null;
            Map<String, Object> itemsMap = null;
            List<Map<String, Object>> tempList = null;
            QueryWrapper<FcOrder> orderWrapper = null;
            FcOrder order = null;
            for (FcContractNft nft : list.getRecords()) {
                res = new HashMap<>(3);
                tempItemsList = itemsList.stream().filter(item -> item.getNftId().equals(nft.getId())).collect(Collectors.toList());
                if (!tempItemsList.isEmpty()) {
                    tempList = new ArrayList<>();
                    for (FcNftItems item : tempItemsList) {
                        itemsMap = new HashMap<>(3);
                        NftItemsInfo itemInfo = new NftItemsInfo(item);
                        fcUser = ListUtils.getByPredicate(userList, user -> user.getAddress().toLowerCase().equals(item.getItemOwner().toLowerCase()));
                        if (null != fcUser) {
                            itemsMap.put("user", new UserVo(fcUser));
                        } else {
                            itemsMap.put("user", new UserVo(item.getItemOwner()));
                        }
                        if (null != item.getOnsell() && item.getOnsell().booleanValue()) {
                            orderWrapper = new QueryWrapper<>();
                            orderWrapper.eq(FcOrder.OWNER, item.getItemOwner())
                                    .eq(FcOrder.SELL_TOKEN, nft.getAddress())
                                    .eq(FcOrder.SELL_TOKEN_ID, nft.getTokenId())
                                    .eq(FcOrder.STATUS, 0)
                                    .eq(FcOrder.EXPIRED, false)
                                    .eq(BaseEntity.DELETED, false);
                            order = this.baseService.getByCondition(FcOrder.class, orderWrapper);
                            if (null != order) {
                                itemInfo.setCompleted(order.getSells());
                                itemInfo.setSellValue(order.getSellValue());
                                itemInfo.setBuyValue(order.getBuyerValue());
                                itemInfo.setPayTokenAddress(order.getBuyerToken());
                            } else {
                                itemInfo.setCompleted(0L);
                                itemInfo.setSellValue("0");
                                itemInfo.setBuyValue("");
                                itemInfo.setPayTokenAddress("");
                            }
                        } else {
                            itemInfo.setCompleted(0L);
                            itemInfo.setSellValue("0");
                            itemInfo.setBuyValue("");
                            itemInfo.setPayTokenAddress("");
                        }
                        itemsMap.put("item", itemInfo);
                        tempList.add(itemsMap);
                    }
                }
                res.put("contract", contract);
                res.put("nft", nft);
                res.put("itemList", tempList);
                result.add(res);
            }
        }
        return ResponseUtil.okList(result, list);
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
