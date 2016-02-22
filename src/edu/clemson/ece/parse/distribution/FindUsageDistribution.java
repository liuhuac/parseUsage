package edu.clemson.ece.parse.distribution;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class FindUsageDistribution {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Parameters.end = 1;
		CliParserDistribution cli = new CliParserDistribution();
		cli.parse(args);
		
		
		int[] usageCount = new int[Parameters.range]; // typical usage 0.03955
		long totalRecords = 0;
		int errorPoints = 0;
		
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
				
				double usage = Double.valueOf(cell[Parameters.field]);
				int index = (int)(usage*Parameters.range);
				if(index>=Parameters.range||index<0){
					// ignore strange data point, e.g.,
					// 12900000000,13054000000,6000618870,69,2790482470,4.336, <--- error point
					//       0.01703,0.02524,0.0007486,0.002155,0.01706,0,0,
					//       5.297,0,3.722,0.009639,0,0,1.907e-06
					// System.out.println("/part-"+String.format("%05d", i)+"-of-00500.csv.gz");
					errorPoints++;
					continue;
				}
				usageCount[index]++;
				totalRecords++;
			}
			
			br.close();
		}
		
		File outfile = new File(Parameters.outpath+"distribution");
		outfile.getParentFile().mkdirs();
		if (!outfile.exists()) {
			outfile.createNewFile();
		} else {
			System.out.println(Parameters.outpath+"distribution already exist");
			System.exit(0);
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(outfile.getAbsoluteFile()));
		
		for(int i=0; i<Parameters.range; i++){
			bw.write(String.format("%.5f\t%d\n", (double)i/Parameters.range, usageCount[i]));
			bw.flush();
		}
		bw.close();
		
		
		File outfileConcise = new File(Parameters.outpath+"dist-concise");
		outfileConcise.getParentFile().mkdirs();
		if (!outfileConcise.exists()) {
			outfileConcise.createNewFile();
		} else {
			System.out.println(Parameters.outpath+"dist-concise already exist");
			System.exit(0);
		}
		BufferedWriter bwConcise = new BufferedWriter(new FileWriter(outfileConcise.getAbsoluteFile()));
		
		int count = 0;
		for(int i=0; i<Parameters.range; i++){
			if((i+1)%1000==0){
				bwConcise.write(String.format("%.5f\t%d\n", (double)i/Parameters.range, count));
				bwConcise.flush();
				count = 0;
			} else {
				count += usageCount[i];
			}
		}
		bwConcise.close();
		
		
		System.out.println("Processed "+totalRecords+" records!");
		System.out.println("Excluding "+errorPoints+" error records!");
	}

}
