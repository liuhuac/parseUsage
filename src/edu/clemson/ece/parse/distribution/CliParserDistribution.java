package edu.clemson.ece.parse.distribution;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliParserDistribution {

	public void parse(String[] args){
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption( "h", "help", false, "print usage of this program" );
		options.addOption( "f", "field", true, "field in the table to statistics" +
				 "\t0 start time" +
				 "\t1 end time" +
				 "\t2 job ID" +
				 "\t3 task index" +
				 "\t4 machine ID" +
				 "\t*5 CPU usage - mean and maximum in 1s window" +
				 "\t*6 memory usage" +
				 "\t7 assigned memory" +
				 "\t8 unmapped page cache memory usage" +
				 "\t9 page cache memory usage" +
				 "\t10 maximum memory usage" +
				 "\t11 dis I/O time - mean" +
				 "\t12 local disk space used - mean" +
				 "\t13 CPU rate - max" +
				 "\t14 disk IO time - max" +
				 "\t15 cycles per instruction (CPI)" +
				 "\t16 memory accesses per instruction (MAI)" +
				 "\t17 sampling rate" +
				 "\t18 aggregation type" +
				 "\t19 sampled CPU usage" );
		options.addOption( "p", "path", true, "path to trace file folder" );
		options.addOption( "o", "outpath", true, "output path folder" );
		options.addOption( "s", "start", true, "starting file index, e.g., 231 for part-00231-of-00500.csv.gz, default 0" );
		options.addOption( "e", "end", true, "ending file index, e.g., 231 for part-00231-of-00500.csv.gz, default 500" );



		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );

		    if( line.hasOption( "f" ) ) {
		    	Parameters.field = Integer.valueOf(line.getOptionValue("f"));
		    }
		    if( line.hasOption( "p" ) ) {
		    	Parameters.path = String.valueOf(line.getOptionValue("p"));
		    }
		    if( line.hasOption( "o" ) ) {
		    	Parameters.outpath = String.valueOf(line.getOptionValue("o"));
		    }
		    if( line.hasOption( "s" ) ) {
		    	Parameters.start = Integer.valueOf(line.getOptionValue("s"));
		    }
		    if( line.hasOption( "e" ) ) {
		    	Parameters.end = Integer.valueOf(line.getOptionValue("e"));
		    }
		    if( line.hasOption( "h" ) ) {
		    	HelpFormatter formatter = new HelpFormatter();
	    		formatter.printHelp( "GoogleClusterTraceParser-Find Task Usage Distribution, Author: Liuhua Chen", options );
	    		System.exit(0);
		    }
		}
		catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		    HelpFormatter formatter = new HelpFormatter();
    		formatter.printHelp( "GoogleClusterTraceParser-Find Task Usage Distribution, Author: Liuhua Chen", options );
    		System.exit(0);
		}
	}
	
}
