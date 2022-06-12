package com.fingerchar.api.service;

import com.alibaba.fastjson.JSON;
import com.fingerchar.api.vo.ConfigFetchVo;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.db.domain.FcSystem;
import com.fingerchar.db.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Service
public class FcSystemConfigService{
	
    @Autowired
    private FcSystemConfigManager systemConfigManager;

    /**
    * 获取系统名称和系统配置的键值对
    */
    public ConfigFetchVo allShow() {
        List<FcSystem> systemList = this.systemConfigManager.allShow();
        ConfigFetchVo fetchVo = new ConfigFetchVo();
        for (FcSystem item : systemList) {
            if(StringUtils.isEmpty(item.getKeyValue())){
                continue;
            }
            switch (item.getKeyName()){
                case SysConfConstant.IPFS_URL:
                    fetchVo.setIpfsUrl(item.getKeyValue());
                    break;
                case SysConfConstant.SELLER_FEE:
                    fetchVo.setSellerFee(item.getKeyValue());
                    break;
                case SysConfConstant.BUYER_FEE:
                    fetchVo.setBuyerFee(item.getKeyValue());
                    break;
                case SysConfConstant.CDN_URL:
                    fetchVo.setCdnUrl(item.getKeyValue());
                    break;
                case SysConfConstant.LOGIN_MESSAGE:
                    fetchVo.setLoginMessage(item.getKeyValue());
                    break;
                case SysConfConstant.WEBSITE:
                    fetchVo.setWebsite(item.getKeyValue());
                    break;
                case SysConfConstant.CONFIG_DEPLOY:
                    ConfigDeploy configDeploy = JSON.parseObject(item.getKeyValue(), ConfigDeploy.class);
                    String minerAddress = configDeploy.getMinerAddress();
                    fetchVo.setMiner(minerAddress);
                    break;
                case SysConfConstant.CONFIG_NETWORK:
                    ConfigNetwork configNetwork = JSON.parseObject(item.getKeyValue(), ConfigNetwork.class);
                    configNetwork.setRpc(null);
                    fetchVo.setNetwork(configNetwork);
                    break;
                case SysConfConstant.CONFIG_CONTRACT:
                    ConfigContract configContract = JSON.parseObject(item.getKeyValue(), ConfigContract.class);
                    configContract.setExchangeOrdersHolderAddress(null);
                    configContract.setExchangeStateAddress(null);
                    configContract.setTransferProxyForDeprecatedAddress(null);
                    configContract.setNft721Address(null);
                    configContract.setNft1155Address(null);
                    fetchVo.setContract(configContract);
                    break;
                default:
                    break;
            }
        }
        return fetchVo;
    }


    public GasTracker gasTracker(){
        String value = this.systemConfigManager.getKeyValue(SysConfConstant.GAS_TRACKER);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        GasTracker gasTracker = JSON.parseObject(value, GasTracker.class);
        return gasTracker;
    }

}
