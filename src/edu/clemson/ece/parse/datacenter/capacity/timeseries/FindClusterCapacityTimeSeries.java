package edu.clemson.ece.parse.datacenter.capacity.timeseries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class FindClusterCapacityTimeSeries {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		CliParserDatacenter cli = new CliParserDatacenter();
		cli.parse(args);
		
		long curTime = 0;
		long deltaT = 300000000;
		double clusterCapacity = 0;
		int timeIndex = 0;
		
		GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(Parameters.path));
		BufferedReader br = new BufferedReader(new InputStreamReader(gzip)); 
		
		String line = "";
		
		Map<Double,Set<Long>> capMap = new HashMap<>();

		while((line=br.readLine())!=null){
			String[] cell = line.split(",");
			// 0 timestamp
			// 1 machine ID
			// 2 event type
			// 3 platform ID
			// *4 capacity: CPU
			// *5 capacity: memory
			
			
			// debug
			if(cell.length!=6){
				//System.out.println(line);
				continue;
			}
			
			double capacity = Double.valueOf(cell[Parameters.field]);
			int eventType = Integer.valueOf(cell[2]);
			long time = Long.valueOf(cell[0]);
			long machineID = Long.valueOf(cell[1]);

			if(!capMap.containsKey(capacity)){
				Set<Long> s = new HashSet<>();
				s.add(machineID);
				capMap.put(capacity, s);
			} else {
				Set<Long> s = capMap.get(capacity);
				s.add(machineID);
				capMap.put(capacity, s);
			}
			
			
			if((curTime==0 && time!=0) || (time-curTime)>=deltaT){
				if(curTime==0){
					System.out.println(timeIndex+", "+clusterCapacity);
					timeIndex++;
					curTime = 1; // just to avoid curTime==0
				}
				while((time-curTime)>=deltaT){
					System.out.println(timeIndex+", "+clusterCapacity);
					timeIndex++;
					curTime += deltaT;
				}	
			} else {
				
				if(eventType==0){
					clusterCapacity += capacity;
				} else if(eventType==1){
					clusterCapacity -= capacity;
				} else if(eventType==2){
					double preCap = 0;
					// in capMap, find machine capacity before updating
					for(Entry<Double, Set<Long>> ent : capMap.entrySet()){
						if(ent.getValue().contains(machineID)) {
							ent.getValue().remove(machineID);
							preCap = ent.getKey();
							break;
						}
					}
					// update capMap
					if(!capMap.containsKey(capacity)){
						Set<Long> s = new HashSet<>();
						s.add(machineID);
						capMap.put(capacity, s);
					} else {
						Set<Long> s = capMap.get(capacity);
						s.add(machineID);
						capMap.put(capacity, s);
					}
					// update cluster capacity
					clusterCapacity += (capacity - preCap);
				} else {
					System.out.println("Unknow event type!");
					System.exit(1);
				}
				
			}
		}
		
		br.close();
		
		gzip.close();
		
//		System.out.println(timeIndex+", "+curUsage);
		
	}

}
