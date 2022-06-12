package com.fingerchar.api.config;

import com.fingerchar.core.constant.SysConfConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FcWebMvcConfiguration implements WebMvcConfigurer {

	@Autowired
	private TokenInterceptor tokenInterceptor;
	
	@Autowired
	private OptionalTokenInterceptor optionalTokenInterceptor;

	private static final String[] NOT_NEED_LOGIN_PATH = { 
			SysConfConstant.URL_PREFIX + "/user/login",
			SysConfConstant.URL_PREFIX + "/user/reload",
			SysConfConstant.URL_PREFIX + "/user/listbyaddr", 
			SysConfConstant.URL_PREFIX + "/user/follows",
			SysConfConstant.URL_PREFIX + "/user/collections",
			SysConfConstant.URL_PREFIX + "/user/nftlist",
			SysConfConstant.URL_PREFIX + "/user/stat",
			SysConfConstant.URL_PREFIX + "/user/info", 
			SysConfConstant.URL_PREFIX + "/user/onsales",
			SysConfConstant.URL_PREFIX + "/user/like", 
			SysConfConstant.URL_PREFIX + "/user/created",
			SysConfConstant.URL_PREFIX + "/user/white",
			SysConfConstant.URL_PREFIX + "/user/listcontract",
			SysConfConstant.URL_PREFIX + "/follow/match",
			SysConfConstant.URL_PREFIX + "/follow/follows",
			SysConfConstant.URL_PREFIX + "/follow/followers",
			SysConfConstant.URL_PREFIX + "/category/list",
			SysConfConstant.URL_PREFIX + "/contract/info", 
			SysConfConstant.URL_PREFIX + "/contract/getinfo",
			SysConfConstant.URL_PREFIX + "/contract/stat",
			SysConfConstant.URL_PREFIX + "/contract/list",
			SysConfConstant.URL_PREFIX + "/contract/all",
			SysConfConstant.URL_PREFIX + "/contract/listbyaddr",
			SysConfConstant.URL_PREFIX + "/contract/onsales",
			SysConfConstant.URL_PREFIX + "/contract/collections", 
			SysConfConstant.URL_PREFIX + "/contract/listitems",
			SysConfConstant.URL_PREFIX + "/contract/listall",
			SysConfConstant.URL_PREFIX + "/paytoken/list",
			SysConfConstant.URL_PREFIX + "/home/list",
			SysConfConstant.URL_PREFIX + "/home/indexlist",
			SysConfConstant.URL_PREFIX + "/like/listuserlike", 
			SysConfConstant.URL_PREFIX + "/config/fetch",
      		SysConfConstant.URL_PREFIX + "/config/gasTracker",
			SysConfConstant.URL_PREFIX + "/home/search", 
			SysConfConstant.URL_PREFIX + "/home/searchuser",
			SysConfConstant.URL_PREFIX + "/nft/owners", 
			SysConfConstant.URL_PREFIX + "/nft/bids",
			SysConfConstant.URL_PREFIX + "/nft/history", 
			SysConfConstant.URL_PREFIX + "/nft/detail",
			SysConfConstant.URL_PREFIX + "/nft/activebids",
			SysConfConstant.URL_PREFIX + "/nft/activesales",
			SysConfConstant.URL_PREFIX + "/nft/getmedia",
			SysConfConstant.URL_PREFIX + "/nft/getroyalties", 
			SysConfConstant.URL_PREFIX + "/order/get",
			SysConfConstant.URL_PREFIX + "/notices/countunread",
			SysConfConstant.URL_PREFIX + "/notices/list",
			SysConfConstant.URL_PREFIX + "/notices/count", 
			"/upload/*",
			"/static/*"
	};

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 指定必选token的接口（需要登录）
		registry.addInterceptor(tokenInterceptor).excludePathPatterns(NOT_NEED_LOGIN_PATH);
		// 可选token接口（可不登录）
		registry.addInterceptor(optionalTokenInterceptor);
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry register){
		register.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/upload/");
	}
}
