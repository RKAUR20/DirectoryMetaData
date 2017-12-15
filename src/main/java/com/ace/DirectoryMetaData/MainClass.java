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
import com.ace.DirectoryMetaData.runnable.DirectoryConsumerRunnable;
import com.ace.DirectoryMetaData.runnable.DirectoryScannerRunnable;

public class MainClass {
	
	//It will have key as file absolute path and value as file processed status (TRUE processed, False not processed)
	static Map<String, Boolean> fileCache;
	
	/* This queue will have Directory object which contain name of directory and list of unprocessed file in that directory. 
	 * DirectoryScanner will pool changes and put object in this queue which will be consumed by Directory consume.
	 */
	static BlockingQueue<Directory> queue;
	
	public static void main(String[] args) throws InterruptedException {

		String directoryPath = "C://Users//rkau23";
		
		fileCache = new HashMap<String, Boolean>();
		
		queue = new LinkedBlockingQueue<>();
		
		
		ScheduledExecutorService pollingScheduler = Executors.newSingleThreadScheduledExecutor();
		//ScheduledExecutorService consumerScheduler = Executors.newSingleThreadScheduledExecutor();

		DirectoryScannerRunnable scannerTask = new DirectoryScannerRunnable(directoryPath, fileCache,queue);
		DirectoryConsumerRunnable consumerTask = new DirectoryConsumerRunnable(fileCache,queue);

		pollingScheduler.scheduleAtFixedRate(scannerTask, 0, 1, TimeUnit.MINUTES);
		
		//consumerScheduler.scheduleAtFixedRate(consumerTask, 5, 5, TimeUnit.SECONDS);
		
		new Thread(consumerTask).start();
		
	}
	
}
