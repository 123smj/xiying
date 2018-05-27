/*
 * Decompiled with CFR 0_124.
 */
package com.manage.bean;

public class OprInfo {
    private String opr_id;
    private String company_id;
    private Integer opr_degree;
    private String opr_sta;
    private String opr_name;
    private String opr_gender;
    private String register_dt;
    private String opr_pwd;
    private String opr_mobile;
    private String opr_email;
    private String last_upd_ts;
    private String company_name;
    private String address;
    private String connact_name;
    private String identify_no;
    private String opr_type;
    private String belowMchts;

    public String getOpr_id() {
        return this.opr_id;
    }

    public void setOpr_id(String oprId) {
        this.opr_id = oprId;
    }

    public Integer getOpr_degree() {
        return this.opr_degree;
    }

    public void setOpr_degree(Integer oprDegree) {
        this.opr_degree = oprDegree;
    }

    public String getOpr_sta() {
        return this.opr_sta;
    }

    public void setOpr_sta(String oprSta) {
        this.opr_sta = oprSta;
    }

    public String getOpr_name() {
        return this.opr_name;
    }

    public void setOpr_name(String oprName) {
        this.opr_name = oprName;
    }

    public String getOpr_gender() {
        return this.opr_gender;
    }

    public void setOpr_gender(String oprGender) {
        this.opr_gender = oprGender;
    }

    public String getRegister_dt() {
        return this.register_dt;
    }

    public void setRegister_dt(String registerDt) {
        this.register_dt = registerDt;
    }

    public String getOpr_pwd() {
        return this.opr_pwd;
    }

    public void setOpr_pwd(String oprPwd) {
        this.opr_pwd = oprPwd;
    }

    public String getOpr_mobile() {
        return this.opr_mobile;
    }

    public void setOpr_mobile(String oprMobile) {
        this.opr_mobile = oprMobile;
    }

    public String getOpr_email() {
        return this.opr_email;
    }

    public void setOpr_email(String oprEmail) {
        this.opr_email = oprEmail;
    }

    public String getLast_upd_ts() {
        return this.last_upd_ts;
    }

    public void setLast_upd_ts(String lastUpdTs) {
        this.last_upd_ts = lastUpdTs;
    }

    public String getOpr_type() {
        return this.opr_type;
    }

    public void setOpr_type(String oprType) {
        this.opr_type = oprType;
    }

    public String getCompany_id() {
        return this.company_id;
    }

    public void setCompany_id(String companyId) {
        this.company_id = companyId;
    }

    public String getCompany_name() {
        return this.company_name;
    }

    public void setCompany_name(String companyName) {
        this.company_name = companyName;
    }

    public String getBelowMchts() {
        return this.belowMchts;
    }

    public void setBelowMchts(String belowMchts) {
        this.belowMchts = belowMchts;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConnact_name() {
        return this.connact_name;
    }

    public void setConnact_name(String connactName) {
        this.connact_name = connactName;
    }

    public String getIdentify_no() {
        return this.identify_no;
    }

    public void setIdentify_no(String identifyNo) {
        this.identify_no = identifyNo;
    }

    public String toString() {
        return "OprInfo [address=" + this.address + ", belowMchts=" + this.belowMchts + ", company_id=" + this.company_id + ", company_name=" + this.company_name + ", connact_name=" + this.connact_name + ", identify_no=" + this.identify_no + ", last_upd_ts=" + this.last_upd_ts + ", opr_degree=" + this.opr_degree + ", opr_email=" + this.opr_email + ", opr_gender=" + this.opr_gender + ", opr_id=" + this.opr_id + ", opr_mobile=" + this.opr_mobile + ", opr_name=" + this.opr_name + ", opr_pwd=" + this.opr_pwd + ", opr_sta=" + this.opr_sta + ", opr_type=" + this.opr_type + ", register_dt=" + this.register_dt + "]";
    }
}
