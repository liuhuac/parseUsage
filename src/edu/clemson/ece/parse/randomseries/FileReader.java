package edu.clemson.ece.parse.randomseries;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class FileReader {
	
	private int index;
	public BufferedReader br;
	
	public FileReader(String path, int index) throws IOException, IOException {
		super();
		this.index = index;
		
		GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(path+"part-"+String.format("%05d", this.index)+"-of-00500.csv.gz"));
		this.br = new BufferedReader(new InputStreamReader(gzip)); 
		
	}
	
}
