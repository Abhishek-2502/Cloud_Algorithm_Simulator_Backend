package com.example.model;

import java.util.List;

public class SimulationRequest {

    private int numVMs;
    private int numCloudlets;
    private int numHosts;
    private List<HostRequest> hosts;
    private int vmMips;
    private int vmRam;
    private int vmBw;
    private int vmSize;
    private String algorithm;

    // Getters and Setters
    public int getNumVMs() {
        return numVMs;
    }

    public void setNumVMs(int numVMs) {
        this.numVMs = numVMs;
    }

    public int getNumCloudlets() {
        return numCloudlets;
    }

    public void setNumCloudlets(int numCloudlets) {
        this.numCloudlets = numCloudlets;
    }

    public int getNumHosts() {
        return numHosts;
    }

    public void setNumHosts(int numHosts) {
        this.numHosts = numHosts;
    }

    public List<HostRequest> getHosts() {
        return hosts;
    }

    public void setHosts(List<HostRequest> hosts) {
        this.hosts = hosts;
    }

    public int getVmMips() {
        return vmMips;
    }

    public void setVmMips(int vmMips) {
        this.vmMips = vmMips;
    }

    public int getVmRam() {
        return vmRam;
    }

    public void setVmRam(int vmRam) {
        this.vmRam = vmRam;
    }

    public int getVmBw() {
        return vmBw;
    }

    public void setVmBw(int vmBw) {
        this.vmBw = vmBw;
    }

    public int getVmSize() {
        return vmSize;
    }

    public void setVmSize(int vmSize) {
        this.vmSize = vmSize;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
