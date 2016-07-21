package edu.clemson.ece.parse.randomseries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RandomSeries {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Parameters.end = 1;
		CliParser cli = new CliParser();
		cli.parse(args);
		
		Random r = new Random();
		String taskId = Parameters.taskId;
		String jobId = Parameters.jobId;
		
		if(!Parameters.specifyTask){
			int rFileNumber = r.nextInt(500);
			System.out.println("file: "+rFileNumber);
			
			FileReader fr = new FileReader(Parameters.path, rFileNumber);
			int n = 0;
			String line = "";
			while((line=fr.br.readLine())!=null){
				n++;
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
				if(r.nextInt(n)==0) {
					jobId = cell[2];
					taskId = cell[3];
				}
			}
			fr.br.close();
		}
		
		System.out.println("Job and Task ids:"+jobId+"-"+taskId);
		
		File cpufile = new File(Parameters.outpath+jobId+"-"+taskId+"-cpu");
		cpufile.getParentFile().mkdirs();
		if (!cpufile.exists()) {
			cpufile.createNewFile();
		} else {
			System.out.println(" already exist");
			System.exit(0);
		}
		
		File memfile = new File(Parameters.outpath+jobId+"-"+taskId+"-mem");
		memfile.getParentFile().mkdirs();
		if (!memfile.exists()) {
			memfile.createNewFile();
		} else {
			System.out.println(" already exist");
			System.exit(0);
		}
		
		
		BufferedWriter bwCpu = new BufferedWriter(new FileWriter(cpufile.getAbsoluteFile()));
		BufferedWriter bwMem = new BufferedWriter(new FileWriter(memfile.getAbsoluteFile()));
			
		
		for(int i=0; i<500; i++){
			System.out.println("Processing "+i);
			FileReader file = new FileReader(Parameters.path, i);
			String readLine = "";
			while((readLine=file.br.readLine())!=null){
				String[] cell = readLine.split(",");
				if(!jobId.equals(cell[2])||!taskId.equals(cell[3])) continue;
				bwCpu.write(cell[5]+"\n");
				bwMem.write(cell[6]+"\n");
				
			}
			file.br.close();
		}
		
		bwCpu.close();
		bwMem.close();
		
	}

}
