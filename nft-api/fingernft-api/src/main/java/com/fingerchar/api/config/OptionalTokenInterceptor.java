package com.fingerchar.api.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fingerchar.api.utils.JwtHelper;

/**
 * 用于非必须Token接口，实现用户ID配置到RequestAttribute
 */
@Component
public class OptionalTokenInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("请求路径： {}", request.getRequestURL());

        String token = request.getHeader("Finger-Nft-Token");
        String userAddress = null;
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty((userAddress = JwtHelper.verifyTokenAndGetUserAddress(token))) ) {
            request.setAttribute("userAddress", userAddress);
        }
        return true;
    }
}
