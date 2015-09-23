package edu.clemson.ece.parseUsage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class Parse {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Collection<File> files = FileUtils.listFiles(
				  new File("C:\\bak-DATA\\usage\\out"), 
				  new RegexFileFilter("^(.*?)"), 
				  DirectoryFileFilter.DIRECTORY
				);
		
		for(File f : files){
			String parent = f.getParent();
			String name = f.getName();
			if(name.contains("txt")) continue;
			
			if(parent.contains("CPU")){
				System.out.println("process CPU");
				ParseCPU.process(parent, name);
			} else if(parent.contains("IO")){
				System.out.println("process IO");
				ParseIO.process(parent, name);
			} else if(parent.contains("BW")){
				if(!name.contains("eth")) continue;
				System.out.println("process BW");
				ParseBW.process(parent, name);
			}
		}
	}

}
