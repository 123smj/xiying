/*
 * Decompiled with CFR 0_124.
 */
package com.manage.bean;

import java.util.List;

public class MenuBean {
    private String text;//文本
    private Integer id;
    private String nodeId;//节点ID
    private String pageUrl;//页面路径
    private List<MenuBean> nodes;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPageUrl() {
        return this.pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public List<MenuBean> getNodes() {
        return this.nodes;
    }

    public void setNodes(List<MenuBean> nodes) {
        this.nodes = nodes;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
