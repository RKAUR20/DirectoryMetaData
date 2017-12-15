package com.ace.DirectoryMetaData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.Directory;
import com.ace.DirectoryMetaData.model.SortOrder;
import com.ace.DirectoryMetaData.runnable.DirectoryConsumerRunnable;
import com.ace.DirectoryMetaData.runnable.DirectoryScannerRunnable;

public class MainClass {
	
	/* It will have key as file absolute path and value as file last modified time stamp. 
	 * This cache will help in filtering out files which were already processed and also those whose MTD are up to date.
	*/
	static Map<String, Long> fileCache;
	
	/* This queue will have Directory object which contain name of directory and list of unprocessed file in that directory. 
	 * DirectoryScanner will pool changes and put object in this queue which will be consumed by Directory consume.
	 */
	static BlockingQueue<Directory> queue;
	
	public static void main(String[] args) throws InterruptedException {

		String directoryPath = args[0];

		String sortParam = args[1];

		String sortOrder = args[2];

		fileCache = new HashMap<String, Long>();

		queue = new LinkedBlockingQueue<>();

		ScheduledExecutorService pollingScheduler = Executors.newSingleThreadScheduledExecutor();
		// ScheduledExecutorService consumerScheduler =
		// Executors.newSingleThreadScheduledExecutor();

		DirectoryScannerRunnable scannerTask = new DirectoryScannerRunnable(directoryPath, fileCache, queue);

		DirectoryConsumerRunnable consumerTask = new DirectoryConsumerRunnable(queue,
				"ASC".equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC, CountParam.getEnumFromParam(sortParam));

		pollingScheduler.scheduleAtFixedRate(scannerTask, 0, 1, TimeUnit.MINUTES);

		// consumerScheduler.scheduleAtFixedRate(consumerTask, 5, 5, TimeUnit.SECONDS);

		new Thread(consumerTask).start();

	}
	
}
