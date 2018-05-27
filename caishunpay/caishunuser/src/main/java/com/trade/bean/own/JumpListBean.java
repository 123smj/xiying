/*
 * Decompiled with CFR 0_124.
 */
package com.trade.bean.own;

import java.io.Serializable;

public class JumpListBean
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channel_id;
    private String channel_mcht_no;
    private String jump_group;
    private String trade_source;
    private int weight;

    public JumpListBean(String jump_group, String trade_source, String channelId, String channelMchtNo) {
        this.jump_group = jump_group;
        this.trade_source = trade_source;
        this.channel_id = channelId;
        this.channel_mcht_no = channelMchtNo;
    }

    public JumpListBean(String jump_group, String trade_source) {
        this.jump_group = jump_group;
        this.trade_source = trade_source;
    }

    public JumpListBean() {
    }

    public String getChannel_id() {
        return this.channel_id;
    }

    public void setChannel_id(String channelId) {
        this.channel_id = channelId;
    }

    public String getChannel_mcht_no() {
        return this.channel_mcht_no;
    }

    public void setChannel_mcht_no(String channelMchtNo) {
        this.channel_mcht_no = channelMchtNo;
    }

    public String getJump_group() {
        return this.jump_group;
    }

    public void setJump_group(String jumpGroup) {
        this.jump_group = jumpGroup;
    }

    public String getTrade_source() {
        return this.trade_source;
    }

    public void setTrade_source(String tradeSource) {
        this.trade_source = tradeSource;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
