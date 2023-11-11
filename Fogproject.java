package fog.project;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import java.util.Vector;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationInterQuartileRange;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.util.MathUtil;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySimple;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMaximumCorrelation;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumUtilization;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyRandomSelection;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.HostStateHistoryEntry;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.VmStateHistoryEntry;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;

import org.cloudbus.cloudsim.power.PowerHost;
//import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegression;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationLocalRegressionRobust;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationMedianAbsoluteDeviation;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.NetworkTopology;


public class Fogproject {

	private static List<Cloudlet> cloudletList;

	private static List<Vm> vmList;
       

	private static double hostsNumber = 50;
        public static int task_num = 50;
        public static int resource_num = 50;

	private static double cloudletsNumber =50;
        public static Vm[] vm=new PowerVm[50] ;
        public static int requestnumber=50;
	public static double[][] cloudarray = new double[task_num][2];

        
        public static Connection con;

        public static LinkedList<Vm> list = new LinkedList<Vm>();
        public static LinkedList<Cloudlet> list2 = new LinkedList<Cloudlet>();
        private static List<Vm> vmLists;

         public static int t ;
	public static void main(String[] args) {
          
            
           
               
               Vector v = new Vector();
               int max = 0;
          
		try {
                       String url = "jdbc:mysql://localhost:3306/result";
                       String user = "root";
                       String pass = "";
                       con = DriverManager.getConnection(url,user,pass);
                       Statement st = con.createStatement();
                       int i = 0;
                      // String ins = "select *from sort_task;";
                      // ResultSet rs = st.executeQuery(ins);
                       
                      
                       
                       
                       
                      String ins = "select lnt,piriority from task;";
                      ResultSet rs = st.executeQuery(ins);
                       rs.beforeFirst();
                       i = 0;
                       while (rs.next() && i < task_num) {
                       cloudarray[i][0] = rs.getDouble("lnt");
                       cloudarray[i][1] = rs.getDouble("piriority");
                       i++;
                       }	
                   
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace GridSim events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			
                        PowerDatacenter datacenter = (PowerDatacenter) createDatacenter(
					"Datacenter0",
					PowerDatacenter.class);
                       
					
					
			String inss = "select number1,number2,number3 from results;";
                       rs = st.executeQuery(inss);
                       rs.beforeFirst();
                       int t1=resource_num,t2=resource_num,t3=resource_num;
                       while (rs.next() ) {
                       t1= (int)rs.getDouble("number1");
                       t2= (int)rs.getDouble("number2");
                       t3= (int)rs.getDouble("number3");
                        
                       }
                    
                        DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();
                        i=0;
                        vmList = createVmList(brokerId,resource_num);
                        cloudletList = createCloudletList(brokerId);
                       
                        broker.submitVmList(vmList);
                        broker.submitCloudletList(cloudletList);
                
                       vmLists = createVmList(brokerId,t1);
                       vmLists = createVmList(brokerId,t2);
                       vmLists = createVmList(brokerId,t3);
			double lastClock = CloudSim.startSimulation();
			// Final step: Print results when simulation is over
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			Log.printLine("Received " + newList.size() + " cloudlets"); 
                    NetworkTopology.addLink(datacenter.getId(),broker.getId(),10.0,10);

			CloudSim.stopSimulation();

			
                        

			Log.printLine();
			Log.printLine(String.format("Energy consumption: %.2f kWh", datacenter.getPower() / (3600 * 1000)));
			 Log.printLine(String.format("makespan time: %.2f sec", lastClock));

			Log.printLine();

		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}

		Log.printLine("finished!");
	}

	  private static List<Vm> createVmList(int brokerId,int requestnumber) {
           //requestnumber=50;
	List<Vm> vmlist = new ArrayList<Vm>();
		for (int i = 0; i < requestnumber ; i++) {
			int vmType = i / (int) Math.ceil((double) requestnumber / Constants.VM_TYPES);
			vm[i]=new PowerVm(
					i,
					brokerId,
					Constants.VM_MIPS[vmType],
					Constants.VM_PES[vmType],
                                        Constants.VM_RAM[vmType],
					Constants.VM_BW,
					Constants.VM_SIZE,
					1,
					"Xen",
					new CloudletSchedulerDynamicWorkload(Constants.VM_MIPS[vmType], Constants.VM_PES[vmType]),
					Constants.SCHEDULING_INTERVAL);
//                       wrapper   r =new  wrapper ();
                       vmlist.add(vm[i]);
		}

		
		return vmlist;
			
                
                
                
	}
        
