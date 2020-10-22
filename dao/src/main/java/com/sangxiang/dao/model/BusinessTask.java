package com.sangxiang.dao.model;

import com.sangxiang.base.model.BaseEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;

public class BusinessTask extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userid;

    private String createTime;

    private String title;

    private String content;

    private BigDecimal workerPrice;

    private Integer needpeoplenum;

    private Integer payStatus;

    private Integer status;
    @Transient
    private Integer hasApplyedPeopleNum;//有多少人申请

    @Transient
    private Integer hasApplyedAndAuditPassPeopleNum;//已申请通过多少人

    private BigDecimal needtotalpay;

    private Integer typeid;

    private String taskLimit;

    private String applyEndTime;

    private String workEndTime;

    private Integer aduitTime;

    private String orderid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public BigDecimal getWorkerPrice() {
        return workerPrice;
    }

    public void setWorkerPrice(BigDecimal workerPrice) {
        this.workerPrice = workerPrice;
    }

    public Integer getNeedpeoplenum() {
        return needpeoplenum;
    }

    public void setNeedpeoplenum(Integer needpeoplenum) {
        this.needpeoplenum = needpeoplenum;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getNeedtotalpay() {
        return needtotalpay;
    }

    public void setNeedtotalpay(BigDecimal needtotalpay) {
        this.needtotalpay = needtotalpay;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getTaskLimit() {
        return taskLimit;
    }

    public void setTaskLimit(String taskLimit) {
        this.taskLimit = taskLimit == null ? null : taskLimit.trim();
    }

    public String getApplyEndTime() {
        return applyEndTime;
    }

    public void setApplyEndTime(String applyEndTime) {
        this.applyEndTime = applyEndTime == null ? null : applyEndTime.trim();
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime == null ? null : workEndTime.trim();
    }

    public Integer getAduitTime() {
        return aduitTime;
    }

    public void setAduitTime(Integer aduitTime) {
        this.aduitTime = aduitTime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getHasApplyedPeopleNum() {
        return hasApplyedPeopleNum;
    }

    public void setHasApplyedPeopleNum(Integer hasApplyedPeopleNum) {
        this.hasApplyedPeopleNum = hasApplyedPeopleNum;
    }

    public Integer getHasApplyedAndAuditPassPeopleNum() {
        return hasApplyedAndAuditPassPeopleNum;
    }

    public void setHasApplyedAndAuditPassPeopleNum(Integer hasApplyedAndAuditPassPeopleNum) {
        this.hasApplyedAndAuditPassPeopleNum = hasApplyedAndAuditPassPeopleNum;
    }
}