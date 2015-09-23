package edu.clemson.ece.parseUsage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ParseCPU {
	public static void process(String parent, String name) throws IOException {
		File input = new File(parent+"\\"+name);
		File output = new File(parent+"\\"+name+".txt");
		
		BufferedReader inputBr = new BufferedReader(new FileReader(input));
		BufferedWriter outputBw = new BufferedWriter(new FileWriter(output));
		
		String line;
		while((line=inputBr.readLine())!=null){
			if(!line.contains("all")) continue;
			String[] parts = line.split("\\s+");
			outputBw.write(parts[3]+"\n");
		}
		
		inputBr.close();
		outputBw.close();
	}
}
