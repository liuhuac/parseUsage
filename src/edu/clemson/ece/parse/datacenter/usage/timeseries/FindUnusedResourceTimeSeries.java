package edu.clemson.ece.parse.datacenter.usage.timeseries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class FindUnusedResourceTimeSeries {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		CliParserDatacenter cli = new CliParserDatacenter();
		cli.parse(args);
		
		long curTime = 600000000;
		long deltaT = 300000000;
		double curUsage = 0;
		int timeIndex = 0;
		
		for(int i=Parameters.start; i<=Parameters.end; i++){
			
			GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(Parameters.path+"/part-"+String.format("%05d", i)+"-of-00500.csv.gz"));
			BufferedReader br = new BufferedReader(new InputStreamReader(gzip)); 
			
			String line = "";
			while((line=br.readLine())!=null){
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
				
				double usage;
				if(cell[Parameters.field].equals("")){
					usage = 0;
				} else {
					usage = Double.valueOf(cell[Parameters.field]);
				}
				
				long time = Long.valueOf(cell[0]);
				long time1 = Long.valueOf(cell[1]);
				if((time-curTime)>=deltaT){
					//System.out.println(curTime);
					System.out.println(timeIndex+", "+curUsage);
					timeIndex++;
					curUsage = usage;
					curTime += deltaT;
				} else {
					curUsage += usage*((double)(time1-time)/deltaT);
				}
			}
			
			br.close();
			
			gzip.close();
			
			System.out.println(timeIndex+", "+curUsage);
		}
		
	}

}
