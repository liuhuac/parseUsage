package edu.clemson.ece.parse.datacenter.usage.timeseries;

public class Parameters {
	public static String path = "./";
	public static String outpath = "./";
	public static int start = 0;
	public static int end = 500;
	public static int field = 5; 
		// 0 start time
		// 1 end time
		// 2 job ID
		// 3 task index
		// 4 machine ID
		// *5 CPU usage - mean and maximum in 1s window
		// *6 memory usage
		// 7 assigned memory
		// 8 unmapped page cache memory usage
		// 9 page cache memory usage
		// 10 maximum memory usage
		// 11 dis I/O time - mean
		// 12 local disk space used - mean
		// 13 CPU rate - max
		// 14 disk IO time - max
		// 15 cycles per instruction (CPI)
		// 16 memory accesses per instruction (MAI)
		// 17 sampling rate
		// 18 aggregation type
		// 19 sampled CPU usage
	
}
