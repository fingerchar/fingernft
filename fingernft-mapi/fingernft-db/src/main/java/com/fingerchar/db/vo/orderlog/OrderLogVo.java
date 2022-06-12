package com.fingerchar.db.vo.orderlog;

import com.alibaba.fastjson.JSON;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcOrderLog;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.dto.TransferLog;
import com.fingerchar.db.vo.UserBaseInfoVo;

import java.util.Map;

/**
 * @Author： Zjm
 * @Date：2022/4/12 9:57
 */
public class OrderLogVo {
    private String from;
    private String to;
    private UserBaseInfoVo fromUser;
    private UserBaseInfoVo toUser;
    private Integer type;
    private String txHash;
    private String token;
    private String tokenId;
    private Boolean expired;

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    private Map<String, Object> content;

    private String paytokenAddress;
    private String paytokenName;
    private String paytokenSymbol;
    private Integer paytokenDecimals;

    private Long createTime;
    private Long updateTime;

    private FcOrder order;
    private TransferLog transferLog;


    public OrderLogVo(FcOrderLog orderLog, Map<String, Object> content, FcUser fromUser, FcUser toUser){
        this.from = orderLog.getFrom();
        this.to = orderLog.getTo();
        if(null != fromUser) {
            this.fromUser = new UserBaseInfoVo(fromUser);
        }
        if(null != toUser) {
            this.toUser = new UserBaseInfoVo(toUser);
        }
        this.type = orderLog.getType();
        this.txHash = orderLog.getTxHash();
        this.token = orderLog.getToken();
        this.tokenId = orderLog.getTokenId();
        this.expired = orderLog.getExpired();
        // this.content = JSON.parseObject(orderLog.getContent());
        this.content = content;
        this.updateTime = orderLog.getUpdateTime();
        this.createTime = orderLog.getCreateTime();
        this.paytokenAddress = orderLog.getPaytokenAddress();
        this.paytokenName = orderLog.getPaytokenName();
        this.paytokenSymbol = orderLog.getPaytokenSymbol();
        this.paytokenDecimals = orderLog.getPaytokenDecimals();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public UserBaseInfoVo getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserBaseInfoVo fromUser) {
        this.fromUser = fromUser;
    }

    public UserBaseInfoVo getToUser() {
        return toUser;
    }

    public void setToUser(UserBaseInfoVo toUser) {
        this.toUser = toUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public String getPaytokenAddress() {
        return paytokenAddress;
    }

    public void setPaytokenAddress(String paytokenAddress) {
        this.paytokenAddress = paytokenAddress;
    }

    public String getPaytokenName() {
        return paytokenName;
    }

    public void setPaytokenName(String paytokenName) {
        this.paytokenName = paytokenName;
    }

    public String getPaytokenSymbol() {
        return paytokenSymbol;
    }

    public void setPaytokenSymbol(String paytokenSymbol) {
        this.paytokenSymbol = paytokenSymbol;
    }

    public Integer getPaytokenDecimals() {
        return paytokenDecimals;
    }

    public void setPaytokenDecimals(Integer paytokenDecimals) {
        this.paytokenDecimals = paytokenDecimals;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public FcOrder getOrder() {
        return order;
    }

    public void setOrder(FcOrder order) {
        this.order = order;
    }

    public TransferLog getTransferLog() {
        return transferLog;
    }

    public void setTransferLog(TransferLog transferLog) {
        this.transferLog = transferLog;
    }


}
