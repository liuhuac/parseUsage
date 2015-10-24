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
import java.util.Map.Entry;
import java.util.Set;

public class CountNumberOfTasks {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Parameters.end = 1;
		CliParser cli = new CliParser();
		cli.parse(args);
		
		List<FileReader> readerList = new ArrayList<>();
		for(int i=Parameters.start; i<=Parameters.end; i++){
			readerList.add(new FileReader(Parameters.path, i));
		}
		
		Map<String,Set<String>> map = new HashMap<>();
		
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
				
				if(!map.containsKey(cell[2])){			
					if(map.size()>Parameters.njobs){
						continue;
					} else {
						Set<String> s = new HashSet<>();
						s.add(cell[3]);
						map.put(cell[2],s);
					}
				} else {
					Set<String> s = map.get(cell[2]);
					if(s.contains(cell[3])) continue;
					s.add(cell[3]);
				}
			}
		}
		
		for(Entry<String, Set<String>> ent : map.entrySet()){
			for(String s : ent.getValue()){
				System.out.println(ent.getKey()+" "+s);
			}
		}
	}

}
