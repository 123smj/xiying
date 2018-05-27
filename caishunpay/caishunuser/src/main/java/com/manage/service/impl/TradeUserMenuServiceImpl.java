/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.manage.service.impl;

import com.manage.bean.MenuBean;
import com.manage.service.TradeUserMenuService;
import com.tuser.bean.TradeUserMenu;
import com.tuser.dao.TradeUserMenuDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeUserMenuServiceImpl
        implements TradeUserMenuService {
    @Autowired
    private TradeUserMenuDao tradeFuncInfDaoImpl;

    @Override
    public TradeUserMenu get(Integer funcId) {
        return this.tradeFuncInfDaoImpl.get(funcId);
    }

    @Override
    public List<TradeUserMenu> getFuncByOprDegree(Integer oprDegree) {
        return this.tradeFuncInfDaoImpl.getFuncByOprDegree(oprDegree);
    }

    @Override
    public List<MenuBean> build(List<TradeUserMenu> funcList) {
        HashMap<Integer, MenuBean> menuMap = new HashMap<Integer, MenuBean>();
        for (TradeUserMenu funcInf : funcList) {
            MenuBean menu = new MenuBean();
            menu.setText(funcInf.getFuncName());
            menu.setId(funcInf.getFuncId());
            menu.setNodeId(String.valueOf(funcInf.getFuncId()));
            menu.setPageUrl(funcInf.getPageUrl());
            if ("0".equals(funcInf.getFuncType())) {
                if (menuMap.get(funcInf.getFuncId()) == null) {
                    menuMap.put(funcInf.getFuncId(), menu);
                    continue;
                }
                ((MenuBean) menuMap.get(funcInf.getFuncId())).setText(funcInf.getFuncName());
                continue;
            }
            if (!"1".equals(funcInf.getFuncType())) continue;
            MenuBean parent = (MenuBean) menuMap.get(funcInf.getFuncParentId());
            if (parent == null) {
                parent = new MenuBean();
                parent.setId(funcInf.getFuncParentId());
                menuMap.put(funcInf.getFuncParentId(), parent);
            }
            if (parent.getNodes() == null) {
                parent.setNodes(new ArrayList<MenuBean>());
            }
            parent.getNodes().add(menu);
        }
        return new ArrayList<MenuBean>(menuMap.values());
    }
}
