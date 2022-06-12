package com.fingerchar.admin.service;

import com.alibaba.fastjson.JSON;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.db.dto.GasTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/4/12 19:09
 */
@Service
public class GasTrackerService {

    @Autowired
    FcSystemConfigManager systemConfigManager;

    public void getGasTracker(List<BigInteger> gasPriceList, BigInteger lastBlock) {
        if(gasPriceList.isEmpty()){
            return;
        }
        int length = gasPriceList.size();
        int lowIndex = length / 10;
        int highIndex = (length / 5) * 3;
        BigInteger low = gasPriceList.get(lowIndex);
        BigInteger max = gasPriceList.get(highIndex);
        BigInteger diff = max.subtract(low).divide(new BigInteger("4"));

        GasTracker gasTracker = new GasTracker(
                low,
                low.add(diff.multiply(new BigInteger("2"))),
                low.add(diff.multiply(new BigInteger("3"))),
                lastBlock
        );
        String value = JSON.toJSONString(gasTracker);
        this.systemConfigManager.save(SysConfConstant.GAS_TRACKER, value);
    }
}
