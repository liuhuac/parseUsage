package edu.clemson.ece.parse.randomseries;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliParser {

	public void parse(String[] args){
		// create the command line parser
		CommandLineParser parser = new DefaultParser();

		// create the Options
		Options options = new Options();
		options.addOption( "h", "help", false, "print usage of this program" );
		options.addOption( "l", "length", true, "output length of trace (number of data points), default 288" );
		options.addOption( "n", "njobs", true, "number of jobs, default 200" );
		options.addOption( "p", "path", true, "path to trace file folder" );
		options.addOption( "o", "outpath", true, "output path folder" );
		options.addOption( "s", "start", true, "starting file index, e.g., 231 for part-00231-of-00500.csv.gz, default 0" );
		options.addOption( "e", "end", true, "ending file index, e.g., 231 for part-00231-of-00500.csv.gz, default 500" );



		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );

		    if( line.hasOption( "n" ) ) {
		    	Parameters.njobs = Integer.valueOf(line.getOptionValue("n"));
		    }
		    if( line.hasOption( "m" ) ) {
		    	Parameters.length = Integer.valueOf(line.getOptionValue("l"));
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
	    		formatter.printHelp( "GoogleClusterTraceParser, Author: Liuhua Chen", options );
	    		System.exit(0);
		    }
		}
		catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		    HelpFormatter formatter = new HelpFormatter();
    		formatter.printHelp( "GoogleClusterTraceParser, Author: Liuhua Chen", options );
    		System.exit(0);
		}
	}
	
}
