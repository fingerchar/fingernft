package com.fingerchar.shiro;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.fingerchar.domain.FcAdminUser;
import com.fingerchar.service.FcAdminPermissionService;
import com.fingerchar.service.FcAdminRoleService;
import com.fingerchar.service.FcAdminUserService;
import com.fingerchar.utils.bcrypt.BCryptPasswordEncoder;

public class AdminAuthorizingRealm extends AuthorizingRealm {

	@Autowired
	private FcAdminUserService adminService;

	@Autowired
	private FcAdminRoleService roleService;

	@Autowired
	private FcAdminPermissionService permissionService;

	/*
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
		}

		FcAdminUser admin = (FcAdminUser) getAvailablePrincipal(principals);
		String roleIds = admin.getRoleIds();
		if (StringUtils.isEmpty(roleIds)) {
			throw new AuthorizationException("no role.");
		}
		List<Long> roleIdList = JSON.parseArray(roleIds, Long.class);
		Set<String> roles = roleService.queryByIds(roleIdList);
		Set<String> permissions = permissionService.queryByRoleIds(roleIdList);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(roles);
		info.setStringPermissions(permissions);
		return info;
	}

	/**
	 * 验证用户信息
	 * 
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		String password = new String(upToken.getPassword());

		if (StringUtils.isEmpty(username)) {
			throw new AccountException("用户名不能为空");
		}
		if (StringUtils.isEmpty(password)) {
			throw new AccountException("密码不能为空");
		}

		List<FcAdminUser> adminList = adminService.findAdmin(username);
		Assert.state(adminList.size() < 2, "同一个用户名存在两个账户");
		if (adminList.size() == 0) {
			throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
		}
		FcAdminUser admin = adminList.get(0);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (!encoder.matches(password, admin.getPassword())) {
			throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
		}

		return new SimpleAuthenticationInfo(admin, password, getName());
	}

}
