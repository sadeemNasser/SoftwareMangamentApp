package com.example.softwaremangamentapp.Model;

public class TaskInfo {

    public String projectID;
    public String taskName;
    public String taskStartDate;
    public String taskEndDate;
    public String taskResource;
    public String statues;
    public double taskCost;

    public TaskInfo() {
    }

    public TaskInfo(String taskName, String taskStartDate, String taskEndDate , String taskResource, double taskCost, String projectID){
        this.taskName = taskName;
        this.taskStartDate = taskStartDate;
        this.taskEndDate = taskEndDate;
        this.taskResource = taskResource;
        this.taskCost = taskCost;
        this.projectID=projectID;
        statues="Active";

    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }

    public String getProjectID() {

        return projectID;
    }

    public String getTaskName() {

        return taskName;
    }

    public String getTaskEndDate() {

        return taskEndDate;
    }

    public String getTaskStartDate() {

        return taskStartDate;
    }

    public double getTaskCost() {

        return taskCost;
    }

    public String getTaskResource() {

        return taskResource;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskStartDate(String taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public void setTaskEndDate(String taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public void setTaskResource(String taskResource) {
        this.taskResource = taskResource;
    }

    public void setTaskCost(double taskCost) {
        this.taskCost = taskCost;
    }
}
