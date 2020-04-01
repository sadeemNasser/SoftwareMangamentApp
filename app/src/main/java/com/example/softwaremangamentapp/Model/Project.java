package com.example.softwaremangamentapp.Model;


public class Project {

    private String projectName;
    private String projectStartDate;
    private String projectEndDate;
    private String projectDescription;
    private double totalCost;

    public Project() {

    }

    public Project(String projectName, String prpjectStartDate, String prpjectEndDate , String projectDescription, double totalCost){
        this.projectName = projectName;
        this.projectEndDate = prpjectStartDate;
        this.projectStartDate = prpjectStartDate;
        this.projectDescription = projectDescription;
        this.totalCost = totalCost;
    }



    public double getTotalCost() {
        return totalCost;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectStartDate() {
        return projectStartDate;
    }

    public String getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectEndDate(String projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public void setProjectStartDate(String projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}

