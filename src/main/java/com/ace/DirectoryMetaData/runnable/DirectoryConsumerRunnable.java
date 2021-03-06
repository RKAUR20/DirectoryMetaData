package com.ace.DirectoryMetaData.runnable;

import java.util.concurrent.BlockingQueue;

import com.ace.DirectoryMetaData.model.Directory;
import com.ace.DirectoryMetaData.processor.DirectoryProcessor;
import com.ace.DirectoryMetaData.processor.impl.DirectoryProcessorImpl;

public class DirectoryConsumerRunnable implements Runnable{
	
	BlockingQueue<Directory> queue;
	
	private DirectoryProcessor directoryProcessor;

	public DirectoryProcessor getDirectoryProcessor() {
		return directoryProcessor;
	}

	public void setDirectoryProcessor(DirectoryProcessor directoryProcessor) {
		this.directoryProcessor = directoryProcessor;
	}

	public DirectoryConsumerRunnable(BlockingQueue<Directory> queue) {
		super();
		this.queue = queue;
		directoryProcessor = new DirectoryProcessorImpl();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Consumer started");
		
		while(true) {
			try {
				Directory directory = queue.take();
				System.out.println(directory.getName() + " received for processing.");
				directoryProcessor.processDirectory(directory);
				System.out.println(directory.getName() + " processed.");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
