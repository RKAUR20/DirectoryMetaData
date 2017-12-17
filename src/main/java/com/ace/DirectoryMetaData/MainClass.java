package com.ace.DirectoryMetaData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ace.DirectoryMetaData.model.Directory;
import com.ace.DirectoryMetaData.runnable.CacheRefreshRunnable;
import com.ace.DirectoryMetaData.runnable.DirectoryConsumerRunnable;
import com.ace.DirectoryMetaData.runnable.DirectoryScannerRunnable;

public class MainClass {
	
	/* It will have key as file absolute path and value as file last modified time stamp. 
	 * This cache will help in filtering out files which were already processed and also those whose MTD are up to date.
	*/
	static Map<String, Long> fileCache;
	
	/* This queue will have Directory object which contain name of directory and list of unprocessed file in that directory. 
	 * DirectoryScanner will poll changes and put object in this queue which will be consumed by Directory consumer.
	 */
	static BlockingQueue<Directory> queue;
	
	/* This program requires one command line parameter, path of directory from where scanning should start, 
	 * It also takes sort preference from property file, kept at src/main/resources/directorymetadata.properties.
	 */
	public static void main(String[] args) throws InterruptedException, IOException {

		String directoryPath = args[0];

		fileCache = new HashMap<String, Long>();

		queue = new LinkedBlockingQueue<>();

		/* Cache Refresh Task - this will clear fileCache hashmap so that scanner starts scanning from start. 
		 * This is required so as to capture deleted or renamed Files, as in case files already processed are renamed 
		 * or deleted they wont be picked by directory Scanner. So, DMTD and SMTD can be in inconsistent State. 
		 * This also deletes existing MTD, DMTD and SMTD files.
		 * This will also check if Sort preference kept in property file is changed to something else. It will reset to new sort preference.
		 * This task will be rescheduled after 24 Hours.
		 */
		ScheduledExecutorService refreshScheduler = Executors.newSingleThreadScheduledExecutor();
		CacheRefreshRunnable refreshTask = new CacheRefreshRunnable(fileCache, directoryPath);
		refreshScheduler.scheduleAtFixedRate(refreshTask, 0, 24, TimeUnit.HOURS);

		/* Directory Scanner task - this task will poll files from the given directory Path. 
		 * It will start after 1 minute, so by that time sort preferences are set by cache refresh task. 
		 * It will be rescheduled after 30 minutes and will scan newly created and as well as modified files.
		 */
		ScheduledExecutorService pollingScheduler = Executors.newSingleThreadScheduledExecutor();
		DirectoryScannerRunnable scannerTask = new DirectoryScannerRunnable(directoryPath, fileCache, queue);
		pollingScheduler.scheduleAtFixedRate(scannerTask, 1, 30, TimeUnit.MINUTES);
		
		//Directory Consumer task - It will consume Blocking Queue's entry and will process (create MTD, DMTD, SMTD) them. 
		DirectoryConsumerRunnable consumerTask = new DirectoryConsumerRunnable(queue);
		new Thread(consumerTask).start();

	}
	
}
