package com.ace.DirectoryMetaData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.ace.DirectoryMetaData.comparator.WordSortComparator;
import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.CountResult;
import org.apache.commons.io.FileUtils;

import com.ace.DirectoryMetaData.model.Directory;
import com.ace.DirectoryMetaData.model.FileResult;
import com.ace.DirectoryMetaData.model.ResultFileExtension;
import com.ace.DirectoryMetaData.model.SortOrder;
import com.ace.DirectoryMetaData.util.OutputFilePublisher;

public class DirectoryProcessor {
	
	ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	Directory directory;
	
	private OutputFilePublisher outputFilePublisher;

	public DirectoryProcessor(Directory directory) {
		this.directory = directory;
		outputFilePublisher = new OutputFilePublisher();
	}

	public void processDirectory() {
		// TODO Auto-generated method stub
		System.out.println("Processing of " + directory.getName() + " started.");

		List<Future<FileResult>> futureFileResultList = new ArrayList<>();
		
		// Submitting all the new files in directory to executor for MTD creation.
		directory.getFiles().forEach(file -> {
			Future<FileResult> futureFileResult = executorService.submit(new FileProcessorRunnable(file));
			futureFileResultList.add(futureFileResult);
			System.out.println(file + " submited for .mtd creation");
		});
		
		
		/* Since new files have been added to the directory, 
		 * so value of DMTD and SMTD would have been changed.
		 * Thus, meanwhile fetch already created MTD files and 
		 * get their Result into FileResult object for calculating new DMTD and SMTD
		 * and let other threads calculate MTD result for new files added in the directory..
		 */
		List<FileResult> fileResultList = this.getAlreadyCalculatedMTDResult();
		System.out.println("Already calculated MTD");
		fileResultList.stream().forEach(fileResult ->{
			System.out.println(fileResult.getFileName());
			fileResult.getResultMap().forEach( (k,v) -> System.out.println(k + " : " + v));
		});
		System.out.println();
		
		//Adding new files MTD to already existing MTD Result list
		futureFileResultList.stream().forEach(future -> {
			try {
				fileResultList.add(future.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		System.out.println(directory.getName() + "Submitted for DMTD and SMTD");
		executorService.submit(() -> this.calculateAndCreateDMTDResult(fileResultList));
		executorService.submit(() -> this.calculateAndCreateSMTDResult(fileResultList));
		System.out.println("Processing of " + directory.getName() + " ended.");

	}

	private void calculateAndCreateSMTDResult(List<FileResult> fileResultList) {
		// TODO Auto-generated method stub
		System.out.println("calculateAndCreateSMTDResult");
		Collections.sort(fileResultList, new WordSortComparator(CountParam.WORDS, SortOrder.DESC));
		fileResultList.stream().forEach(fileResult -> {
			System.out.println(fileResult.toString());
		});
	}

	private void calculateAndCreateDMTDResult(List<FileResult> fileResultList) {
		// TODO Auto-generated method stub
		FileResult directoryResult = new FileResult(directory.getName());
		Long letterCount = 0l, vowelCount = 0l, wordCount = 0l, specialCharCount = 0l;

		for (FileResult fileResult : fileResultList) {
			for (Map.Entry<CountParam, Long> entry : fileResult.getResultMap().entrySet()) {
				switch (entry.getKey()) {
				case LETTERS:
					letterCount = letterCount + entry.getValue();
					break;
				case VOWELS:
					vowelCount = vowelCount + entry.getValue();
					break;
				case WORDS:
					wordCount = wordCount + entry.getValue();
					break;
				case SPECIAL_CHAR:
					specialCharCount = specialCharCount + entry.getValue();
					break;
				}
			}

		}

		Map<CountParam, Long> directoryResultMap = new HashMap<>();
		directoryResultMap.put(CountParam.LETTERS, letterCount);
		directoryResultMap.put(CountParam.VOWELS, vowelCount);
		directoryResultMap.put(CountParam.WORDS, wordCount);
		directoryResultMap.put(CountParam.SPECIAL_CHAR, specialCharCount);
		directoryResult.setResultMap(directoryResultMap);

		outputFilePublisher.createOutputFromResultList(directoryResult, ResultFileExtension.DMTD);

		System.out.println("calculateAndCreateDMTDResult");
	}

	private List<FileResult> getAlreadyCalculatedMTDResult() {
		// TODO Auto-generated method stub
		File root = new File(this.directory.getName());
		String[] extensions = { "mtd" };
		boolean recursive = false;

		//Get all MTD files list in directory.
		Collection<File> allMTDfiles = FileUtils.listFiles(root, extensions, recursive);
		
		/* There could be a case that while we are fetching existing MTDs, 
		 * executor thread has created MTD for new file. So filtering out new MTDS. */
		Collection<File> filterMTDs = allMTDfiles.stream()
				.filter(file -> exists(directory.getFiles(),file.getAbsolutePath())).collect(Collectors.toList());

		return this.convertMTDsToFileResultList(filterMTDs);
	}

	private Boolean exists(List<String> files, String absolutePath) {
		// TODO Auto-generated method stub
		for (String file : files) {
			if (file.equals(absolutePath))
				return true;
		}
		return false;
	}

	private List<FileResult> convertMTDsToFileResultList(Collection<File> filterMTDs) {
		// TODO Auto-generated method stub
		List<FileResult> fileResultList = new ArrayList<>();

		filterMTDs.stream().forEach(mtd -> {
			FileResult fileResult = new FileResult(mtd.getAbsolutePath());

			Map<CountParam, Long> resultMap = this.getCountResultFromFile(mtd.getAbsolutePath());
			System.out.println("File Result of :: " + mtd.getAbsolutePath());
			resultMap.forEach((k,v) -> System.out.println(k +" : " + v));
			fileResult.setResultMap(resultMap);
			fileResultList.add(fileResult);
		});

		return fileResultList;
	}

	private Map<CountParam, Long> getCountResultFromFile(String filePath) {
		// TODO Auto-generated method stub
		Map<CountParam, Long> resultMap = new HashMap<>();
		
		Scanner input;
		String line;
		try {
			
			input = new Scanner(new FileReader(filePath));

			if (!input.hasNext()) {
				System.out.println("File is empty. Aborting Program");
				return null;
			}

			while (input.hasNextLine()) {
				
				line = input.nextLine();
				String[] splitResult = line.split(" : ");
				resultMap.put(CountParam.getEnumFromParam(splitResult[0]), Long.parseLong(splitResult[1]));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

}
