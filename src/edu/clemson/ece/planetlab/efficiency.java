package edu.clemson.ece.planetlab;

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

public class efficiency {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		List<File> dirs = allSubDirs("G:\\planetlab1");
		Map<String,List<String>> map = new HashMap<>();
		
		for(File dir : dirs){
			for(File file : dir.listFiles()){
				String name = file.getName();
				String path = file.getPath();
				if(map.containsKey(name)){
					map.get(name).add(path);
				} else {
					List<String> l = new ArrayList<>();
					l.add(path);
					map.put(name, l);
				}
			}
		}
		
		for(String name : map.keySet()){
			List<String> list = map.get(name);
			if(list.size()==1) continue;

			double[][] data = new double[list.size()][288];
			for(int i=0; i<list.size(); i++){
				data[i] = read_profile(list.get(i));
			}
			
			double[] env = envelope(data);
			double[] profile = filter(env);
			double[] pattern = A2D(profile);
			
			for(int i=0; i<list.size()-1; i++){
				double sum_p = 0;
				double sum_d = 0;
				for(int j=0; j<pattern.length; j++){
					sum_p += pattern[j];
					sum_d += pattern[j]<data[i][j] ? pattern[j] : data[i][j];
				}
				System.out.println(sum_d/sum_p);
			}		
			
		}
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
        	if (input[i] < 10) {
        		input[i] = 10;
        	} else if (input[i] < 20) {
        		input[i] = 20;
        	} else if (input[i] < 30) {
        		input[i] = 30;
        	}
        	 else if (input[i] < 40) {
         		input[i] = 40;
         	}
        	 else if (input[i] < 50) {
         		input[i] = 50;
         	}
        	 else if (input[i] < 60) {
         		input[i] = 60;
         	}
        	 else if (input[i] < 70) {
         		input[i] = 70;
         	}
        	 else if (input[i] < 80) {
         		input[i] = 80;
         	}
        	 else if (input[i] < 90) {
         		input[i] = 90;
         	}
        	 else if (input[i] < 100) {
         		input[i] = 100;
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
}
