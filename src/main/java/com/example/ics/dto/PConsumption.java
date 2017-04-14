package com.example.ics.dto;

/**
 * Simple model to store the calculated value of Product Consumption
 * Created by razamd on 4/14/2017.
 */
public class PConsumption implements Comparable<PConsumption>{
    private Long monthlyConsumption;
    private double percentage;
    private double commulativePercentage;

    public PConsumption() {
    }

    public PConsumption(Long monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
    }

    public Long getMonthlyConsumption() {
        return monthlyConsumption;
    }

    public void setMonthlyConsumption(Long monthlyConsumption) {
        this.monthlyConsumption = monthlyConsumption;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getCommulativePercentage() {
        return commulativePercentage;
    }

    public void setCommulativePercentage(double commulativePercentage) {
        this.commulativePercentage = commulativePercentage;
    }

    @Override
    public int compareTo(PConsumption other) {
        return (int)(monthlyConsumption - other.monthlyConsumption);
    }
}
