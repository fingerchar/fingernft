package com.fingerchar.api.config;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.fingerchar.api.utils.JwtHelper;
import com.fingerchar.core.util.ResponseUtil;

/**
 * Token拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("请求路径： {}", request.getRequestURL());
        String token = request.getHeader("Finger-Nft-Token");
        logger.info("token：{}", token);
        String userAddress = null;
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty((userAddress = JwtHelper.verifyTokenAndGetUserAddress(token)))) {
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
