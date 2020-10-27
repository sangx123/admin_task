package com.sangxiang.model.UserCenter;

public class MyTaskParam {
    private Integer pageSize = 10;
    private Integer pageNumber = 1;
    private Integer status = 0;//1，表示已创建，未支付，2表示已支付待审核，3表示进行中，4，表示已完成
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
}
