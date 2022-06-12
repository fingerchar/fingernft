package com.fingerchar.db.vo.notice;

import com.fingerchar.db.vo.UserBaseInfoVo;

/**
 * @Author： Zjm
 * @Date：2022/4/1 14:41
 */
public class NoticeContentVo {
    Long id;
    NftInfoVo nft;
    UserBaseInfoVo user;
    ContractVo contract;


    public ContractVo getContract() {
        return contract;
    }

    public void setContract(ContractVo contract) {
        this.contract = contract;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    private String txHash;

    public NftInfoVo getNft() {
        return nft;
    }

    public void setNft(NftInfoVo nft) {
        this.nft = nft;
    }

    public UserBaseInfoVo getUser() {
        return user;
    }

    public void setUser(UserBaseInfoVo user) {
        this.user = user;
    }

}