        private static List<Cloudlet> createCloudletList(int brokerId ) {
	List<Cloudlet> list = new ArrayList<Cloudlet>();

		long length = 150000; // 10 min on 250 MIPS
		int pesNumber = 1;
		long fileSize = 300;
		long outputSize = 300;
               // cloudletsNumber =50;
		for (int i = 0; i < cloudletsNumber; i++) {
			Cloudlet cloudlet = new Cloudlet(i, length, pesNumber, fileSize, outputSize, new UtilizationModelStochastic(), new UtilizationModelStochastic(), new UtilizationModelStochastic());
			cloudlet.setUserId(brokerId);
			cloudlet.setVmId(i);
			list.add(cloudlet);
		}

		return list;
	
	}   
	 
	private static Datacenter createDatacenter(String name,
			Class<? extends Datacenter> datacenterClass)
			 throws Exception {
		
		List<PowerHost> hostList = new ArrayList<PowerHost>();
              
		int[] mips = { 5000, 5500 };
		int [] ram = {4096, 4096}; // host memory (MB)
		long storage = 10000; // host storage
		int bw = 10000;
              int[] HOST_MIPS = { 5000, 4500 };
            int[] HOST_PES	 = { 1, 1 };
		for (int i = 0; i < hostsNumber; i++) {
			int hostType = i % Constants.HOST_TYPES;

			List<Pe> peList = new ArrayList<Pe>();
			for (int j = 0; j < Constants.HOST_PES[hostType]; j++) {
				peList.add(new Pe(j, new PeProvisionerSimple(HOST_MIPS[hostType])));
			}

			hostList.add(new PowerHostUtilizationHistory(
					i,
					new RamProvisionerSimple(ram[hostType]),
					new BwProvisionerSimple(Constants.HOST_BW),
					Constants.HOST_STORAGE,
					peList,
					new VmSchedulerTimeShared(peList),
					Constants.HOST_POWER[hostType]));// This is our machine
		}
                
		
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this resource
		double costPerBw = 0.0; // the cost of using bw in this resource
                String vmAllocationPolicyName="lr";
			String vmSelectionPolicyName= "mmt";
			String parameterName="2.5";
                        VmAllocationPolicy vmAllocationPolicy = null;
		PowerVmSelectionPolicy vmSelectionPolicy = null;
		if (!vmSelectionPolicyName.isEmpty()) {
			vmSelectionPolicy = getVmSelectionPolicy(vmSelectionPolicyName);
		}
		double parameter = 0;
		if (!parameterName.isEmpty()) {
			parameter = Double.valueOf(parameterName);
		}
		
		 if (vmAllocationPolicyName.equals("lr")) {
			PowerVmAllocationPolicyMigrationAbstract fallbackVmSelectionPolicy = new PowerVmAllocationPolicyMigrationStaticThreshold(
					hostList,
					vmSelectionPolicy,
					0.7);
			vmAllocationPolicy = new PowerVmAllocationPolicyMigrationLocalRegression(
					hostList,
					vmSelectionPolicy,
					parameter,
					Constants.SCHEDULING_INTERVAL,
					fallbackVmSelectionPolicy);
			} else {
			System.out.println("Unknown VM allocation policy: " + vmAllocationPolicyName);
			System.exit(0);
		}
		 

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
			Datacenter datacenter = null;
		try {
			 datacenter = datacenterClass.getConstructor(
					String.class,
					DatacenterCharacteristics.class,
					VmAllocationPolicy.class,
					List.class,
					Double.TYPE).newInstance(
					name,
					characteristics,
					vmAllocationPolicy,
					new LinkedList<Storage>(),
					Constants.SCHEDULING_INTERVAL);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}
protected static PowerVmSelectionPolicy getVmSelectionPolicy(String vmSelectionPolicyName) {
		PowerVmSelectionPolicy vmSelectionPolicy = null;
		
		 if (vmSelectionPolicyName.equals("mc")) {
			vmSelectionPolicy = new PowerVmSelectionPolicyMaximumCorrelation(
					new PowerVmSelectionPolicyMinimumMigrationTime());
		} else if (vmSelectionPolicyName.equals("mmt")) {
			vmSelectionPolicy = new PowerVmSelectionPolicyMinimumMigrationTime();
		} else if (vmSelectionPolicyName.equals("mu")) {
			vmSelectionPolicy = new PowerVmSelectionPolicyMinimumUtilization();
		} else if (vmSelectionPolicyName.equals("rs")) {
			vmSelectionPolicy = new PowerVmSelectionPolicyRandomSelection();
		} else {
			System.out.println("Unknown VM selection policy: " + vmSelectionPolicyName);
			System.exit(0);
		}
		return vmSelectionPolicy;
	}
	
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	
	
}
