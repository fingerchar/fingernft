package com.fingerchar.controller;


import com.alibaba.fastjson.JSON;
import com.fingerchar.base.controller.BaseController;
import com.fingerchar.domain.FcAdminToken;
import com.fingerchar.domain.FcAdminUser;
import com.fingerchar.service.*;
import com.fingerchar.utils.*;
import com.google.code.kaptcha.Producer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController extends BaseController {
	
    private final Log logger = LogFactory.getLog(AdminAuthController.class);

    @Autowired
    private FcAdminUserService adminService;

    @Autowired
    private FcAdminRoleService roleService;

    @Autowired
    private FcAdminPermissionService permissionService;

    @Autowired
    private LogHelper logHelper;

    @Autowired
    private FcAdminTokenService adminTokenService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Producer kaptchaProducer;

    private HashMap<String, String> systemPermissionsMap = null;

    @PostMapping("/login")
    public Object login(String username, String password, String code) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password )) {
            return ResponseUtil.badArgument();
        }

         if (StringUtils.isEmpty(code)) {
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_KAPTCHA_REQUIRED, "The verification code cannot be empty");
        }
       HttpSession session = request.getSession();
        String kaptcha = (String)session.getAttribute("kaptcha");
        if (Objects.requireNonNull(code).compareToIgnoreCase(kaptcha) != 0) {
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_KAPTCHA, "Verification code is not correct",doKaptcha(request));
        }
        /*
        *获取当前用户的登录信息
        * 调用doGetAuthenticationInfo
        * */
        Subject currentUser = SecurityUtils.getSubject();

        try {
            currentUser.login(new UsernamePasswordToken(username, password));
            FcAdminUser loginUser = (FcAdminUser) currentUser.getPrincipal();
            //当用户状态为1时表示禁用，用户不能登录
            if (loginUser != null && loginUser.getStatus() == true) {
                throw new LockedAccountException();
            }
        } catch (UnknownAccountException uae) {
            logHelper.logAuthFail("登录", "用户帐号或密码不正确");
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_ACCOUNT, "User name or password is incorrect");
        } catch (LockedAccountException lae) {
            logHelper.logAuthFail("登录", "用户帐号已锁定不可用");
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_ACCOUNT_LOCKED, "The user account is locked and unavailable");

        } catch (AuthenticationException ae) {
            logHelper.logAuthFail("登录", "认证失败");
            ae.printStackTrace();
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_ACCOUNT_FAILED, "Authentication failed");
        }

        currentUser = SecurityUtils.getSubject();
        FcAdminUser admin = (FcAdminUser) currentUser.getPrincipal();
        admin.setLastLoginIp(IpUtil.getIpAddr(request));
        admin.setLastLoginTime(System.currentTimeMillis() / 1000);
        adminService.updateById(admin);

        logHelper.logAuthSucceed("登录");

        //todo 记录用户上线 && 同时，在用户手动退出（仅通过退出按钮）时才判定为下线！！！
        logger.info("token：" + currentUser.getSession().getId());
        FcAdminToken adminToken = new FcAdminToken();
        adminToken.setUserId(admin.getId());
        adminToken.setKey("" + currentUser.getSession().getId());
        adminToken.setStatus(1);
        if (!adminTokenService.addOrUpdateAdminToken(adminToken)) {
            return ResponseUtil.fail(502, "Failed to record or update online record");
        }
        // userInfo
        Map<String, Object> adminInfo = new HashMap<String, Object>();
        adminInfo.put("nickName", admin.getUsername());
        adminInfo.put("avatar", admin.getAvatar());
        adminInfo.put("id",admin.getId()); //管理员主键id

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", currentUser.getSession().getId());
        result.put("adminInfo", adminInfo);

        return ResponseUtil.ok(result);
    }

    /*
     *
     */
    @RequiresAuthentication
    @PostMapping("/logout")
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();
        logHelper.logAuthSucceed("退出");
        //todo 使用退出按钮标记用户下线
        Object adminObj = SecurityUtils.getSubject().getPrincipal();
        FcAdminUser admin = null;
        if (adminObj instanceof FcAdminUser) {
            admin = (FcAdminUser) adminObj;
        }
        FcAdminToken adminToken = adminTokenService.findByUserId(admin.getId());
        if (adminToken != null) {
            adminToken.setKey(null);
        } else {
            adminToken = new FcAdminToken();
            if (admin != null) {
                adminToken.setUserId(admin.getId());
            }
        }
        adminToken.setUpdateTime(System.currentTimeMillis() / 1000);
        adminToken.setStatus(0);
        adminToken.setKey("");
        if (adminTokenService.addOrUpdateAdminToken(adminToken)) {
            currentUser.logout();
            return ResponseUtil.ok();
        }
        return ResponseUtil.fail(503, "Failed to update user exit record");
    }


    @RequiresAuthentication
    @PostMapping("/info")
    public Object info() {
        Subject currentUser = SecurityUtils.getSubject();
        FcAdminUser admin = (FcAdminUser) currentUser.getPrincipal();
        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId());
        data.put("name", admin.getUsername());
        data.put("avatar", admin.getAvatar());
        String roleIds = admin.getRoleIds();
        if (StringUtils.isEmpty(roleIds)){
            throw new AuthorizationException("no role.");
        }
        List<Long> roleIdList = JSON.parseArray(roleIds, Long.class);
        
        Set<String> roles = roleService.queryByIds(roleIdList);
        Set<String> permissions = permissionService.queryByRoleIds(roleIdList);
        data.put("roles_id", roleIds);
        data.put("roles", roles);
        // NOTE
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        data.put("perms", toApi(permissions));
        return ResponseUtil.ok(data);
    }

    private Collection<String> toApi(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.fingerchar.controller";
            List<Permission> systemPermissions = PermissionUtil.listPermission(context, basicPackage);
            for (Permission permission : systemPermissions) {
                String perm = permission.getRequiresPermissions().value()[0];
                String api = permission.getApi();
                systemPermissionsMap.put(perm, api);
            }
        }

        Collection<String> apis = new HashSet<>();
        for (String perm : permissions) {
            String api = systemPermissionsMap.get(perm);
            apis.add(api);

            if (perm.equals("*")) {
                apis.clear();
                apis.add("*");
                return apis;
                //                return systemPermissionsMap.values();

            }
        }
        return apis;
    }

    @PostMapping("/kaptcha")
    public Object kaptcha(HttpServletRequest request) {
        String kaptcha = doKaptcha(request);
        if (kaptcha != null) {
            return ResponseUtil.ok(kaptcha);
        }
        return ResponseUtil.fail();
    }

    private String doKaptcha(HttpServletRequest request) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        HttpSession session = request.getSession();
        session.setAttribute("kaptcha", text);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            return "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
        } catch (IOException e) {
            return null;
        }
    }

    @PostMapping("/401")
    public Object page401() {
        return ResponseUtil.unlogin();
    }

    @PostMapping("/index")
    public Object pageIndex() {
        return ResponseUtil.ok();
    }

    @PostMapping("/403")
    public Object page403() {
        return ResponseUtil.unauthz();
    }
}
