/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.trade.service.impl;

import com.manage.bean.PageModle;
import com.trade.bean.own.JumpListBean;
import com.trade.dao.JumpListDao;
import com.trade.service.JumpListService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 跳码列表
 */
@Service
public class JumpListServiceImpl implements JumpListService {
    @Autowired
    private JumpListDao jumpListDaoImpl;

    @Override
    public void delet(JumpListBean jumpListBean) {
        this.jumpListDaoImpl.delet(jumpListBean);
    }

    @Override
    public JumpListBean getJumpList(String jump_group, String channel_id, String channel_mcht_no, String trade_source) {
        return this.jumpListDaoImpl.getJumpList(jump_group, channel_id, channel_mcht_no, trade_source);
    }

    @Override
    public JumpListBean getFirstJumpList(String jumpGroup) {
        return this.jumpListDaoImpl.getFirstJumpList(jumpGroup);
    }

    @Override
    public List<JumpListBean> getJumpList(String jumpGroup, String tradeSource) {
        return this.jumpListDaoImpl.getJumpList(jumpGroup, tradeSource);
    }

    @Override
    public List<Map<String, String>> listAllJumpgroup(Map<String, String> param) {
        return this.jumpListDaoImpl.listAllJumpgroup(param);
    }

    @Override
    public PageModle<Map<String, String>> listGroupByPage(Map<String, String> param, int pageNum, int perPageNum) {
        return this.jumpListDaoImpl.listGroupByPage(param, pageNum, perPageNum);
    }

    @Override
    public void save(JumpListBean jumpListBean) {
        this.jumpListDaoImpl.save(jumpListBean);
    }

    @Override
    public void update(JumpListBean jumpListBean) {
        this.jumpListDaoImpl.update(jumpListBean);
    }
}
