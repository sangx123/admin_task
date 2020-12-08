package com.sangxiang.dao.model;

import com.sangxiang.base.model.BaseEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

public class UserTask extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    private Integer businessTaskId;

    private Integer userTaskStatus;

    private Date userApplyTaskTime;

    private Date businessAgreeApplyTime;

    private Date userFirstSubmitTaskTime;

    private String userFirstSubmitTaskContent;

    private Date businessAuditFirstTime;

    private String businessAuditFirstResult;

    private Date userSecondSubmitTaskTime;

    private String userSecondSubmitTaskContent;

    private Date businessAuditSecondTime;

    private String businessAuditSecondResult;

    private Integer userQuitTask;

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    private Boolean finished;

    public Integer getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(Integer businessUserId) {
        this.businessUserId = businessUserId;
    }

    public BigDecimal getUserGetMoney() {
        return userGetMoney;
    }

    public void setUserGetMoney(BigDecimal userGetMoney) {
        this.userGetMoney = userGetMoney;
    }

    private BigDecimal userGetMoney;
    private Integer businessUserId;

    public Date getUserFirstSubmitTaskTimeout() {
        return userFirstSubmitTaskTimeout;
    }

    public void setUserFirstSubmitTaskTimeout(Date userFirstSubmitTaskTimeout) {
        this.userFirstSubmitTaskTimeout = userFirstSubmitTaskTimeout;
    }

    private Date userFirstSubmitTaskTimeout;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBusinessTaskId() {
        return businessTaskId;
    }

    public void setBusinessTaskId(Integer businessTaskId) {
        this.businessTaskId = businessTaskId;
    }

    public Integer getUserTaskStatus() {
        return userTaskStatus;
    }

    public void setUserTaskStatus(Integer userTaskStatus) {
        this.userTaskStatus = userTaskStatus;
    }

    public Date getUserApplyTaskTime() {
        return userApplyTaskTime;
    }

    public void setUserApplyTaskTime(Date userApplyTaskTime) {
        this.userApplyTaskTime = userApplyTaskTime;
    }

    public Date getBusinessAgreeApplyTime() {
        return businessAgreeApplyTime;
    }

    public void setBusinessAgreeApplyTime(Date businessAgreeApplyTime) {
        this.businessAgreeApplyTime = businessAgreeApplyTime;
    }

    public Date getUserFirstSubmitTaskTime() {
        return userFirstSubmitTaskTime;
    }

    public void setUserFirstSubmitTaskTime(Date userFirstSubmitTaskTime) {
        this.userFirstSubmitTaskTime = userFirstSubmitTaskTime;
    }

    public String getUserFirstSubmitTaskContent() {
        return userFirstSubmitTaskContent;
    }

    public void setUserFirstSubmitTaskContent(String userFirstSubmitTaskContent) {
        this.userFirstSubmitTaskContent = userFirstSubmitTaskContent == null ? null : userFirstSubmitTaskContent.trim();
    }

    public Date getBusinessAuditFirstTime() {
        return businessAuditFirstTime;
    }

    public void setBusinessAuditFirstTime(Date businessAuditFirstTime) {
        this.businessAuditFirstTime = businessAuditFirstTime;
    }

    public String getBusinessAuditFirstResult() {
        return businessAuditFirstResult;
    }

    public void setBusinessAuditFirstResult(String businessAuditFirstResult) {
        this.businessAuditFirstResult = businessAuditFirstResult == null ? null : businessAuditFirstResult.trim();
    }

    public Date getUserSecondSubmitTaskTime() {
        return userSecondSubmitTaskTime;
    }

    public void setUserSecondSubmitTaskTime(Date userSecondSubmitTaskTime) {
        this.userSecondSubmitTaskTime = userSecondSubmitTaskTime;
    }

    public String getUserSecondSubmitTaskContent() {
        return userSecondSubmitTaskContent;
    }

    public void setUserSecondSubmitTaskContent(String userSecondSubmitTaskContent) {
        this.userSecondSubmitTaskContent = userSecondSubmitTaskContent == null ? null : userSecondSubmitTaskContent.trim();
    }

    public Date getBusinessAuditSecondTime() {
        return businessAuditSecondTime;
    }

    public void setBusinessAuditSecondTime(Date businessAuditSecondTime) {
        this.businessAuditSecondTime = businessAuditSecondTime;
    }

    public String getBusinessAuditSecondResult() {
        return businessAuditSecondResult;
    }

    public void setBusinessAuditSecondResult(String businessAuditSecondResult) {
        this.businessAuditSecondResult = businessAuditSecondResult == null ? null : businessAuditSecondResult.trim();
    }

    public Integer getUserQuitTask() {
        return userQuitTask;
    }

    public void setUserQuitTask(Integer userQuitTask) {
        this.userQuitTask = userQuitTask;
    }

    @Transient
    private  BusinessTask task;

    public BusinessTask getTask() {
        return task;
    }

    public void setTask(BusinessTask task) {
        this.task = task;
    }

    @Transient
    private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}