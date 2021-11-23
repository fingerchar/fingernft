package com.fingerchar.api.service;

import org.apache.commons.lang3.StringUtils;

import com.fingerchar.api.utils.JwtHelper;

public class UserTokenManager {
	
	public static String generateToken( String address) {
        return JwtHelper.createToken(address);
    }
    
	public static String getUserAddress(String token) {
    	String userAddress = JwtHelper.verifyTokenAndGetUserAddress(token);
    	if(StringUtils.isEmpty(userAddress)){
    		return null;
    	}
        return userAddress;
    }
}
