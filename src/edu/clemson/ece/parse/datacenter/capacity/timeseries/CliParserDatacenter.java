package edu.clemson.ece.parse.datacenter.capacity.timeseries;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliParserDatacenter {

	public void parse(String[] args){
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption( "h", "help", false, "print usage of this program" );
		options.addOption( "f", "field", true, "field in the table to statistics" +
				 "\t0 timestamp" +
				 "\t1 machine ID" +
				 "\t2 event type" +
				 "\t3 platform ID" +
				 "\t*4 capacity: CPU" +
				 "\t*5 capacity: memory");
		options.addOption( "o", "open", true, "open path to trace file file" );



		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );

		    if( line.hasOption( "f" ) ) {
		    	Parameters.field = Integer.valueOf(line.getOptionValue("f"));
		    }
		    if( line.hasOption( "o" ) ) {
		    	Parameters.path = String.valueOf(line.getOptionValue("o"));
		    }
		    if( line.hasOption( "h" ) ) {
		    	HelpFormatter formatter = new HelpFormatter();
	    		formatter.printHelp( "GoogleClusterTraceParser-Find Capacity Time Series, Author: Liuhua Chen", options );
	    		System.exit(0);
		    }
		}
		catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		    HelpFormatter formatter = new HelpFormatter();
    		formatter.printHelp( "GoogleClusterTraceParser-Find Capacity Time Series, Author: Liuhua Chen", options );
    		System.exit(0);
		}
	}
	
}
