package com.fingerchar.db.vo.notice;

import com.fingerchar.db.domain.FcNotice;
import com.fingerchar.db.vo.UserBaseInfoVo;

/**
 * @Author： Zjm
 * @Date：2022/4/1 15:06
 */
public class NoticeInfoVo {
    private Long id;
    private Integer type;
    private Integer subType;
    private String owner;
    private UserBaseInfoVo operator;
    private Boolean isRead;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    private Long createTime;
    private NoticeContentVo content;

    public NoticeInfoVo(FcNotice notice, UserBaseInfoVo operator, NoticeContentVo content){
        this.id = notice.getId();
        this.type = notice.getType();
        this.subType = notice.getSubType();
        this.owner = notice.getOwner();
        this.operator = operator;
        this.isRead = notice.getIsRead();
        this.createTime = notice.getCreateTime();
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public UserBaseInfoVo getOperator() {
        return operator;
    }

    public void setOperator(UserBaseInfoVo operator) {
        this.operator = operator;
    }

    public NoticeContentVo getContent() {
        return content;
    }

    public void setContent(NoticeContentVo content) {
        this.content = content;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
