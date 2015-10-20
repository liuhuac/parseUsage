package edu.clemson.ece.parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParseUsage {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Parameters.end = 1;
		
		List<FileReader> readerList = new ArrayList<>();
		for(int i=Parameters.start; i<=Parameters.end; i++){
			readerList.add(new FileReader(Parameters.path, i));
		}
		
		Map<String,BufferedWriter[]> map = new HashMap<>();
		Set<String> jobs = new HashSet<>();
		
		for(FileReader fr : readerList){
			String line = "";
			while((line=fr.br.readLine())!=null){
				String[] cell = line.split(",");
				// 0 start time
				// 1 end time
				// 2 job ID
				// 3 task index
				// 4 machine ID
				// *5 CPU usage - mean and maximum in 1s window
				// *6 memory usage
				// 7 assigned memory
				// 8 unmapped page cache memory usage
				// 9 page cache memory usage
				// 10 maximum memory usage
				// 11 dis I/O time - mean
				// 12 local disk space used - mean
				// 13 CPU rate - max
				// 14 disk IO time - max
				// 15 cycles per instruction (CPI)
				// 16 memory accesses per instruction (MAI)
				// 17 sampling rate
				// 18 aggregation type
				// 19 sampled CPU usage

				String key = cell[2] + "-" + cell[3];
				
				if(!jobs.contains(cell[2])){			
					if(jobs.size()>Parameters.njobs){
						continue;
					} else {
						jobs.add(cell[2]);
					}
				}
				
				if(!map.containsKey(key)){				
					File cpufile = new File(Parameters.outpath+"cpu/"+key);
					cpufile.getParentFile().mkdirs();
					if (!cpufile.exists()) {
						cpufile.createNewFile();
					} else {
						System.out.println(Parameters.outpath+"cpu/"+key+" already exist");
						System.exit(0);
					}
					File memfile = new File(Parameters.outpath+"mem/"+key);
					memfile.getParentFile().mkdirs();
					if (!memfile.exists()) {
						memfile.createNewFile();
					} else {
						System.out.println(Parameters.outpath+"mem/"+key+" already exist");
						System.exit(0);
					}
					BufferedWriter[] bw = new BufferedWriter[2];
					bw[0] = new BufferedWriter(new FileWriter(cpufile.getAbsoluteFile()));
					bw[1] = new BufferedWriter(new FileWriter(memfile.getAbsoluteFile()));
					
					bw[0].write(cell[5]+"\n");
					bw[0].flush();
					bw[1].write(cell[6]+"\n");
					bw[1].flush();
					
					map.put(key,bw);
				} else {
					BufferedWriter[] bw = map.get(key);
					bw[0].write(cell[5]+"\n");
					bw[0].flush();
					bw[1].write(cell[6]+"\n");
					bw[1].flush();
				}
			}
		}
		
	}

}
