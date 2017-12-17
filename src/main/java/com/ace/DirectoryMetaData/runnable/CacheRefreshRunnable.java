package com.ace.DirectoryMetaData.runnable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.SortOrder;
import com.ace.DirectoryMetaData.util.SortPreferrence;

public class CacheRefreshRunnable implements Runnable{

	private Map<String, Long> fileCache;

	public CacheRefreshRunnable(Map<String, Long> fileCache) {
		super();
		this.fileCache = fileCache;
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
		System.out.println("Cache Refresh ended.");
	}

}
