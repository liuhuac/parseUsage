package edu.clemson.ece.parseUsage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ParseBW {
	public static void process(String parent, String name) throws IOException {
		File input = new File(parent+"\\"+name);
		File output1 = new File(parent+"\\"+name+"_RX.txt");
		File output2 = new File(parent+"\\"+name+"_TX.txt");
		
		BufferedReader inputBr = new BufferedReader(new FileReader(input));
		BufferedWriter outputBw1 = new BufferedWriter(new FileWriter(output1));
		BufferedWriter outputBw2 = new BufferedWriter(new FileWriter(output2));
		
		String line;
		while((line=inputBr.readLine())!=null){
			if(line.contains("Monitoring")) continue;
			String[] parts = line.split("\\s+");
			outputBw1.write(parts[2]+"\n");
			outputBw2.write(parts[5]+"\n");
		}
		
		inputBr.close();
		outputBw1.close();
		outputBw2.close();
	}
}
