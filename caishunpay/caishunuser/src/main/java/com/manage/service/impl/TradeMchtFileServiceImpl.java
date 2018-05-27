/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.manage.service.impl;

import com.manage.bean.TradeMchtFile;
import com.manage.service.TradeMchtFileService;
import com.tuser.dao.TradeMchtFileDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeMchtFileServiceImpl
        implements TradeMchtFileService {
    @Autowired
    private TradeMchtFileDao tradeMchtFileDaoImpl;

    @Override
    public void delete(TradeMchtFile mchtFile) {
        this.tradeMchtFileDaoImpl.delete(mchtFile);
    }

    @Override
    public TradeMchtFile get(String fileId) {
        return this.tradeMchtFileDaoImpl.get(fileId);
    }

    @Override
    public List<TradeMchtFile> getMchtFiles(String mchtNo) {
        return this.tradeMchtFileDaoImpl.getMchtFiles(mchtNo);
    }

    @Override
    public void save(TradeMchtFile mchtFile) {
        this.tradeMchtFileDaoImpl.save(mchtFile);
    }

    @Override
    public void save(List<TradeMchtFile> mchtFiles) {
        for (TradeMchtFile mchtFile : mchtFiles) {
            this.tradeMchtFileDaoImpl.save(mchtFile);
        }
    }

    @Override
    public void update(TradeMchtFile mchtFile) {
        this.tradeMchtFileDaoImpl.update(mchtFile);
    }
}
