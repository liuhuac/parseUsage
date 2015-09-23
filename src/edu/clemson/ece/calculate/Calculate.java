package edu.clemson.ece.calculate;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Calculate {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		Collection<File> files = FileUtils.listFiles(
//				  new File("C:\\bak-DATA\\usage\\out\\grep-hdfs\\8178375.pbs01"), 
//				  new NotFileFilter(TrueFileFilter.INSTANCE), 
//				  DirectoryFileFilter.DIRECTORY
//				);
		
		List<File> files = allSubDirs("C:\\bak-DATA\\usage\\out\\grep-hdfs\\8178375.pbs01");
		
		for(File dir : files){
			String parent = f.getParent();
			String name = f.getName();
			//if(name.contains("txt")) continue;
			
			System.out.println(f.getPath());
			
//			if(parent.contains("CPU")){
//				System.out.println("process CPU");
//				ParseCPU.process(parent, name);
//			} else if(parent.contains("IO")){
//				System.out.println("process IO");
//				ParseIO.process(parent, name);
//			} else if(parent.contains("BW")){
//				if(!name.contains("eth")) continue;
//				System.out.println("process BW");
//				ParseBW.process(parent, name);
//			}
			
		}
	}
	
	public static File[] subDirs(String dir){
		File file = new File(dir);
		File[] directories = file.listFiles(new FilenameFilter() {
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		return directories;
	}
	
	public static List<File> allSubDirs(String dir){
		List<File> res = new ArrayList<File>();
		File[] dirs = subDirs(dir);
		Queue<File> q = new LinkedList<>();
		for(File s : dirs){
			q.add(s);
		}
		while(!q.isEmpty()){
			File d = q.poll();
			File[] newdirs = subDirs(d.getPath());
			if(newdirs.length==0) {
				res.add(d);
			} else {
				for(File str : newdirs){
					q.add(str);
				}
			}
		}
		return res;
	}

}
