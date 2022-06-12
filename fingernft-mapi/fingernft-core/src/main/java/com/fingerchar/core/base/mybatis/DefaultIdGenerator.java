package com.fingerchar.core.base.mybatis;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIdGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DefaultIdGenerator.class);
	
	public static long lastTimestamp = -1L;
	
	public static long sequence = -1L;
	
	public static long startTime = -1L;
	
	public long workerId = -1L;
	
	public long datacenterId = -1L;
			
	public final long workerIdBits = 5L;
	
	public final long datacenterIdBits = 5L;
	
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
		
	public final long sequenceBits = 12L;
	
	public final long genLeftShift = sequenceBits;
	
	public final long workerIdLeftShift = sequenceBits;
	
	public final long datacenterIdLeftShift = sequenceBits + workerIdBits;
	
	public final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; 
	
	public final long sequenceMask = -1L ^ (-1L << sequenceBits);	
	
	public DefaultIdGenerator(long workerId, long datacenterId) {
		if(workerId < 0 || workerId > maxWorkerId) {
			throw new RuntimeException("workerId不能小于0或者大于" + maxWorkerId);
		}
		if(datacenterId < 0 || datacenterId > maxDatacenterId) {
			throw new RuntimeException("datacenterId不能小于0或者大于" + maxDatacenterId);
		}
		Calendar c = Calendar.getInstance(); 
		c.set(2021, 9, 19, 0, 0, 0);
		startTime = c.getTimeInMillis();
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public synchronized long nextId() {
		long currentTimestamp = this.nextTimestamp();
		if(currentTimestamp < lastTimestamp) {
			logger.error("时钟回拨异常");
			throw new RuntimeException("时钟回拨异常");
		}
		if(lastTimestamp == currentTimestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if(0 == sequence) {
				currentTimestamp = this.nextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = currentTimestamp;
		return ((currentTimestamp - startTime) << timestampLeftShift) | (datacenterId << datacenterIdLeftShift) | (workerId << workerIdLeftShift) | sequence;
	}
	
	private long nextMillis(long lastTimestamp) {
        long time = this.nextTimestamp();
        while (time <= lastTimestamp) {
            time = this.nextTimestamp();
        }
        return time;	
    }
	
	private long nextTimestamp() {
		return System.currentTimeMillis();
	}
}

