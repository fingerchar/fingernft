package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAdminNotice
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_admin_notice")
public class FcAdminNotice extends BaseEntity {


    /**
     * 通知ID
     */
    @TableField("`notice_id`")
    private Long noticeId;

    /**
     * 通知标题
     */
    @TableField("`notice_title`")
    private String noticeTitle;

    /**
     * 接收通知的管理员ID
     */
    @TableField("`admin_id`")
    private Long adminId;

    @TableField("`read_time`")
    private Long readTime;


    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getReadTime() {
        return readTime;
    }

    public void setReadTime(Long readTime) {
        this.readTime = readTime;
    }

    public static final String NOTICE_ID = "`notice_id`";

    public static final String NOTICE_TITLE = "`notice_title`";

    public static final String ADMIN_ID = "`admin_id`";

    public static final String READ_TIME = "`read_time`";

    @Override
    public String toString() {
        return "FcAdminNotice{" +
        "noticeId=" + noticeId +
        ", noticeTitle=" + noticeTitle +
        ", adminId=" + adminId +
        ", readTime=" + readTime +
        "}";
    }
}
