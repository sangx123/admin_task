package com.sangxiang.model.UserCenter;

public class MyJieShouTaskParam {
    /**
     * 0-全部
     * 1-待提交
     * 2-审核中
     * 3-审核通过
     * 4-审核失败
     */
    private int type=-1;//设置默认值-1

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
