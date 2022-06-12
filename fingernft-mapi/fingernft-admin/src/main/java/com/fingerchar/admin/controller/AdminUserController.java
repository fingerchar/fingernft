package com.fingerchar.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.bcrypt.BCryptPasswordEncoder;
import com.fingerchar.db.domain.FcAdminUser;
import com.fingerchar.admin.service.FcAdminUserService;
import com.fingerchar.admin.service.LogHelper;
import com.fingerchar.admin.utils.AdminResponseCode;
import com.fingerchar.admin.vo.AdminUserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/adminuser")
public class AdminUserController  extends BaseController {

    @Autowired
    private FcAdminUserService adminService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:adminuser:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "查询")
    @PostMapping("/list")
    public Object list(String username,
                       String name,
                       String mobile, String sort, String order) {
        IPage<FcAdminUser> iPage = adminService.querySelective(username, mobile, this.getPageInfo(), this.isAsc(order), sort);
        List<FcAdminUser> list = iPage.getRecords();
        List<AdminUserVo> voList = list.stream().map(user->new AdminUserVo(user)).collect(Collectors.toList());
        IPage<AdminUserVo> result = new Page<AdminUserVo>(iPage.getCurrent(), iPage.getSize());
        result.setPages(iPage.getPages());
        result.setRecords(voList);
        result.setTotal(iPage.getTotal());
        return ResponseUtil.okList(result);
    }

    private Object validate(FcAdminUser admin) {
        String name = admin.getUsername();
        if (StringUtils.isEmpty(name)) {
        	return ResponseUtil.fail(-1, "管理员名称不能为空！");
        }
        String password = admin.getPassword();
        if (StringUtils.isEmpty(password) || password.length() < 6) {
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_PASSWORD, "Employee password length cannot be less than 6");
        }
        return null;
    }


    @RequiresPermissions("admin:adminuser:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(AdminUserVo adminVo) {
        FcAdminUser admin = adminVo.toAdminUser();
        Object error = validate(admin);
        if (error != null) {
            return error;
        }

        String username = admin.getUsername();
        List<FcAdminUser> adminList = adminService.findAdmin(username);
        if (adminList.size() > 0) {
            return ResponseUtil.fail(AdminResponseCode.ADMIN_NAME_EXIST, "Employee already exists");
        }

        String rawPassword = admin.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        admin.setPassword(encodedPassword);
        adminService.add(admin);
        logHelper.logAuthSucceed("添加员工", username);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:adminuser:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "详情")
    @PostMapping("/read")
    public Object read(@NotNull Long id) {
    	FcAdminUser admin = adminService.findById(id);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:adminuser:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(AdminUserVo adminVo) {
        FcAdminUser admin = adminVo.toAdminUser();
        Object error = validate(admin);
        if (error != null) {
            return error;
        }

        Long anotherAdminId = admin.getId();
        if (anotherAdminId == null) {
        	return ResponseUtil.fail(-1, "管理员ID不能为空！");
        }

        // 不允许员工通过编辑接口修改密码

        if (!StringUtils.isEmpty(admin.getPassword())) {
            //todo 新传入的密码与旧密码进行比较，如果不一样则更新密码
        	FcAdminUser originAdmin = adminService.findById(admin.getId());
            if (originAdmin != null && originAdmin.getPassword() != null && !originAdmin.getPassword().equals(admin.getPassword())) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String pwd = encoder.encode(admin.getPassword());
                admin.setPassword(pwd);
            }
        }

        if (adminService.updateById(admin) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        logHelper.logAuthSucceed("编辑员工", admin.getUsername());
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:adminuser:setpwd")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "修改密码")
    @PostMapping("/setpwd")
    public Object update(String oldPassword, String newPassword, String newPassword2) {
        if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(newPassword2)) {
            return ResponseUtil.fail(-1, "旧密码, 新密码, 确认密码不能为空！");
        }
        if(!newPassword.equals(newPassword2)) {
            return ResponseUtil.fail(-1, "新密码和确认密码不一致！");
        }
        FcAdminUser user = (FcAdminUser)SecurityUtils.getSubject().getPrincipal();
        if(null == user) {
            return ResponseUtil.unlogin();
        }

        FcAdminUser originAdmin = adminService.findById(user.getId());
        if(null == originAdmin) {
            return ResponseUtil.fail(-1, "用户不存在或者已被禁用");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldPassword, originAdmin.getPassword())) {
            return ResponseUtil.fail(-1, "旧密码不正确");
        }
        if(adminService.updatePwd(encoder.encode(newPassword), originAdmin.getId()) > 0) {
            SecurityUtils.getSubject().logout();;
            logHelper.logAuthSucceed("修改密码", originAdmin.getUsername());
            return ResponseUtil.ok();
        } else {
            logHelper.logAuthFail("修改密码", originAdmin.getUsername());
            return ResponseUtil.fail(-1, "更新失败！");
        }
    }

    @RequiresPermissions("admin:adminuser:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "管理员管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(Long id) {
        Long anotherAdminId = id;
        if (anotherAdminId == null) {
        	return ResponseUtil.fail(-1, "管理员ID不能为空！");
        }

        // 员工不能删除自身账号
        Subject currentUser = SecurityUtils.getSubject();
        FcAdminUser currentAdmin = (FcAdminUser) currentUser.getPrincipal();
        if (currentAdmin.getId().equals(anotherAdminId)) {
            return ResponseUtil.fail(AdminResponseCode.ADMIN_DELETE_NOT_ALLOWED, "Employees cannot delete their own accounts");
        }
        FcAdminUser admin = adminService.findById(anotherAdminId);
        adminService.deleteById(anotherAdminId);
        logHelper.logAuthSucceed("删除员工", admin.getUsername());
        return ResponseUtil.ok();
    }
}
