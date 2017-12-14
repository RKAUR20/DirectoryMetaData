package com.ace.DirectoryMetaData;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ace.DirectoryMetaData.model.Directory;

public class DirectoryConsumerRunnable implements Runnable{
	
	ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	Map<String, Boolean> fileCache;
	
	BlockingQueue<Directory> queue;

	public DirectoryConsumerRunnable(Map<String, Boolean> fileCache, BlockingQueue<Directory> queue) {
		super();
		this.fileCache = fileCache;
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Consumer started");
		
		while(true) {
			try {
				Directory directory = queue.take();
				System.out.println(directory.getName() + " received for processing.");
				executorService.submit(new DirectoryProcessorRunnable(directory));
				System.out.println(directory.getName() + " submitted for processing.");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
