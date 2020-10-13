package com.sangxiang.dao.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userid;

    private Integer taskid;
    private Integer state;
    public Integer getId() {
        return id;
    }
    @Transient
    private  Task task;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskid() {
        return taskid;
    }

    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Transient
    public static int jie_shou=0;

    @Transient
    public static int yi_ti_jiao=1;

    @Transient
    public static int yi_wan_chen=2;

    @Transient
    public static int yi_guo_qi=3;

    public String getStatusContent() {
        switch(state){
            case 0:statusContent="已接受";break;
            case 1:statusContent="已提交";break;
            case 2:statusContent="已完成";break;
            case 3:statusContent="已过期";break;
            default:statusContent="未知";
        }
        return statusContent;
    }
    @Transient
    public String  statusContent;
}