package com.fingerchar.api.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcPayToken;

@Service
public class FcPayTokenService {

/*    @Resource
    private FcPayTokenMapper payTokenMapper;*/

    @Resource
    private IBaseService baseService;
    
    public List<FcPayToken> findAll() {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		return this.baseService.findByCondition(FcPayToken.class, wrapper);
	}


    /**
     * 查询
     * @return
     */
    public FcPayToken querySelective(String symbol) {
       /* FcPayTokenExample example = new FcPayTokenExample();
        FcPayTokenExample.Criteria criteria = example.createCriteria();
        criteria.andSymbolEqualTo(symbol);
        return payTokenMapper.selectOneByExample(example);*/
        QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcPayToken.SYMBOL,symbol);
        return baseService.getByCondition(FcPayToken.class,wrapper);
    }

    public  FcPayToken queryListSelective(List<String> symbols) {
    	/*FcPayTokenExample example = new FcPayTokenExample();
    	FcPayTokenExample.Criteria criteria = example.createCriteria();
        criteria.andSymbolIn(symbols);
        return payTokenMapper.selectOneByExample(example);*/
        QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
        wrapper.in(FcPayToken.SYMBOL, Arrays.asList(symbols));
        return baseService.getByCondition(FcPayToken.class,wrapper);
    }

    /**
     * 根据ID更新
     */
    public boolean updatePayToken(FcPayToken payToken) {
        /*payToken.setUpdateTime(LocalDateTime.now());
        return payTokenMapper.updateByPrimaryKeySelective(payToken) > 0;*/
        payToken.setUpdateTime(System.currentTimeMillis());
        return baseService.update(payToken) > 0;
    }

    /**
     * 根据ID添加
     */
    public boolean insertPayToken(FcPayToken payToken) {
        payToken.setUpdateTime(System.currentTimeMillis());
        /*return payTokenMapper.insert(payToken) > 0;*/
        return baseService.save(payToken) > 0;
    }




}
