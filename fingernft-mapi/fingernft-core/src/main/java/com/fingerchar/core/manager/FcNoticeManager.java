package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.dto.*;
import com.fingerchar.db.vo.UserBaseInfoVo;
import com.fingerchar.db.vo.notice.ContractVo;
import com.fingerchar.db.vo.notice.NftInfoVo;
import com.fingerchar.db.vo.notice.NoticeContentVo;
import com.fingerchar.db.vo.notice.NoticeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author： Zjm
 * @Date：2022/3/21 18:52
 */
@Service
public class FcNoticeManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcUserManager userManager;


    public Integer readAll(String address){
        UpdateWrapper<FcNotice> wrapper = new UpdateWrapper<>();
        wrapper.eq(FcNotice.OWNER, address)
                .eq(FcNotice.IS_READ, false)
                .eq(FcNotice.DELETED, false);
        wrapper.set(FcNotice.IS_READ, true);
        return this.baseService.updateByCondition(FcNotice.class, wrapper);
    }

    public Integer countTotal(String address){
        QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNotice.OWNER, address)
                .eq(FcNotice.DELETED, false);
        return this.baseService.counts(FcNotice.class, wrapper);
    }

    public Integer countUnread(String address){
        QueryWrapper<FcNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNotice.OWNER, address)
                .eq(FcNotice.IS_READ, false)
                .eq(FcNotice.DELETED, false);
        return this.baseService.counts(FcNotice.class, wrapper);
    }

    public Integer add(Map<String, Object> content, String owner, Integer type, Integer noticeType, String operator){
        String _content = JSON.toJSONString(content);
        return this.add(_content, owner, type, noticeType, operator);
    }

    public Integer add(String content, String owner, Integer type, Integer noticeType, String operator){
        if(owner.equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
            return 0;
        }
        FcNotice notice = new FcNotice();
        notice.setContent(content);
        notice.setOwner(owner);
        notice.setType(noticeType);
        notice.setSubType(type);
        notice.setIsRead(false);
        notice.setOperator(operator);
        return this.save(notice);
    }

    public FcNotice get(Long id){
        return this.baseService.getById(FcNotice.class, id);
    }

    public Integer update(FcNotice notice){
        return this.baseService.update(notice);
    }

    public Integer save(FcNotice notice){
        notice.setOperator(notice.getOperator().toLowerCase(Locale.ROOT));
        notice.setOwner(notice.getOwner().toLowerCase(Locale.ROOT));
        return this.baseService.save(notice);
    }

}
