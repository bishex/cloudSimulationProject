package org.cloudsimplus.examples;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSim;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified version of BasicFirstExample to simulate a **private cloud**.
 * - No external multi-tenancy
 * - Private infrastructure
 * - Restricted VM allocation
 */
public class BasicFirstExample {
    private static final int HOSTS = 2; // Increased for better private resource handling
    private static final int HOST_PES = 8;
    private static final int HOST_MIPS = 1000;
    private static final int HOST_RAM = 4096; // More resources for private cloud
    private static final long HOST_BW = 10_000;
    private static final long HOST_STORAGE = 2_000_000;

    private static final int VMS = 4; // Increased for internal workload processing
    private static final int VM_PES = 4;

    private static final int CLOUDLETS = 6; // More cloudlets for private processing
    private static final int CLOUDLET_PES = 2;
    private static final int CLOUDLET_LENGTH = 20_000;

    private final CloudSim simulation;
    private final DatacenterBrokerSimple broker0;
    private List<Vm> vmList;
    private List<Cloudlet> cloudletList;
    private Datacenter datacenter0;

    public static void main(String[] args) {
        new BasicFirstExample();
    }

    private BasicFirstExample() {
        simulation = new CloudSim();
        datacenter0 = createDatacenter();

        // Custom Broker for Private Cloud (no external public allocation)
        broker0 = new DatacenterBrokerSimple(simulation);
        broker0.setName("PrivateCloudBroker"); // Name it explicitly as private

        vmList = createVms();
        cloudletList = createCloudlets();
        broker0.submitVmList(vmList);
        broker0.submitCloudletList(cloudletList);

        simulation.start();

        final var cloudletFinishedList = broker0.getCloudletFinishedList();
        new CloudletsTableBuilder(cloudletFinishedList).build();
    }

    /**
     * Creates a Private Datacenter and its Hosts.
     */
    private Datacenter createDatacenter() {
        final var hostList = new ArrayList<Host>(HOSTS);
        for (int i = 0; i < HOSTS; i++) {
            final var host = createHost();
            hostList.add(host);
        }

        // Private Datacenter without external multi-tenant resource provisioning
        Datacenter datacenter = new DatacenterSimple(simulation, hostList);
        datacenter.setName("PrivateDatacenter");
        return datacenter;
    }

    private Host createHost() {
        final var peList = new ArrayList<Pe>(HOST_PES);
        for (int i = 0; i < HOST_PES; i++) {
            peList.add(new PeSimple(HOST_MIPS));
        }

        return new HostSimple(HOST_RAM, HOST_BW, HOST_STORAGE, peList);
    }

    /**
     * Creates a list of VMs for private cloud use.
     */
    private List<Vm> createVms() {
        final var vmList = new ArrayList<Vm>(VMS);
        for (int i = 0; i < VMS; i++) {
            final var vm = new VmSimple(HOST_MIPS, VM_PES);
            vm.setRam(1024).setBw(2000).setSize(20_000);
            vmList.add(vm);
        }

        return vmList;
    }

    /**
     * Creates cloudlets for internal processing in the private cloud.
     */
    private List<Cloudlet> createCloudlets() {
        final var cloudletList = new ArrayList<Cloudlet>(CLOUDLETS);
        final var utilizationModel = new UtilizationModelDynamic(0.7);

        for (int i = 0; i < CLOUDLETS; i++) {
            final var cloudlet = new CloudletSimple(CLOUDLET_LENGTH, CLOUDLET_PES, utilizationModel);
            cloudlet.setSizes(2048);
            cloudletList.add(cloudlet);
        }

        return cloudletList;
    }
}

