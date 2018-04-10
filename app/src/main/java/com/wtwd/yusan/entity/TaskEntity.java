package com.wtwd.yusan.entity;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskEntity {

    /**
     * 数据库id
     */
    private int id;
    /**
     * 发布任务者头像URL
     */
    private String imgUrl;
    /**
     * 发布任务者名字
     */
    private String taskName;
    /**
     * 发布任务者性别
     */
    private String taskerSex;
    /**
     * 发布任务详情描述
     */
    private String taskContent;
    /**
     * 任务金额
     */
    private float taskCost;
    /**
     * 任务类型
     */
    private int taskType;
    /**
     * 任务开始时间
     */
    private String taskTime;
    /**
     * 任务开始日期
     */
    private String taskDate;
    /**
     * 任务性别条件
     */
    private String taskCondition;
    /**
     * 任务完成地点
     */
    private String taskLocation;
    /**
     * 任务状态
     */
    private String taskState;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskerSex() {
        return taskerSex;
    }

    public void setTaskerSex(String taskerSex) {
        this.taskerSex = taskerSex;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public float getTaskCost() {
        return taskCost;
    }

    public void setTaskCost(float taskCost) {
        this.taskCost = taskCost;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskCondition() {
        return taskCondition;
    }

    public void setTaskCondition(String taskCondition) {
        this.taskCondition = taskCondition;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskerSex='" + taskerSex + '\'' +
                ", taskContent='" + taskContent + '\'' +
                ", taskCost=" + taskCost +
                ", taskType=" + taskType +
                ", taskTime='" + taskTime + '\'' +
                ", taskDate='" + taskDate + '\'' +
                ", taskCondition='" + taskCondition + '\'' +
                ", taskLocation='" + taskLocation + '\'' +
                ", taskState='" + taskState + '\'' +
                '}';
    }
}
