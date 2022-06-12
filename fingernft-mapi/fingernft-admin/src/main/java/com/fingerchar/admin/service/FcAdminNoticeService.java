package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcAdminNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Service
public class FcAdminNoticeService {

	@Autowired
    private IBaseService baseService;

    public IPage<FcAdminNotice> querySelective(String title, String type, Long adminId, IPage<FcAdminNotice> page, boolean isASC, String sortType) {

        QueryWrapper<FcAdminNotice> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(title)) {
            wrapper.eq(FcAdminNotice.NOTICE_TITLE,title);
        }

        if (type.equals("read")) {
            wrapper.isNotNull(FcAdminNotice.READ_TIME);
        } else if (type.equals("unread")) {
            wrapper.isNull(FcAdminNotice.READ_TIME);
        }
        wrapper.eq(FcAdminNotice.ADMIN_ID,adminId);
        wrapper.eq(BaseEntity.DELETED,false);

        if (isASC && !StringUtils.isEmpty(sortType)) {
            wrapper.orderBy(true,isASC,sortType);
        }
        return baseService.findByPage(FcAdminNotice.class,wrapper,page);
    }

    public FcAdminNotice find(Long noticeId, Long adminId) {
        QueryWrapper<FcAdminNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAdminNotice.NOTICE_ID,noticeId)
                .eq(FcAdminNotice.ADMIN_ID,adminId)
                .eq(BaseEntity.DELETED,false);
        return baseService.getByCondition(FcAdminNotice.class,wrapper);
    }

    public void add(FcAdminNotice noticeAdmin) {
        noticeAdmin.setCreateTime(System.currentTimeMillis()/1000);
        noticeAdmin.setUpdateTime(System.currentTimeMillis()/1000);

        baseService.save(noticeAdmin);
    }

    public void update(FcAdminNotice noticeAdmin) {
        noticeAdmin.setUpdateTime(System.currentTimeMillis()/1000);
        baseService.update(noticeAdmin);
    }

    public void markReadByIds(List<Long> ids, Long adminId) {
        FcAdminNotice noticeAdmin = new FcAdminNotice();
        Long now = System.currentTimeMillis()/1000;
        noticeAdmin.setReadTime(now);
        noticeAdmin.setUpdateTime(now);
        UpdateWrapper<FcAdminNotice> wrapper = new UpdateWrapper<>();
        wrapper.in(BaseEntity.ID,ids)
                .eq(FcAdminNotice.ADMIN_ID,adminId)
                .eq(BaseEntity.DELETED,false);
        baseService.updateByCondition(FcAdminNotice.class,noticeAdmin,wrapper);
    }

    public void deleteById(Long id, Long adminId) {
        FcAdminNotice noticeAdmin = new FcAdminNotice();
        noticeAdmin.setUpdateTime(System.currentTimeMillis()/1000);
        noticeAdmin.setDeleted(true);
        UpdateWrapper<FcAdminNotice> wrapper = new UpdateWrapper<>();
        wrapper.eq(BaseEntity.ID,id)
                .eq(FcAdminNotice.ADMIN_ID,adminId)
                .eq(BaseEntity.DELETED,false);
        baseService.updateByCondition(FcAdminNotice.class,noticeAdmin,wrapper);

    }

    public void deleteByIds(List<Long> ids, Long adminId) {
        FcAdminNotice noticeAdmin = new FcAdminNotice();
        noticeAdmin.setUpdateTime(System.currentTimeMillis()/1000);
        noticeAdmin.setDeleted(true);
        UpdateWrapper<FcAdminNotice> wrapper = new UpdateWrapper<>();
        wrapper.in(BaseEntity.ID,ids)
                .eq(FcAdminNotice.ADMIN_ID,adminId)
                .eq(BaseEntity.DELETED,false);
        baseService.updateByCondition(FcAdminNotice.class,noticeAdmin,wrapper);
    }

    public int countUnread(Long adminId) {
        QueryWrapper<FcAdminNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAdminNotice.ADMIN_ID,adminId)
               .isNull(FcAdminNotice.READ_TIME)
               .eq(BaseEntity.DELETED,false);
       return  baseService.counts(FcAdminNotice.class,wrapper);
    }

    public List<FcAdminNotice> queryByNoticeId(Long noticeId) {
        /*FcAdminNoticeExample example = new FcAdminNoticeExample();
        example.or().andNoticeIdEqualTo(noticeId).andDeletedEqualTo(false);
        return noticeAdminMapper.selectByExample(example);*/
        QueryWrapper<FcAdminNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAdminNotice.NOTICE_ID,noticeId)
                .eq(BaseEntity.DELETED,false);
        return  baseService.findByCondition(FcAdminNotice.class,wrapper);

    }

    public void deleteByNoticeId(Long id) {
        /*FcAdminNoticeExample example = new FcAdminNoticeExample();
        example.or().andNoticeIdEqualTo(id).andDeletedEqualTo(false);
        FcAdminNotice noticeAdmin = new FcAdminNotice();
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdmin.setDeleted(true);
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);*/
        UpdateWrapper<FcAdminNotice> wrapper = new UpdateWrapper<>();
        wrapper.eq(FcAdminNotice.NOTICE_ID,id)
                .eq(BaseEntity.DELETED,false);
        FcAdminNotice noticeAdmin = new FcAdminNotice();
        noticeAdmin.setUpdateTime(System.currentTimeMillis()/1000);
        noticeAdmin.setDeleted(true);
        baseService.updateByCondition(FcAdminNotice.class,noticeAdmin,wrapper);
    }

    public void deleteByNoticeIds(List<Long> ids) {
        /*FcAdminNoticeExample example = new FcAdminNoticeExample();
        example.or().andNoticeIdIn(ids).andDeletedEqualTo(false);
        FcAdminNotice noticeAdmin = new FcAdminNotice();
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdmin.setDeleted(true);
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);*/
        UpdateWrapper<FcAdminNotice> wrapper = new UpdateWrapper<>();
        wrapper.in(FcAdminNotice.NOTICE_ID,ids)
                .eq(BaseEntity.DELETED,false);
        FcAdminNotice noticeAdmin = new FcAdminNotice();
        noticeAdmin.setUpdateTime(System.currentTimeMillis()/1000);
        noticeAdmin.setDeleted(true);
        baseService.updateByCondition(FcAdminNotice.class,noticeAdmin,wrapper);
    }

    public int countReadByNoticeId(Long noticeId) {
        /*FcAdminNoticeExample example = new FcAdminNoticeExample();
        example.or().andNoticeIdEqualTo(noticeId).andReadTimeIsNotNull().andDeletedEqualTo(false);
        return (int) noticeAdminMapper.countByExample(example);*/
        QueryWrapper<FcAdminNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAdminNotice.NOTICE_ID,noticeId)
                .isNotNull(FcAdminNotice.READ_TIME)
                .eq(BaseEntity.DELETED,false);
        return  baseService.counts(FcAdminNotice.class,wrapper);
    }

    public void updateByNoticeId(FcAdminNotice noticeAdmin, Long noticeId) {
        /*FcAdminNoticeExample example = new FcAdminNoticeExample();
        example.or().andNoticeIdEqualTo(noticeId).andDeletedEqualTo(false);
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);*/
        UpdateWrapper<FcAdminNotice> wrapper = new UpdateWrapper<>();
        wrapper.eq(FcAdminNotice.NOTICE_ID,noticeId)
                .eq(BaseEntity.DELETED,false);
        noticeAdmin.setUpdateTime(System.currentTimeMillis()/1000);
        baseService.updateByCondition(FcAdminNotice.class,noticeAdmin,wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean readAllUnreadNoticesByAdminId(Long adminId) {
        /*FcAdminNoticeExample noticeAdminExample = new FcAdminNoticeExample();
        noticeAdminExample.createCriteria().andAdminIdEqualTo(adminId).andReadTimeIsNull().andDeletedEqualTo(false);
        List<FcAdminNotice> unreadNoticeList = noticeAdminMapper.selectByExample(noticeAdminExample);
        if (unreadNoticeList.isEmpty()) {
            return true;
        }
        LocalDateTime readTime = LocalDateTime.now();
        for (int i = 0; i < unreadNoticeList.size(); i++) {
            FcAdminNotice notice = unreadNoticeList.get(i);
            notice.setReadTime(readTime);
            noticeAdminMapper.updateByPrimaryKeySelective(notice);
        }
        return true;*/
        QueryWrapper<FcAdminNotice> wrapper = new QueryWrapper<>();
        wrapper.eq(FcAdminNotice.ADMIN_ID,adminId)
                .isNull(FcAdminNotice.READ_TIME)
                .eq(BaseEntity.DELETED,false);
        List<FcAdminNotice> unreadNoticeList = baseService.findByCondition(FcAdminNotice.class, wrapper);
        if (unreadNoticeList.isEmpty()) {
            return true;
        }
        Long readTime = System.currentTimeMillis()/1000;
        for (int i = 0; i < unreadNoticeList.size(); i++) {
            FcAdminNotice notice = unreadNoticeList.get(i);
            notice.setReadTime(readTime);
            baseService.update(notice);
        }
        return true;
    }
}
