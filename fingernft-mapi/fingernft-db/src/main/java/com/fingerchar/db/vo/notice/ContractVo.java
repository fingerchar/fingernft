package com.fingerchar.db.vo.notice;

import com.fingerchar.db.domain.FcContract;

/**
 * @Author： Zjm
 * @Date：2022/4/4 17:21
 */
public class ContractVo {
    private String cover;
    private String name;
    private String symbol;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String address;



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
