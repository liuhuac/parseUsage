package edu.clemson.ece.googletrace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class offset {

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
			
			System.out.println(findOffset(a,b));
			System.out.println(findOffset(b,c));
			System.out.println(findOffset(a,c));		
		}
	}
	
	private static int findOffset(double[] a, double[] b){
		double maxCorr = -1;
		int offset = 0;
		for(int x=-10; x<=10; x++){
			int al = x<0 ? -x : 0;
			int ar = x<0 ? a.length-1 : a.length-x-1;
			int bl = x<0 ? 0 : x;
			int br = x<0 ? a.length+x-1 : a.length-1;
			
			double ave_a = average(a, al, ar);
			double ave_b = average(b, bl, br);
			
			double dev_a = deviation(a, ave_a, al, ar);
			double dev_b = deviation(b, ave_b, bl, br);
			
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
