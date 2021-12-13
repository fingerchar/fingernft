package com.fingerchar.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcContract;

@Service
public class FcContractService  {

    @Autowired
    private IBaseService baseService;

    /**
     * 查询
     * @param caddress
     * @param isverify
     * @param name
     * @param page
     * @param
     * @param
     * @param
     * @return
     */
    public IPage<FcContract> querySelective(String caddress, Boolean isverify, String name, IPage<FcContract> page, boolean isASC, String sortType) {
        QueryWrapper<FcContract> wrapper =new QueryWrapper<>();
        if(!StringUtils.isEmpty(name) ){
            wrapper.like(FcContract.NAME, name);
        }
        if(!StringUtils.isEmpty(isverify) ){
            wrapper.eq(FcContract.VERIFY, isverify);
        }
        if(!StringUtils.isEmpty(caddress) ){
            wrapper.eq(FcContract.ADDRESS, caddress);
        }
        if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC,sortType);
        }
        return baseService.findByPage(FcContract.class, wrapper, page);
    }



    /**
     * 根据ID更新Verify
     *
     * @param contract Verify记录
     * @return 更新成功返回true，否则返回false
     */
    public boolean verifyContract(FcContract contract) {
        contract.setVerify(true);
        contract.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.update(contract) > 0;
    }

    /**
     * 根据ID禁用
     *
     * @param contract
     * @return 更新成功返回true，否则返回false
     */
    public boolean disableContract(FcContract contract) {
        contract.setDeleted(true);
        contract.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.update(contract) > 0;
    }


    /**
     * 根据ID启用
     *
     * @param contract
     * @return 更新成功返回true，否则返回false
     */
    public boolean enableContract(FcContract contract) {
        contract.setDeleted(false);
        contract.setUpdateTime(System.currentTimeMillis()/1000);
        return baseService.update(contract) > 0;
    }


    /**
     *findById
     * @param id
     * @return
     */
    public FcContract findById(Long id) {
        return baseService.getById(FcContract.class,id);
    }
	
	public List<String> findAllAddress() {
		QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcContract.IS_ADMIN, true);
		List<FcContract> list = this.baseService.findByCondition(FcContract.class, wrapper);
		return list.stream().map(contract->contract.getAddress()).collect(Collectors.toList());
	}
}
