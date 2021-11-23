package com.fingerchar.api.dto;

public class TxParamsInfo {

	private String txFrom;
	
	private String txTo;
	
	private String txHash;
	
	private Long txNonce;
	
	private String txInput;

	public String getTxFrom() {
		return txFrom;
	}

	public void setTxFrom(String txFrom) {
		this.txFrom = txFrom;
	}

	public String getTxTo() {
		return txTo;
	}

	public void setTxTo(String txTo) {
		this.txTo = txTo;
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public Long getTxNonce() {
		return txNonce;
	}

	public void setTxNonce(Long txNonce) {
		this.txNonce = txNonce;
	}

	public String getTxInput() {
		return txInput;
	}

	public void setTxInput(String txInput) {
		this.txInput = txInput;
	}
}
