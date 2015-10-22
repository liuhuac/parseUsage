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


public class CalculateOffset {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		List<File> files = allSubDirs("C:\\Share\\ParseGoogleTrace\\6-10");
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
				
				for(int i=0; i<list.size()-1; i++){
					for(int j=i+1; j<list.size(); j++){
						System.out.println(findOffset(data[i], data[j]));
					}
				}
			}
		}
	}
	
	private static int findOffset(double[] a, double[] b){
		double[] shorter = a.length>b.length ? b : a;
		double[] longer = a.length>b.length ? a : b;
		
		double maxCorr = -1;
		int offset = 0;
		
		for(int x=longer.length-shorter.length; x>=0; x--){
			double ave_s = average(shorter, 0, shorter.length-1);
			double ave_l = average(longer, x, shorter.length+x-1);
			
			double dev_s = deviation(shorter, ave_s, 0, shorter.length-1);
			double dev_l = deviation(longer, ave_l, x, shorter.length+x-1);
			
			double[] stand_s = standard_value(shorter, ave_s, dev_s, 0, shorter.length-1);
			double[] stand_l = standard_value(longer, ave_l, dev_l, x, shorter.length+x-1);
			
			double corr = corr(stand_s, stand_l);
			
			if(corr>maxCorr){
				maxCorr = corr;
				offset = x-(longer.length-shorter.length);
			}
		}

		return a.length>b.length ? -offset : offset;
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
	
	private static double corr(double[] stand_a, double[] stand_b) {
		double sum = 0;
		int len = Math.min(stand_a.length, stand_b.length);
		for(int i=0; i<len; i++){
			sum += stand_a[i]*stand_b[i];
		}
		return sum/len;		
	}
	
	private static double[] standard_value(double[] a, double ave_a, double dev_a, int l, int r) {
		double[] res = new double[r-l+1];
		for(int i=l; i<=r; i++){
			res[i-l] = (a[i]-ave_a)/dev_a;
		}
		return res;		
	}
	
	private static double deviation(double[] a, double ave, int l, int r) {
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
