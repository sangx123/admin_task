package com.sangxiang.model.UserCenter;

public class UserTaskParam {
    private Integer pageSize = 10;
    private Integer pageNumber = 1;
    private Integer status = -1;//
    //100查询所有的成功的结果
    private Integer taskId=-1;
    //    -1      表示全部状态
    //    状态0：  申请中(待商家同意)
    //    状态1:   用户待提交（商家同意申请通过，任务进行中）
    //    状态2:   用户待提交超过了任务截止时间(任务提交超时，用户可进行任务提交超时申诉)
    //    状态3:   商户审核中，（用户已提交任务，3天时间倒计时）
    //    状态4:   商户审核超时完成(任务状态为已完成，用户总金额+任务奖励)(用户查询审核状态的时候，超时就设置任务状态为商户审核超时，或者商家查询任务状态的时候，超时就设置为申请超时)
    //    状态5:   商户审核成功（任务已完成，用户总金额+任务奖励）
    //    状态6：  用户审核失败，（商家提供审核失败原因内容，审核失败时间）（用户可通过进行申诉，需提供申诉时间，申诉内容）（任务审核失败，已结束）
    //    状态7：  重新提交给商户申诉中，审核失败，待申诉，提交的内容，提交时间，申诉内容，申诉时间
    //    状态8:   商户超时未审核 (任务已完成，用户总金额+任务奖励)
    //    状态9:   申诉成功，(任务已完成，用户总金额+任务奖励)
    //    状态10:  申诉失败，（任务申诉失败，已结束）
    //user_task_status
    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
