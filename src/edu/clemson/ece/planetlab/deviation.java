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

public class deviation {

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
			
			for(int i=0; i<list.size()-1; i++){
				for(int j=i+1; j<list.size(); j++){
					System.out.println(findOffset(data[i],data[j]));
				}
			}
			

			
			
			
		}
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
	
	private static int findOffset(double[] a, double[] b){
		double maxCorr = -1;
		int offset = 0;
		for(int x=-20; x<=20; x++){
			int al = x<0 ? -x : 0;
			int ar = x<0 ? a.length-1 : a.length-x-1;
			int bl = x<0 ? 0 : x;
			int br = x<0 ? a.length+x-1 : a.length-1;
			
			double ave_a = average(a, al, ar);
			double ave_b = average(b, bl, br);
			
			double dev_a = mydeviation(a, ave_a, al, ar);
			double dev_b = mydeviation(b, ave_b, bl, br);
			
			double[] stand_a = standard_value(a, ave_a, dev_a, al, ar);
			double[] stand_b = standard_value(b, ave_b, dev_b, bl, br);
			
			double corr = corr(stand_a, stand_b);
			
			if(corr>maxCorr){
				maxCorr = corr;
				offset = x;
			}
		}
		return offset;
	}
	
	private static double corr(double[] stand_a, double[] stand_b) {
		double sum = 0;
		for(int i=0; i<stand_a.length; i++){
			sum += stand_a[i]*stand_b[i];
		}
		return sum/(stand_a.length-1);		
	}
	
	private static double[] standard_value(double[] a, double ave_a, double dev_a, int l, int r) {
		double[] res = new double[r-l+1];
		for(int i=l; i<=r; i++){
			res[i-l] = (a[i]-ave_a)/dev_a;
		}
		return res;		
	}
	
	private static double mydeviation(double[] a, double ave, int l, int r) {
		double sum = 0;
		for(int i=l; i<=r; i++){
			sum += Math.pow(a[i]-ave, 2);
		}
		return Math.sqrt(sum/(r-l+1));
	}
	
	private static double average(double[] a, int l, int r) {
		double sum = 0;
		for(int i=l; i<=r; i++){
			sum += a[i];
		}
		return sum/(r-l+1);		
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
