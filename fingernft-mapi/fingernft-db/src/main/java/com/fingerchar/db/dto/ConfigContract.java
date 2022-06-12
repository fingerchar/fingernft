package com.fingerchar.db.dto;

/**
 * @Author： Zjm
 * @Date：2022/3/30 19:27
 */
public class ConfigContract {
    private String multiCallAddress;
    private String nft721Address;
    private String nft1155Address;
    private String transferProxyForDeprecatedAddress;
    private String erc20TransferProxyAddress;
    private String exchangeStateAddress;
    private String exchangeOrdersHolderAddress;
    private String transferProxyAddress;
    private String nftExchangeAddress;



    public String getMultiCallAddress() {
        return multiCallAddress;
    }

    public void setMultiCallAddress(String multiCallAddress) {
        this.multiCallAddress = multiCallAddress;
    }

    public String getNft721Address() {
        return nft721Address;
    }

    public void setNft721Address(String nft721Address) {
        this.nft721Address = nft721Address;
    }

    public String getNft1155Address() {
        return nft1155Address;
    }

    public void setNft1155Address(String nft1155Address) {
        this.nft1155Address = nft1155Address;
    }

    public String getTransferProxyForDeprecatedAddress() {
        return transferProxyForDeprecatedAddress;
    }

    public void setTransferProxyForDeprecatedAddress(String transferProxyForDeprecatedAddress) {
        this.transferProxyForDeprecatedAddress = transferProxyForDeprecatedAddress;
    }

    public String getErc20TransferProxyAddress() {
        return erc20TransferProxyAddress;
    }

    public void setErc20TransferProxyAddress(String erc20TransferProxyAddress) {
        this.erc20TransferProxyAddress = erc20TransferProxyAddress;
    }

    public String getExchangeStateAddress() {
        return exchangeStateAddress;
    }

    public void setExchangeStateAddress(String exchangeStateAddress) {
        this.exchangeStateAddress = exchangeStateAddress;
    }

    public String getExchangeOrdersHolderAddress() {
        return exchangeOrdersHolderAddress;
    }

    public void setExchangeOrdersHolderAddress(String exchangeOrdersHolderAddress) {
        this.exchangeOrdersHolderAddress = exchangeOrdersHolderAddress;
    }

    public String getTransferProxyAddress() {
        return transferProxyAddress;
    }

    public void setTransferProxyAddress(String transferProxyAddress) {
        this.transferProxyAddress = transferProxyAddress;
    }

    public String getNftExchangeAddress() {
        return nftExchangeAddress;
    }

    public void setNftExchangeAddress(String nftExchangeAddress) {
        this.nftExchangeAddress = nftExchangeAddress;
    }

}
