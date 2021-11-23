package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcUserApply
 * @Author 
 * @Date 2021-11-16
 * @Version 2.1
 */
@TableName("`fc_user_apply`")
public class FcUserApply extends BaseEntity {


    /**
     * 用户区块链地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 用户名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 用户艺术品地址
     */
    @TableField("`art_website`")
    private String artWebsite;

    /**
     * instagram
     */
    @TableField("`instagram`")
    private String instagram;

    /**
     * twitter地址
     */
    @TableField("`twitter`")
    private String twitter;

    /**
     * 其他社交网站或者地址
     */
    @TableField("`social_links`")
    private String socialLinks;

    /**
     * 用户邮箱
     */
    @TableField("`email`")
    private String email;

    /**
     * 艺术品视频地址
     */
    @TableField("`application_video`")
    private String applicationVideo;

    /**
     * 艺术品文件夹
     */
    @TableField("`art_folder`")
    private String artFolder;

    /**
     * 艺术品故事
     */
    @TableField("`art_story`")
    private String artStory;

    /**
     * 知道super rare方式
     */
    @TableField("`kown_superrare_way`")
    private String kownSuperrareWay;

    /**
     * 审核状态
     */
    @TableField("`status`")
    private Integer status;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtWebsite() {
        return artWebsite;
    }

    public void setArtWebsite(String artWebsite) {
        this.artWebsite = artWebsite;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(String socialLinks) {
        this.socialLinks = socialLinks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApplicationVideo() {
        return applicationVideo;
    }

    public void setApplicationVideo(String applicationVideo) {
        this.applicationVideo = applicationVideo;
    }

    public String getArtFolder() {
        return artFolder;
    }

    public void setArtFolder(String artFolder) {
        this.artFolder = artFolder;
    }

    public String getArtStory() {
        return artStory;
    }

    public void setArtStory(String artStory) {
        this.artStory = artStory;
    }

    public String getKownSuperrareWay() {
        return kownSuperrareWay;
    }

    public void setKownSuperrareWay(String kownSuperrareWay) {
        this.kownSuperrareWay = kownSuperrareWay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static final String ADDRESS = "`address`";

    public static final String NAME = "`name`";

    public static final String ART_WEBSITE = "`art_website`";

    public static final String INSTAGRAM = "`instagram`";

    public static final String TWITTER = "`twitter`";

    public static final String SOCIAL_LINKS = "`social_links`";

    public static final String EMAIL = "`email`";

    public static final String APPLICATION_VIDEO = "`application_video`";

    public static final String ART_FOLDER = "`art_folder`";

    public static final String ART_STORY = "`art_story`";

    public static final String KOWN_SUPERRARE_WAY = "`kown_superrare_way`";

    public static final String STATUS = "`status`";

    @Override
    public String toString() {
        return "FcUserApply{" +
        "address=" + address +
        ", name=" + name +
        ", artWebsite=" + artWebsite +
        ", instagram=" + instagram +
        ", twitter=" + twitter +
        ", socialLinks=" + socialLinks +
        ", email=" + email +
        ", applicationVideo=" + applicationVideo +
        ", artFolder=" + artFolder +
        ", artStory=" + artStory +
        ", kownSuperrareWay=" + kownSuperrareWay +
        ", status=" + status +
        "}";
    }
}
