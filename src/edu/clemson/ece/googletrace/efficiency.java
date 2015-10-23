package edu.clemson.ece.googletrace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class efficiency {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputFolderName = "G:\\workspace\\out\\cpu";
		File folder = new File(inputFolderName);
		
		Set<String> set = new HashSet();
		
		for(File f : folder.listFiles()){
			String name = f.getName();
			String pre = name.substring(0, name.indexOf(name.charAt(name.length()-1)));
			if(!set.add(pre)){
				continue;
			}
			
			double[][] a = new double[3][];
			a[0] = read_profile(inputFolderName+"\\"+pre+"a");
			a[1] = read_profile(inputFolderName+"\\"+pre+"b");
			a[2] = read_profile(inputFolderName+"\\"+pre+"c");
			
			double[] env = envelope(a);
			double[] profile = filter(env);
			//double[] pattern = filter(env);
			double[] pattern = A2D(profile);
			
			for(int i=0; i<3; i++){
				double sum_p = 0;
				double sum_d = 0;
				for(int j=0; j<pattern.length; j++){
					sum_p += pattern[j];
					sum_d += pattern[j]<a[i][j-(pattern.length-a[i].length)] ? pattern[j] : a[i][j-(pattern.length-a[i].length)];
				}
				System.out.println(sum_d/sum_p);
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
