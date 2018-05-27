/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.manage.service.impl;

import com.manage.bean.MenuBean;
import com.manage.bean.TradeFuncInf;
import com.manage.dao.TradeFuncInfDao;
import com.manage.service.TradeFuncInfService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeFuncInfServiceImpl
implements TradeFuncInfService {
    @Autowired
    private TradeFuncInfDao tradeFuncInfDaoImpl;

    @Override
    public TradeFuncInf get(Integer funcId) {
        return this.tradeFuncInfDaoImpl.get(funcId);
    }

    @Override
    public List<TradeFuncInf> getFuncByOprDegree(Integer oprDegree) {
        return this.tradeFuncInfDaoImpl.getFuncByOprDegree(oprDegree);
    }

    @Override
    public List<MenuBean> build(List<TradeFuncInf> funcList) {
        HashMap<Integer, MenuBean> menuMap = new HashMap<Integer, MenuBean>();
        for (TradeFuncInf funcInf : funcList) {
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
                ((MenuBean)menuMap.get(funcInf.getFuncId())).setText(funcInf.getFuncName());
                continue;
            }
            if (!"1".equals(funcInf.getFuncType())) continue;
            MenuBean parent = (MenuBean)menuMap.get(funcInf.getFuncParentId());
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

    @Override
    public HashSet<String> getAuthSet(List<TradeFuncInf> funcList) {
        HashSet<String> set = new HashSet<String>();
        for (TradeFuncInf funcInf : funcList) {
            if (!"1".equals(funcInf.getFuncType())) continue;
            set.add(funcInf.getPageUrl());
        }
        return set;
    }
}
