package com.company;


/*
Standard class to save the goal information as an object
 */
public class GoalObj {
    public GoalObj( String goaluid, String name,String currency, int minorUnits) {
        this.goaluid = goaluid;
        this.name = name;
        this.currency = currency;
        this.minorUnits = minorUnits;
    }

    String currency,name,goaluid="";
    int minorUnits=0;
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoaluid() {
        return goaluid;
    }

    public void setGoaluid(String goaluid) {
        this.goaluid = goaluid;
    }

    public int getMinorUnits() {
        return minorUnits;
    }

    public void setMinorUnits(int minorUnits) {
        this.minorUnits = minorUnits;
    }

}
