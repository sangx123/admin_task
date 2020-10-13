package com.sangxiang.app.controller.login;

import com.sangxiang.dao.model.SysRole;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

public class UserLoginInfo {
    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    private String userToken;

    private Integer id;

    private String username;

    private String name;

    private String password;

    private String salt;
    //用户状态：0-启用；1-停用；2-锁定；
    private int state;

    private String mobile;

    private String email;

    @Column(name = "create_time")
    private Date createTime;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Transient
    private SysRole role;


    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Float money;

    public Float getMoney() {
        if(money==null){
            money=0f;
        }
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

}
