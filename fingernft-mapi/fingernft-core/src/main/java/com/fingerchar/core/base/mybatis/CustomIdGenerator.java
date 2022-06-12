package com.fingerchar.core.base.mybatis;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

@Component
public class CustomIdGenerator implements IdentifierGenerator {

	private static long workerId = 0;

	private static long datacenterId = 0;
	
	private static final DefaultIdGenerator defaultIdGenerator = new DefaultIdGenerator(workerId, datacenterId); 

	
	@Override
	public Number nextId(Object entity) {
		return defaultIdGenerator.nextId();
	}

}
