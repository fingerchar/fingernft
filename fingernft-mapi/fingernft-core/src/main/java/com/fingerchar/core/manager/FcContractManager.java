package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.db.domain.FcContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * @Author： Zjm
 * @Date：2022/3/21 20:25
 */
@Service
public class FcContractManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcNoticeManager noticeManager;

    public FcContract setAdminContract(String name, String symbol, String address, Integer type){
        FcContract contract = this.getAdminContract();
        if(null != contract){
            return contract;
        }
        contract = new FcContract();
        contract.setName(name);
        contract.setSymbol(symbol);
        contract.setAddress(address.toLowerCase(Locale.ROOT));
        contract.setIsSync(true);
        contract.setIsAdmin(true);
        contract.setVerify(true);
        this.save(contract);
        return contract;
    }

    public FcContract getAdminContract(){
        QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContract.IS_SYNC, true)
                .eq(FcContract.IS_ADMIN, true)
                .eq(FcContract.DELETED, false);
        return this.baseService.getByCondition(FcContract.class, wrapper);
    }

    public FcContract get(String address){
        QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContract.ADDRESS, address)
                .eq(FcContract.DELETED, false);
        return this.baseService.getByCondition(FcContract.class, wrapper);
    }

    public Integer add(String address){
        FcContract contract = new FcContract();
        contract.setAddress(address);
        contract.setIsSync(true);
        return this.save(contract);
    }

    public Integer update(FcContract contract){
        return this.baseService.update(contract);
    }

    public Integer save(FcContract contract){
        return this.baseService.save(contract);
    }
}
