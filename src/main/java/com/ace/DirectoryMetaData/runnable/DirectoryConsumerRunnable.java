package com.ace.DirectoryMetaData.runnable;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ace.DirectoryMetaData.model.Directory;
import com.ace.DirectoryMetaData.processor.DirectoryProcessor;
import com.ace.DirectoryMetaData.processor.impl.DirectoryProcessorImpl;

public class DirectoryConsumerRunnable implements Runnable{
	
	ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	Map<String, Boolean> fileCache;
	
	BlockingQueue<Directory> queue;
	
	private DirectoryProcessor directoryProcessor;

	public DirectoryProcessor getDirectoryProcessor() {
		return directoryProcessor;
	}

	public void setDirectoryProcessor(DirectoryProcessor directoryProcessor) {
		this.directoryProcessor = directoryProcessor;
	}

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
				directoryProcessor = new DirectoryProcessorImpl(directory);
				directoryProcessor.processDirectory();
				System.out.println(directory.getName() + " processed.");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
