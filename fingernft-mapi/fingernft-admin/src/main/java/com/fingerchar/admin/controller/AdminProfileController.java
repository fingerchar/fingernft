package com.fingerchar.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.bcrypt.BCryptPasswordEncoder;
import com.fingerchar.db.domain.FcAdminNotice;
import com.fingerchar.db.domain.FcAdminUser;
import com.fingerchar.admin.service.FcAdminNoticeService;
import com.fingerchar.admin.service.FcAdminUserService;
import com.fingerchar.admin.utils.AdminResponseCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/profile")
public class AdminProfileController extends BaseController {

    @Autowired
    private FcAdminUserService adminService;
    @Autowired
    private FcAdminNoticeService noticeAdminService;

    @RequiresAuthentication
    @PostMapping("/password")
    public Object create(@RequestBody String body) {
    	JSONObject obj = JSONObject.parseObject(body);
        String oldPassword = obj.getString("oldPassword");
        String newPassword = obj.getString("newPassword");
        if (StringUtils.isEmpty(oldPassword)) {
        	return ResponseUtil.fail(-1, "旧密码不能为空！");
        }
        if (StringUtils.isEmpty(newPassword)) {
        	return ResponseUtil.fail(-1, "新密码不能为空！");
        }

        Subject currentUser = SecurityUtils.getSubject();
        FcAdminUser admin = (FcAdminUser) currentUser.getPrincipal();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, admin.getPassword())) {
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_ACCOUNT, "账号密码不对");
        }

        String encodedNewPassword = encoder.encode(newPassword);
        admin.setPassword(encodedNewPassword);

        adminService.updateById(admin);
        return ResponseUtil.ok();
    }

    private Long getAdminId() {
        Subject currentUser = SecurityUtils.getSubject();
        FcAdminUser admin = (FcAdminUser) currentUser.getPrincipal();
        if(admin!=null){
            return admin.getId();
        }else {
            return 0L;
        }

    }

    @PostMapping("/nnotice")
    public Object nNotice() {
        int count =0;
        if(!getAdminId().equals(0L)){
             count = noticeAdminService.countUnread(getAdminId());
        }
        return ResponseUtil.ok(count);
    }

    @RequiresAuthentication
    @PostMapping("/lsnotice")
    public Object lsNotice(String title, String type,
                           String sort,
                           String order) {
        IPage<FcAdminNotice> iPage = noticeAdminService.querySelective(title, type, getAdminId(), this.getPageInfo(), this.isAsc(order), sort);
        return ResponseUtil.okList(iPage);
    }

    @RequiresAuthentication
    @PostMapping("/catnotice")
    public Object catNotice(@RequestBody String body) {
    	JSONObject obj = JSONObject.parseObject(body);
        Long noticeId = obj.getLong("noticeId");
        if (noticeId == null) {
            return ResponseUtil.badArgument();
        }

        FcAdminNotice noticeAdmin = noticeAdminService.find(noticeId, getAdminId());
        if (noticeAdmin == null) {
            return ResponseUtil.badArgumentValue();
        }
        // 更新通知记录中的时间
        noticeAdmin.setReadTime(System.currentTimeMillis() / 1000);
        noticeAdminService.update(noticeAdmin);

        // 返回通知的相关信息
        Map<String, Object> data = new HashMap<>();

        if (noticeAdmin.getAdminId().equals(0L)) {
            data.put("admin", "系统");
        } else {
            FcAdminUser admin = adminService.findById(noticeAdmin.getAdminId());
            data.put("admin", admin.getUsername());
            data.put("avatar", admin.getAvatar());
        }
        return ResponseUtil.ok(data);
    }

    @RequiresAuthentication
    @PostMapping("/bcatnotice")
    public Object bcatNotice(@RequestBody String body) {
    	JSONObject obj = JSONObject.parseObject(body);
        List<Long> ids = JSON.parseArray(obj.getString("ids"), Long.class);
        noticeAdminService.markReadByIds(ids, getAdminId());
        return ResponseUtil.ok();
    }

    @RequiresAuthentication
    @PostMapping("/rmnotice")
    public Object rmNotice(@RequestBody String body) {
    	JSONObject obj = JSONObject.parseObject(body);
        Long id = obj.getLong("id");
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        noticeAdminService.deleteById(id, getAdminId());
        return ResponseUtil.ok();
    }

    @RequiresAuthentication
    @PostMapping("/brmnotice")
    public Object brmNotice(@RequestBody String body) {
    	JSONObject obj = JSONObject.parseObject(body);
        List<Long> ids = JSON.parseArray(obj.getString("ids"), Long.class);
        noticeAdminService.deleteByIds(ids, getAdminId());
        return ResponseUtil.ok();
    }


    @RequiresAuthentication
    @PostMapping("/avatar")
    public Object updateAvatar(@RequestBody Map<String, String> params) {
        String avatarUrl = params.get("avatar");
        if (StringUtils.isEmpty(avatarUrl)) {
            return ResponseUtil.badArgumentValue();
        }
        FcAdminUser admin = (FcAdminUser) SecurityUtils.getSubject().getPrincipal();
        if (admin == null) {
            return ResponseUtil.fail();
        }
        admin.setAvatar(avatarUrl);
        if (adminService.updateById(admin) == 0) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }


    @RequiresAuthentication
    @PostMapping("/readAll")
    public Object makeAllUnreadNotification2Read() {
        //todo 1. 获取当前用户
        FcAdminUser currentUser = (FcAdminUser) SecurityUtils.getSubject().getPrincipal();

        //todo 2. 将当前用户所有未读的通知置为已读
        noticeAdminService.readAllUnreadNoticesByAdminId(currentUser.getId());

        return ResponseUtil.ok();
    }

}
