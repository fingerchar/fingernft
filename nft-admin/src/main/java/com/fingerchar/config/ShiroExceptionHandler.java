package com.fingerchar.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fingerchar.utils.ResponseUtil;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ShiroExceptionHandler {

    private final Log logger = LogFactory.getLog(ShiroExceptionHandler.class);
    @Autowired
    HttpServletRequest request;

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public Object unauthenticatedHandler(AuthenticationException e) {
        logger.info(request.getRequestURL());
        logger.warn(e.getMessage(), e);
        return ResponseUtil.unlogin();
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Object unauthorizedHandler(AuthorizationException e) {
        logger.info(request.getRequestURL());
        logger.warn(e.getMessage(), e);
        return ResponseUtil.unauthz();
    }

}
