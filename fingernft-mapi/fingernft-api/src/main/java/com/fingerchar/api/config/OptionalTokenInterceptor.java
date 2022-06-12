package com.fingerchar.api.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.utils.JwtHelper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcUserToken;

/**
 * 用于非必须Token接口，实现用户ID配置到RequestAttribute
 */
@Component
public class OptionalTokenInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IBaseService baseService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("请求路径： {}", request.getRequestURL());

        String token = request.getHeader("Finger-Nft-Token");
        String userAddress = null;
        if (!StringUtils.isEmpty(token) && !StringUtils.isEmpty((userAddress = JwtHelper.verifyTokenAndGetUserAddress(token))) ) {
            QueryWrapper<FcUserToken> wrapper = new QueryWrapper<>();
            wrapper.eq(FcUserToken.USER_ADDRESS, userAddress);
            FcUserToken userToken = this.baseService.getByCondition(FcUserToken.class, wrapper);
            if(null != userToken && null != userToken.getUserToken() && userToken.getUserToken().equals(token)) {
            	request.setAttribute("userAddress", userAddress);
            }
        }
        return true;
    }
}
