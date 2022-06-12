package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FcContractService  {

	@Autowired
    private IBaseService baseService;
	

	 public FcContract get(String address){
	 	QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
	 	wrapper.eq(FcContract.ADDRESS, address)
				.eq(FcContract.DELETED, false);
	 	return this.baseService.getByCondition(FcContract.class, wrapper);
	 }


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
		wrapper.eq(BaseEntity.DELETED, false);
        if(!StringUtils.isEmpty(name) ){
            wrapper.like(FcContract.NAME, name);
        }
        if(null != isverify && isverify ){
            wrapper.eq(FcContract.VERIFY, isverify);
        }
        if(!StringUtils.isEmpty(caddress) ){
            wrapper.eq(FcContract.ADDRESS, caddress);
        }
        if (isASC && !StringUtils.isEmpty(sortType)) {
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
    public boolean VerifyContract(FcContract contract) {
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


	public Object save(FcContract contract) {
		if(hasContract(contract)){
			return ResponseUtil.fail(-1, "已有对应的合约");
		}

		if(contract.getIsAdmin() && hasSystemContract(contract)){
			return ResponseUtil.fail(-1, "系统合约已经存在");
		}
		if(!DappWeb3jUtil.isValidAddress(contract.getAddress())){
			return ResponseUtil.fail(-1, "address is incorrect");
		}
		if(!DappWeb3jUtil.isValidAddress(contract.getOwner())){
			return ResponseUtil.fail(-1, "owner is incorrect");
		}

		contract.setIsSync(true);
		this.baseService.save(contract);
		return ResponseUtil.ok();
	}

	public Object update(FcContract contract) {
		if(hasContract(contract)){
			return ResponseUtil.fail(-1, "已有对应的合约");
		}

		if(contract.getIsAdmin() && hasSystemContract(contract)){
			return ResponseUtil.fail(-1, "系统合约已经存在");
		}
		if(!DappWeb3jUtil.isValidAddress(contract.getAddress())){
			return ResponseUtil.fail(-1, "address is incorrect");
		}
		if(!DappWeb3jUtil.isValidAddress(contract.getOwner())){
			return ResponseUtil.fail(-1, "owner is incorrect");
		}

		this.baseService.update(contract);
		return ResponseUtil.ok();
	}

	private  Boolean hasContract(FcContract contract){
		QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		wrapper.eq(FcContract.ADDRESS, contract.getAddress());
		FcContract temp = this.baseService.getByCondition(FcContract.class, wrapper);
		if(null != temp && temp.getId() != contract.getId()){
			return true;
		}
		return false;
	}


	private Boolean hasSystemContract(FcContract contract){
		QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		wrapper.eq(FcContract.IS_ADMIN, true);
		FcContract temp = this.baseService.getByCondition(FcContract.class, wrapper);
		if(null != temp && temp.getId() != contract.getId()){
			return true;
		}
		return false;
	}

	/**
	 * @param address
	 * @return
	 */
	public Object delete(String address) {
		UpdateWrapper<FcContract> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcContract.ADDRESS, address);
		this.baseService.deleteByCondition(FcContract.class, wrapper);
		return ResponseUtil.ok();
	}


	/**
     *findById
     * @param id
     * @return
     */
    public FcContract findById(Long id) {

        return baseService.getById(FcContract.class,id);
    }

	/**
	 * @param
	 * @return
	 */
	public List<String> findAllAddress() {
		QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		List<FcContract> list = this.baseService.findByCondition(FcContract.class, wrapper);
		return list.stream().map(contract->contract.getAddress()).collect(Collectors.toList());
	}


}
