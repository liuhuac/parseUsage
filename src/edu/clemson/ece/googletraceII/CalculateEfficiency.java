package edu.clemson.ece.googletraceII;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class CalculateEfficiency {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		List<File> files = allSubDirs("C:\\Share\\ParseGoogleTrace\\0-5");
		String type = "mem";
//		String filter = "MEM.txt";
		
//		String type = "IO";
//		String filter = "IO.txt";
		
//		String type = "BW";
//		String filter = "RX.txt";
		
//		String type = "BW";
//		String filter = "TX.txt";
		
//		String type = "CPU";
//		String filter = "txt";
		
		for(File dir : files){
			
			if(!dir.getPath().contains(type)) continue;
			
			
			Map<String, List<File>> map = new HashMap<>();
			for(File f : dir.listFiles()){
				//String parent = f.getParent();
				String name = f.getName();
				String key = name.substring(0,name.indexOf('-'));
				if(map.containsKey(key)){
					map.get(key).add(f);
				} else {
					List<File> list = new ArrayList<>();
					list.add(f);
					map.put(key, list);
				}

			}
			
			for(Map.Entry<String, List<File>> entry : map.entrySet()){
			
				List<File> list = entry.getValue();
				
				double[][] data = new double[list.size()][];
				for(int i=0; i<list.size(); i++){
					data[i] = read_profile(list.get(i).getPath());
				}
				
				double[] env = envelope(data);
				double[] profile = filter(env);
				double[] pattern = A2D(profile);
				
				for(int i=0; i<list.size()-1; i++){
					double sum_p = 0;
					double sum_d = 0;
					for(int j=pattern.length-data[i].length; j<pattern.length; j++){
						sum_p += pattern[j];
						sum_d += pattern[j]<data[i][j-(pattern.length-data[i].length)] ? pattern[j] : data[i][j-(pattern.length-data[i].length)];
					}
					System.out.println(sum_d/sum_p);
				}
			}
		}
	}
	
	public static double[] envelope(double[][] input){
		int max = -1;
		for(int i=0; i<input.length; i++){
			if(max<input[i].length){
				max = input[i].length;
			}
		}
		
		double[] res = new double[max];
		
		for(int i=0; i<input.length; i++){
			for(int j=max-input[i].length; j<max; j++){
				if(res[j]<input[i][j-(max-input[i].length)]){
					res[j] = input[i][j-(max-input[i].length)];
				}
			}
		}
		
		return res;
	}
	
	public static double[] filter(double[] input) {

		double ALPHA = 0.8; //OSK: lower ALPHA leads to smoother curve
		double prev = input[0];
		
        for ( int i=0; i<input.length; i++ ) {
        	if (i==0) {
        		prev = input[0];
        	} else {
        		prev = input[i-1];
        	}
        	input[i] = ALPHA * input[i] + (1 - ALPHA) * prev;
        }
        return input;
    }
	
	public static double[] A2D(double[] input) {

        for ( int i=0; i<input.length; i++ ) {
        	if (input[i] < 0.01) {
        		input[i] = 0.01;
        	} else if (input[i] < 0.02) {
        		input[i] = 0.02;
        	} else if (input[i] < 0.03) {
        		input[i] = 0.03;
        	}
        	 else if (input[i] < 0.04) {
         		input[i] = 0.04;
         	}
        	 else if (input[i] < 0.05) {
         		input[i] = 0.05;
         	}
        	 else if (input[i] < 0.06) {
         		input[i] = 0.06;
         	}
        	 else if (input[i] < 0.07) {
         		input[i] = 0.07;
         	}
        	 else if (input[i] < 0.08) {
         		input[i] = 0.08;
         	}
        	 else if (input[i] < 0.09) {
         		input[i] = 0.09;
         	}
        	 else if (input[i] < 0.1) {
         		input[i] = 0.1;
         	} else {
         		System.exit(0);
         	}
        	
        }
        return input;
    }
	
	public static double[] flat(double[] input) {

		int window = 15;
		
        for ( int i=0; i<input.length-window; i++ ) {        	
        	for (int j=1; j<window; j++) {
        		if(input[i] < input[i+j]) {
        			input[i] = input[i+j];
        		}
        	}
        }
        for ( int i=input.length-window; i<input.length; i++ ) { 
        	for (int j=i; j<input.length; j++) {
        		if(input[i] < input[j]) {
        			input[i] = input[j];
        		}
        	}
        }
        return input;
    }
	
	public static File[] subDirs(String dir){
		File file = new File(dir);
		File[] directories = file.listFiles(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return directories;
	}
	
	public static List<File> allSubDirs(String dir){
		List<File> res = new ArrayList<File>();
		File[] dirs = subDirs(dir);
		Queue<File> q = new LinkedList<>();
		for(File s : dirs){
			q.add(s);
		}
		while(!q.isEmpty()){
			File d = q.poll();
			File[] newdirs = subDirs(d.getPath());
			if(newdirs.length==0) {
				res.add(d);
			} else {
				for(File str : newdirs){
					q.add(str);
				}
			}
		}
		return res;
	}

	private static double[] read_profile(String fileName) throws IOException {
		List<Double> profile = new ArrayList<>();
		BufferedReader inputBr = new BufferedReader(new FileReader(fileName));	
		String line;
		while((line=inputBr.readLine())!=null){
			profile.add(Double.valueOf(line));
		}
		inputBr.close();
		
		double[] res = new double[profile.size()];
		for(int i=0; i<profile.size(); i++){
			res[i] = profile.get(i).doubleValue();
		}
		return res;
	}

}
