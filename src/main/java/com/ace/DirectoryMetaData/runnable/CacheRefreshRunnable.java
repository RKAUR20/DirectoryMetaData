package com.ace.DirectoryMetaData.runnable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.SortOrder;
import com.ace.DirectoryMetaData.util.SortPreferrence;

public class CacheRefreshRunnable implements Runnable{

	private Map<String, Long> fileCache;
	private String directoryName;

	public CacheRefreshRunnable(Map<String, Long> fileCache, String directoryName) {
		super();
		this.fileCache = fileCache;
		this.directoryName = directoryName;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Cache Refresh started.");
		fileCache.clear();
		FileReader reader;
		Properties p = null;
		try {
			reader = new FileReader("src/main/resources/directorymetadata.properties");
			p=new Properties();  
		    p.load(reader); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	      
		String sortParam = p.getProperty("SORT_PARAM");

		String sortOrder = p.getProperty("SORT_ORDER");
	    
	    SortPreferrence.getInstance().setOrder("ASC".equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
	    SortPreferrence.getInstance().setParam(CountParam.getEnumFromParam(sortParam));
	    
		deleteAllMTDS(directoryName);
		System.out.println("Cache Refresh ended.");
	}

	private void deleteAllMTDS(String directoryName) {
		// TODO Auto-generated method stub
		System.out.println("Deleting existing MTD, DMTD and SMTD");
		File root = new File(directoryName);
		String[] extensions = { "mtd", "dmtd", "smtd"};
		boolean recursive = true;

		//Get all MTD files list in directory.
		Collection<File> allMTDfiles = FileUtils.listFiles(root, extensions, recursive);
		allMTDfiles.stream().forEach(file -> file.delete());
		System.out.println("Deleted existing MTD, DMTD and SMTD");
	}

}
