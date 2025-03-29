package com.example.service;

import com.example.model.*;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;
import org.springframework.stereotype.Service;

import java.util.*;

import com.example.algorithms.*;

@Service
public class SimulationService {

    public SimulationResponse runCloudSim(SimulationRequest request) {
        long startTime = System.currentTimeMillis();
        List<String> logs = new ArrayList<>();
        Map<String, String> vmAllocations = new HashMap<>();
        Map<String, String> cloudletExecution = new HashMap<>();
        List<String> failedAllocations = new ArrayList<>();

        try {
            if (request.getNumVMs() <= 0 || request.getNumCloudlets() <= 0 || request.getNumHosts() <= 0) {
                return new SimulationResponse("Failed", Collections.emptyList(), request.getAlgorithm(), 0, vmAllocations, cloudletExecution, failedAllocations, List.of("Invalid input. All values must be greater than zero."));
            }

            CloudSim.init(1, Calendar.getInstance(), false);
            logs.add("CloudSim initialized.");

            // Create Datacenter and Broker
            Datacenter datacenter0 = createDatacenter("Datacenter_0", request, logs, vmAllocations);
            if (datacenter0 == null) {
                throw new RuntimeException("Failed to create Datacenter.");
            }

            DatacenterBroker broker = createBroker();
            if (broker == null) {
                throw new RuntimeException("Failed to create Broker.");
            }

            // Create VMs and Cloudlets
            List<Vm> vmList = createVMs(broker.getId(), request);
            List<Cloudlet> cloudletList = createCloudlets(broker.getId(), request);

            broker.submitVmList(vmList);
            broker.submitCloudletList(cloudletList);

            // Choose Algorithm
            switch (request.getAlgorithm().toLowerCase()) {
                case "roundrobin":
                    new RoundRobinAlgorithm().runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "fcfs":
                    new FCFSAlgorithm().runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "ant":
                    new ACOAlgorithm().runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "genetic":
                    new GeneticAlgorithm().runAlgorithm(broker, vmList, cloudletList);
                    break;
                case "sjf":
                    new SJFAlgorithm().runAlgorithm(broker, vmList, cloudletList);
                    break;
                default:
                    throw new RuntimeException("Invalid algorithm specified.");
            }

            logs.add("Starting simulation...");
            CloudSim.startSimulation();
            CloudSim.stopSimulation();
            logs.add("Simulation completed successfully.");

            // Track Cloudlet Execution
            for (Cloudlet cloudlet : cloudletList) {
                if (cloudlet.getStatus() == Cloudlet.SUCCESS) {
                    cloudletExecution.put("Cloudlet " + cloudlet.getCloudletId(), "VM " + cloudlet.getVmId());
                } else {
                    failedAllocations.add("Cloudlet " + cloudlet.getCloudletId() + " failed.");
                }
            }

            // Track VM Allocations
//            for (Vm vm : vmList) {
//                if (vm.getHost() != null) {
//                    vmAllocations.put("VM " + vm.getId(), "Host " + vm.getHost().getId());
//                    logs.add("VM " + vm.getId() + " allocated to Host " + vm.getHost().getId());
//                } else {
//                    logs.add("VM " + vm.getId() + " failed to allocate.");
//                }
//            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            return new SimulationResponse("Success", List.of("Simulation completed using " + request.getAlgorithm()), request.getAlgorithm(), elapsedTime, vmAllocations, cloudletExecution, failedAllocations, logs);

        } catch (Exception e) {
            logs.add("Simulation failed: " + e.getMessage());
            return new SimulationResponse("Failed", Collections.emptyList(), request.getAlgorithm(), 0, vmAllocations, cloudletExecution, failedAllocations, logs);
        }
    }

    // Method to create Datacenter
    private Datacenter createDatacenter(String name, SimulationRequest request, List<String> logs, Map<String, String> vmAllocations) {
        try {
            List<Host> hostList = new ArrayList<>();
            for (int i = 0; i < request.getNumHosts(); i++) {
                List<Pe> peList = new ArrayList<>();
                List<Integer> pesMips = request.getHosts().get(i).getPesMips();

                for (int j = 0; j < pesMips.size(); j++) {
                    peList.add(new Pe(j, new PeProvisionerSimple(pesMips.get(j))));
                }

                Host host = new Host(i,
                        new RamProvisionerSimple(request.getHosts().get(i).getRam()),
                        new BwProvisionerSimple(request.getHosts().get(i).getBw()),
                        request.getHosts().get(i).getStorage(),
                        peList,
                        new VmSchedulerTimeShared(peList));

                hostList.add(host);
            }

            DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                    "x86", "Linux", "Xen", hostList, 10.0, 3.0, 0.05, 0.001, 0.0);

            Datacenter datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), new LinkedList<>(), 0);
            logs.add("Datacenter " + name + " created with " + request.getNumHosts() + " hosts.");

            return datacenter;

        } catch (Exception e) {
            logs.add("Failed to create Datacenter: " + e.getMessage());
            return null;
        }
    }

    // Method to create Broker
    private DatacenterBroker createBroker() {
        try {
            return new DatacenterBroker("Broker");
        } catch (Exception e) {
            System.out.println("Failed to create Broker: " + e.getMessage());
            return null;
        }
    }

    // Method to create VMs
    private List<Vm> createVMs(int brokerId, SimulationRequest request) {
        List<Vm> list = new ArrayList<>();
        for (int i = 0; i < request.getNumVMs(); i++) {
            list.add(new Vm(i, brokerId, request.getVmMips(), 1, request.getVmRam(),
                    request.getVmBw(), request.getVmSize(),
                    "Xen", new CloudletSchedulerTimeShared()));
        }
        return list;
    }

    // Method to create Cloudlets
    private List<Cloudlet> createCloudlets(int brokerId, SimulationRequest request) {
        List<Cloudlet> list = new ArrayList<>();
        for (int i = 0; i < request.getNumCloudlets(); i++) {
            Cloudlet cloudlet = new Cloudlet(i, 4000, 1, 300, 300,
                    new UtilizationModelFull(), new UtilizationModelFull(), new UtilizationModelFull());
            cloudlet.setUserId(brokerId);
            list.add(cloudlet);
        }
        return list;
    }
}
