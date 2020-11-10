package com.sangxiang.model.Task;

public class TaskParam {
    /***
     * 传入userId
     */
    private int userId;
    /***
     * 传入taskId
     */
    private int taskId;
    /***
     * 传入主键的id，代表所想要的主键
     */
    private int id;

    private boolean agree;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }
}
