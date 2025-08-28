package com.example;

public class Process {
    private String process;
    private Integer arrivalTime;
    private Integer burstTime;
    private Integer completionTime;
    private Integer turnaroundTime;
    private Integer waitingTime;
    private Integer responseTime;

    public Process(String process, Integer arrivalTime, Integer burstTime,
            Integer completionTime, Integer turnaroundTime,
            Integer waitingTime, Integer responseTime) {
        this.process = process;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.completionTime = completionTime;
        this.turnaroundTime = turnaroundTime;
        this.waitingTime = waitingTime;
        this.responseTime = responseTime;
    }

    // Getters
    public String getProcess() {
        return process;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public Integer getBurstTime() {
        return burstTime;
    }

    public Integer getCompletionTime() {
        return completionTime;
    }

    public Integer getTurnaroundTime() {
        return turnaroundTime;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public Integer getResponseTime() {
        return responseTime;
    }
}
