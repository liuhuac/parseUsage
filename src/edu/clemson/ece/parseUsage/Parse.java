package edu.clemson.ece.parseUsage;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class Parse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Collection<File> files = FileUtils.listFiles(
				  new File("C:\\bak-DATA\\usage\\out\\grep-hdfs\\8178375.pbs01"), 
				  new RegexFileFilter("^(.*?)"), 
				  DirectoryFileFilter.DIRECTORY
				);
		
		for(File f : files){
			//System.out.println(f.getParent());
			//System.out.println(f.getName());
			String parent = f.getParent();
			String name = f.getName();
			
			if(parent.contains("CPU")){
				System.out.println("process CPU");
			} else if(parent.contains("IO")){
				System.out.println("process IO");
			} else if(parent.contains("BW")){
				System.out.println("process BW");
			}
		}
	}

}
