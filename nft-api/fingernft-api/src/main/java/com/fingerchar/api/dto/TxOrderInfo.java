package com.fingerchar.api.dto;

import com.fingerchar.db.domain.FcTxOrder;

public class TxOrderInfo extends OrderInterface {
	
	private String txHash;
	
	public FcTxOrder toFcOrder() {
		FcTxOrder txOrder = new FcTxOrder();
		txOrder.setTxHash(this.txHash);
		return txOrder;
	}

	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

}
