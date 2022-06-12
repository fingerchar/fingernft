package com.fingerchar.db.dto;

/**
 * @Author： Zjm
 * @Date：2022/3/30 18:53
 */
public class ConfigNetwork {
    private String network;
    private Integer chainId;
    private String name;
    private String symbol;
    private String explorer;
    private String opensea;
    private String rpc;
    private Integer blockTime;


    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Integer getChainId() {
        return chainId;
    }

    public void setChainId(Integer chainId) {
        this.chainId = chainId;
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

    public String getExplorer() {
        return explorer;
    }

    public void setExplorer(String explorer) {
        this.explorer = explorer;
    }

    public String getOpensea() {
        return opensea;
    }

    public void setOpensea(String opensea) {
        this.opensea = opensea;
    }

    public String getRpc() {
        return rpc;
    }

    public void setRpc(String rpc) {
        this.rpc = rpc;
    }

    public Integer getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Integer blockTime) {
        this.blockTime = blockTime;
    }

}
