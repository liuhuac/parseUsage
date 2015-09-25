package edu.clemson.ece.calculate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class CalculateCorrelation {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		List<File> files = allSubDirs("C:\\bak-DATA\\usage\\out\\grep-hdfs\\8178375.pbs01\\32");
		String type = "IO";
		String filter = "MEM.txt";
		
		for(File dir : files){
			
			if(!dir.getPath().contains(type)) continue;
			
			List<File> list = new ArrayList<>();
			for(File f : dir.listFiles()){
				String parent = f.getParent();
				String name = f.getName();		
				if(!name.contains(filter)||!parent.contains(type)) continue;	
				list.add(f);			
			}
			
			double[][] data = new double[12][];
			int minLen = 1000;
			for(int i=0; i<list.size(); i++){
				data[i] = read_profile(list.get(i).getPath());
				if(minLen>data[i].length){
					minLen = data[i].length;
				}
			}
			
			double[] ave = new double[list.size()];
			double[] dev = new double[list.size()];
			double[][] stand = new double[list.size()][];
			for(int i=0; i<list.size(); i++){
				ave[i] = average(data[i], data[i].length-minLen, data[i].length-1);
				dev[i] = deviation(data[i], ave[i], data[i].length-minLen, data[i].length-1);
				stand[i] = standard_value(data[i], ave[i], dev[i], data[i].length-minLen, data[i].length-1);
			}
			
			for(int i=0; i<list.size()-1; i++){
				for(int j=i+1; j<list.size(); j++){
					System.out.println(corr(stand[i], stand[j]));
				}
			}
		}
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
