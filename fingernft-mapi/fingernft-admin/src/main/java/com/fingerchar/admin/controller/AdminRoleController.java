package com.fingerchar.admin.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcAdminPermission;
import com.fingerchar.db.domain.FcAdminRole;
import com.fingerchar.db.domain.FcAdminUser;
import com.fingerchar.admin.service.FcAdminPermissionService;
import com.fingerchar.admin.service.FcAdminRoleService;
import com.fingerchar.admin.service.FcAdminUserService;
import com.fingerchar.admin.utils.AdminResponseCode;
import com.fingerchar.admin.utils.Permission;
import com.fingerchar.admin.utils.PermissionUtil;
import com.fingerchar.admin.vo.PermVo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/admin/role")
public class AdminRoleController  extends BaseController {


    public static final String ADMIN_PACKAGE = "com.fingerchar.admin";

    @Autowired
    private FcAdminRoleService roleService;
    @Autowired
    private FcAdminPermissionService permissionService;
    @Autowired
    private FcAdminUserService adminService;

    @RequiresPermissions("admin:role:list")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色查询")
    @PostMapping("/list")
    public Object list(String name, String sort, String order) {
        IPage<FcAdminRole> iPage = roleService.querySelective(name, this.getPageInfo(), this.isAsc(order), sort);
        return ResponseUtil.okList(iPage);
    }

    @PostMapping("/options")
    public Object options() {
        List<FcAdminRole> roleList = roleService.queryAll();

        List<Map<String, Object>> options = new ArrayList<>(roleList.size());
        for (FcAdminRole role : roleList) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", role.getId());
            option.put("label", role.getName());
            options.add(option);
        }

        return ResponseUtil.okList(options,null);
    }

    @RequiresPermissions("admin:role:read")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色详情")
    @PostMapping("/read")
    public Object read(@NotNull Long id) {
        FcAdminRole role = roleService.findById(id);
        return ResponseUtil.ok(role);
    }

    //名称不能为空
    private Object validate(FcAdminRole role) {
        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
        	return ResponseUtil.fail(-1, "角色名称不能为空！");
        }

        return null;
    }

    @RequiresPermissions("admin:role:create")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色添加")
    @PostMapping("/create")
    public Object create(FcAdminRole role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }

        if (roleService.checkExist(role.getName())) {
            return ResponseUtil.fail(AdminResponseCode.ROLE_NAME_EXIST, "Role already exists");
        }

        roleService.add(role);

        return ResponseUtil.ok(role);
    }

    @RequiresPermissions("admin:role:update")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色编辑")
    @PostMapping("/update")
    public Object update(FcAdminRole role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }

        roleService.updateById(role);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:role:delete")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "角色删除")
    @PostMapping("/delete")
    public Object delete(Long id) {
        if (id == null) {
        	return ResponseUtil.fail(-1, "角色ID不能为空！");
        }

        // 如果当前角色所对应管理员仍存在，则拒绝删除角色。
        List<FcAdminUser> adminList = adminService.all();
        for (FcAdminUser admin : adminList) {
            String roleIds = admin.getRoleIds();
            if (StringUtils.isEmpty(roleIds)){
                throw new AuthorizationException("no role.");
            }
            List<Long> roleIdList = JSON.parseArray(roleIds, Long.class);
            for (Long roleId : roleIdList) {
                if (id.equals(roleId)) {
                    return ResponseUtil.fail(AdminResponseCode.ROLE_USER_EXIST, "There is an administrator in the current role, which cannot be deleted");
                }
            }
        }

        roleService.deleteById(id);
        return ResponseUtil.ok();
    }


    @Autowired
    private ApplicationContext context;
    private List<PermVo> systemPermissions = null;
    private Set<String> systemPermissionsString = null;

    /*
    * 扫描包路径下的方法是否有注解
    * */
    private List<PermVo> getSystemPermissions() {
        final String adminPackage = ADMIN_PACKAGE;
        if (systemPermissions == null) {
            List<Permission> permissions = PermissionUtil.listPermission(context, adminPackage);
            systemPermissions = PermissionUtil.listPermVo(permissions);
            systemPermissionsString = PermissionUtil.listPermissionString(permissions);
        }
        return systemPermissions;
    }

    /*
    * 获取指定管理员角色的权限
    * */
    private Set<String> getAssignedPermissions(Long roleId) {
        // 这里需要注意的是，如果存在超级权限*，那么这里需要转化成当前所有系统权限。
        // 之所以这么做，是因为前端不能识别超级权限，所以这里需要转换一下。
        Set<String> assignedPermissions = null;
        if (permissionService.checkSuperPermission(roleId)) {
            getSystemPermissions();
            assignedPermissions = systemPermissionsString;
        } else {
            assignedPermissions = permissionService.queryByRoleId(roleId);
        }

        return assignedPermissions;
    }

    /**
     * 管理员的权限情况
     *
     * @return 系统所有权限列表和管理员已分配权限
     */
    @RequiresPermissions("admin:role:permission:getpermissions")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "权限详情")
    @PostMapping("/getpermissions")
    public Object getPermissions(Long roleId) {
        List<PermVo> systemPermissions = getSystemPermissions();
        Set<String> assignedPermissions = getAssignedPermissions(roleId);
        Map<String, Object> data = new HashMap<>();
        data.put("systemPermissions", systemPermissions);
        data.put("assignedPermissions", assignedPermissions);
        return ResponseUtil.ok(data);
    }


    /**
     * 更新管理员的权限
     *
     * @return
     */
    @RequiresPermissions("admin:role:permission:updatepermissions")
    @RequiresPermissionsDesc(menu = {"系统管理", "角色管理"}, button = "权限变更")
    @PostMapping("/updatepermissions")
    public Object updatePermissions(String[] permissions, Long roleId) {
        //Long roleId = JacksonUtils.parseLong(body, "roleId");
        //List<String> permissions = JacksonUtils.parseStringList(body, "permissions");
        if (roleId == null || permissions == null|| permissions.length <=0) {
        	return ResponseUtil.fail(-1, "角色ID,授权信息不能为空！");
        }

        // 如果修改的角色是超级权限，则拒绝修改。
        if (permissionService.checkSuperPermission(roleId)) {
            return ResponseUtil.fail(AdminResponseCode.ROLE_SUPER_SUPERMISSION, "The super permission of the current role cannot be changed");
        }

        // 先删除旧的权限，再更新新的权限
        permissionService.deleteByRoleId(roleId);
        for (String permission : permissions) {
            FcAdminPermission FcAdminPermission = new FcAdminPermission();
            FcAdminPermission.setRoleId(roleId);
            FcAdminPermission.setPermission(permission);
            permissionService.add(FcAdminPermission);
        }
        return ResponseUtil.ok();
    }

}
