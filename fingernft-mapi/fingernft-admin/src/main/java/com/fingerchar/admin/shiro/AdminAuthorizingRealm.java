package com.fingerchar.admin.shiro;


import com.alibaba.fastjson.JSON;
import com.fingerchar.admin.service.FcAdminPermissionService;
import com.fingerchar.admin.service.FcAdminRoleService;
import com.fingerchar.admin.service.FcAdminUserService;
import com.fingerchar.core.util.bcrypt.BCryptPasswordEncoder;
import com.fingerchar.db.domain.FcAdminUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

public class AdminAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private FcAdminUserService adminService;
    
    @Autowired
    private FcAdminRoleService roleService;
    
    @Autowired
    private FcAdminPermissionService permissionService;

    /*
    * 授权
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        FcAdminUser admin = (FcAdminUser) getAvailablePrincipal(principals);
        String roleIds = admin.getRoleIds();
        if (StringUtils.isEmpty(roleIds)){
            throw new AuthorizationException("no role.");
        }
        List<Long> roleList = JSON.parseArray(roleIds, Long.class);
        Set<String> roles = roleService.queryByIds(roleList);
        Set<String> permissions = permissionService.queryByRoleIds(roleList);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    /**
     * 验证用户信息
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
