package com.ace.DirectoryMetaData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ace.DirectoryMetaData.model.Directory;

public class MainClass {
	
	static Map<String, Boolean> fileCache;
	
	static BlockingQueue<Directory> queue;
	
	public static void main(String[] args) throws InterruptedException {

		/*String test = "D:\\apache-tomcat-7.0.82\\logs";
		
		System.out.println(test.lastIndexOf("\\"));*/
		
		String directoryPath = "D://";
		
		fileCache = new HashMap<String, Boolean>();
		
		queue = new LinkedBlockingQueue<>();
		
		
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		//ScheduledExecutorService consumerScheduler = Executors.newSingleThreadScheduledExecutor();

		DirectoryScannerRunnable scannerTask = new DirectoryScannerRunnable(directoryPath, fileCache,queue);
		DirectoryConsumerRunnable consumerTask = new DirectoryConsumerRunnable(fileCache,queue);

		scheduler.scheduleAtFixedRate(scannerTask, 0, 1, TimeUnit.MINUTES);
		
		//consumerScheduler.scheduleAtFixedRate(consumerTask, 5, 5, TimeUnit.SECONDS);
		
		new Thread(consumerTask).start();
		
	}
	
}
