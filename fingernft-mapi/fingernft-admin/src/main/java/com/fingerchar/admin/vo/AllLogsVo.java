package com.fingerchar.admin.vo;

import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;

public class AllLogsVo {
    private List<EthLog.LogResult> allLogs;
    private BigInteger end;

    public AllLogsVo(List<EthLog.LogResult> allLogs, BigInteger end) {
        this.allLogs = allLogs;
        this.end = end;
    }

    public List<EthLog.LogResult> getAllLogs() {
        return allLogs;
    }

    public void setAllLogs(List<EthLog.LogResult> allLogs) {
        this.allLogs = allLogs;
    }

    public BigInteger getEnd() {
        return end;
    }

    public void setEnd(BigInteger end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "AllLogsVo{" +
                "allLogs=" + allLogs +
                ", end=" + end +
                '}';
    }
}
