package edu.clemson.ece.parseUsage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ParseIO {
	public static void process(String parent, String name) throws IOException {
		File input = new File(parent+"\\"+name);
		File outputIO = new File(parent+"\\"+name+"_IO.txt");
		File outputMEM = new File(parent+"\\"+name+"_MEM.txt");
		
		BufferedReader inputBr = new BufferedReader(new FileReader(input));
		BufferedWriter outputBw1 = new BufferedWriter(new FileWriter(outputIO));
		BufferedWriter outputBw2 = new BufferedWriter(new FileWriter(outputMEM));
		
		String line;
		while((line=inputBr.readLine())!=null){
			if(line.contains("procs")||line.contains("free")) continue;
			String[] parts = line.split("\\s+");
			outputBw1.write(parts[10]+"\n");
			outputBw2.write(parts[4]+"\n");
		}
		
		inputBr.close();
		outputBw1.close();
		outputBw2.close();
	}
}
