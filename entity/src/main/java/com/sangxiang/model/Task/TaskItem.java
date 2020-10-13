package com.sangxiang.model.Task;

public class TaskItem {
    private String insert;

    private TaskAttributes attributes;

    public void setInsert(String insert){
        this.insert = insert;
    }
    public String getInsert(){
        return this.insert;
    }
    public void setAttributes(TaskAttributes attributes){
        this.attributes = attributes;
    }
    public TaskAttributes getAttributes(){
        return this.attributes;
    }
}
