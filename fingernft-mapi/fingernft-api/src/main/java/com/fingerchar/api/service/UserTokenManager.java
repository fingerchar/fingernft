package com.fingerchar.api.service;

import com.fingerchar.api.utils.JwtHelper;

/**
 * 维护用户token
 */
public class UserTokenManager {
	public static String generateToken( String address) {
        return JwtHelper.createToken(address);
    }
}
