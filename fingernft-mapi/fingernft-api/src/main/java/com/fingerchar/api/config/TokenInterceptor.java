package com.fingerchar.api.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fingerchar.core.constant.SysConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.utils.JwtHelper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUserToken;

/**
 * Token拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    IBaseService baseService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("请求路径： {}", request.getRequestURL());
        String token = request.getHeader(SysConstant.WEB_TOKEN_NAME);
        logger.info("token：{}", token);
        String userAddress = null;
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty((userAddress = JwtHelper.verifyTokenAndGetUserAddress(token)))) {
            this.unLogin(request, response);
            return false;
        }
        QueryWrapper<FcUserToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcUserToken.USER_ADDRESS, userAddress);
        FcUserToken userToken = this.baseService.getByCondition(FcUserToken.class, wrapper);
        if(null == userToken || null == userToken.getUserToken() || !userToken.getUserToken().equals(token)) {
        	this.unLogin(request, response);
            return false;
        }
        request.setAttribute("userAddress", userAddress);
        return true;
    }

    private void unLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object obj = ResponseUtil.unlogin();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(obj));
    }
}
