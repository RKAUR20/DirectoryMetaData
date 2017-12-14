package com.ace.DirectoryMetaData;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.ace.DirectoryMetaData.model.Directory;

public class DirectoryScannerRunnable implements Runnable {

	private String directoryPath;
	
	BlockingQueue<Directory> queue;
	
	private Map<String, Boolean> fileCache;
	
	public DirectoryScannerRunnable(String directoryPath, Map<String, Boolean> fileCache, BlockingQueue<Directory> queue) {
		super();
		this.directoryPath = directoryPath;
		this.fileCache = fileCache;
		this.queue = queue;
	}
	
	public List<String> listFiles(String folder){
		
		return null;
	}
	

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Directory scanning started");

		File root = new File(this.getDirectoryPath());
		String[] extensions = { "csv" };
		boolean recursive = true;

		Collection<File> files = FileUtils.listFiles(root, extensions, recursive);

		/* Filter files which are already in cache (means already processed), 
		 * then group all txt and csv files into their parent directory */
		Map<String, List<File>> directoryFilesMap = files.stream().filter(file -> !fileCache.containsKey(file.getAbsolutePath()))
				.collect(Collectors.groupingBy(File::getParent, HashMap::new,
						Collectors.mapping(Function.identity(), Collectors.toList())));

		System.out.println(directoryFilesMap);

		/*
		 * Processing Directory- Files Map and adding each 
		 * directory into blocking queue as well as putting all files in cache.
		 * */
		directoryFilesMap.forEach((key, value) -> {
			System.out.println("Processing directory :: " + key);
			System.out.println("Values :: "+ value);
			Directory directory = new Directory();
			directory.setName(key);
			
			value.stream().forEach(v -> {
				directory.getFiles().add(v.getAbsolutePath());
				fileCache.put(v.getAbsolutePath(), Boolean.FALSE);
			});
			
			System.out.println("Directory " + directory + " added in queue.");
			queue.add(directory);
			
			System.out.println(queue);

		});
		
		System.out.println("Directory scanning completed");
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

}
