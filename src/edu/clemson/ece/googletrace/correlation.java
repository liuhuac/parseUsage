package edu.clemson.ece.googletrace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class correlation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String inputFolderName = "G:\\workspace\\out\\mem";
		File folder = new File(inputFolderName);
		
		Set<String> set = new HashSet();
		
		for(File f : folder.listFiles()){
			String name = f.getName();
			String pre = name.substring(0, name.indexOf(name.charAt(name.length()-1)));
			if(!set.add(pre)){
				continue;
			}
			
			double[] a = read_profile(inputFolderName+"\\"+pre+"a");
			double[] b = read_profile(inputFolderName+"\\"+pre+"b");
			double[] c = read_profile(inputFolderName+"\\"+pre+"c");
			
			double ave_a = average(a);
			double ave_b = average(b);
			double ave_c = average(c);
			
			double dev_a = deviation(a, ave_a);
			double dev_b = deviation(b, ave_b);
			double dev_c = deviation(c, ave_c);
			
			double[] stand_a = standard_value(a, ave_a, dev_a);
			double[] stand_b = standard_value(b, ave_b, dev_b);
			double[] stand_c = standard_value(c, ave_c, dev_c);
			
			
			System.out.println(corr(stand_a, stand_b));
			System.out.println(corr(stand_a, stand_c));
			System.out.println(corr(stand_b, stand_c));
			
		}
	}
	
	private static double corr(double[] stand_a, double[] stand_b) {
		double sum = 0;
		for(int i=0; i<stand_a.length; i++){
			sum += stand_a[i]*stand_b[i];
		}
		return sum/(stand_a.length-1);		
	}
	
	private static double[] standard_value(double[] a, double ave_a, double dev_a) {
		double[] res = new double[a.length];
		for(int i=0; i<a.length; i++){
			res[i] = (a[i]-ave_a)/dev_a;
		}
		return res;		
	}
	
	private static double average(double[] a) {
		double sum = 0;
		for(int i=0; i<a.length; i++){
			sum += a[i];
		}
		return sum/a.length;		
	}
	
	private static double deviation(double[] a, double ave) {
		double sum = 0;
		for(int i=0; i<a.length; i++){
			sum += Math.pow(a[i]-ave, 2);
		}
		return Math.sqrt(sum/(a.length-1));		
	}

	private static double[] read_profile(String fileName) {
		int length = 288;
		double[] profile = new double[length];
		File file = new File(fileName);
		Scanner scan;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {
			scan = null;
			e.printStackTrace();
		}
		for(int i = 0; i < length; i++) {
			String str = scan.nextLine();
			profile[i] = Double.valueOf(str);
		}
		scan.close();
		return profile;
	}
}
