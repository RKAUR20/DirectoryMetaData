package com.ace.DirectoryMetaData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import com.ace.DirectoryMetaData.model.Directory;
import com.ace.DirectoryMetaData.util.Constants;

public class DirectoryProcessorRunnable implements Runnable {
	
	Directory directory;

	public DirectoryProcessorRunnable(Directory directory) {
		super();
		this.directory = directory;
	}



	public void run() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName() + " started processing " + directory.getName());
		
		/*directory.getFiles().forEach();*/

	}



	private void createMetaDataForFile(Map<String, Long> occurence, String fileName) {
		// TODO Auto-generated method stub
		/*File f = new File(fileName);
		if (f.exists())
		{
		   f.delete();
		}
		FileWriter out = new FileWriter(f); 
		out.write(occurence.forEach((k,v) -> k +" : " + v));
		out.close();*/
	}
	

}
