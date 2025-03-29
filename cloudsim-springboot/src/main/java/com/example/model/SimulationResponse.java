package com.example.model;

import java.util.List;
import java.util.Map;

public class SimulationResponse {

    private String status;
    private List<String> messages;
    private String algorithm;
    private long elapsedTime;
    private Map<String, String> vmAllocations;
    private Map<String, String> cloudletExecution;
    private List<String> failedAllocations;
    private List<String> logs;

    // Constructor
    public SimulationResponse(String status, List<String> messages, String algorithm, long elapsedTime,
                              Map<String, String> vmAllocations, Map<String, String> cloudletExecution,
                              List<String> failedAllocations, List<String> logs) {
        this.status = status;
        this.messages = messages;
        this.algorithm = algorithm;
        this.elapsedTime = elapsedTime;
//        this.vmAllocations = vmAllocations;
        this.cloudletExecution = cloudletExecution;
        this.failedAllocations = failedAllocations;
        this.logs = logs;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

//    public Map<String, String> getVmAllocations() {
//        return vmAllocations;
//    }
//
//    public void setVmAllocations(Map<String, String> vmAllocations) {
//        this.vmAllocations = vmAllocations;
//    }

    public Map<String, String> getCloudletExecution() {
        return cloudletExecution;
    }

    public void setCloudletExecution(Map<String, String> cloudletExecution) {
        this.cloudletExecution = cloudletExecution;
    }

    public List<String> getFailedAllocations() {
        return failedAllocations;
    }

    public void setFailedAllocations(List<String> failedAllocations) {
        this.failedAllocations = failedAllocations;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
