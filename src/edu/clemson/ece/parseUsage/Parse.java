package edu.clemson.ece.parseUsage;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class Parse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Collection files = FileUtils.listFiles(
				  new File("D:\\workspace"), 
				  new RegexFileFilter("^(.*?)"), 
				  DirectoryFileFilter.DIRECTORY
				);
	}

}
