package com.ace.DirectoryMetaData.runnable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.FileResult;
import com.ace.DirectoryMetaData.model.OutputFileExtension;
import com.ace.DirectoryMetaData.util.OutputFilePublisher;

public class FileProcessorRunnable implements Callable<FileResult>{
	
	private String fileName;
	
	private OutputFilePublisher outputFilePublisher;

	public FileProcessorRunnable(String fileName) {
		super();
		this.fileName = fileName;
		outputFilePublisher = new OutputFilePublisher();
	}

	@Override
	public FileResult call() {
		// TODO Auto-generated method stub
		
		System.out.println(Thread.currentThread().getName() + " processing " + this.fileName);

		FileResult fileResult = new FileResult(this.fileName);

		System.out.println("Calculating File Result for "  + this.fileName);
		Map<CountParam, Long> resultMap = this.calculateFileResult();

		fileResult.setResultMap(resultMap);
		
		System.out.println("Calling Output publisher for publishing MTD for "  + this.fileName);
		outputFilePublisher.createOutputFromFileResult(fileResult, OutputFileExtension.MTD);
		
		System.out.println(Thread.currentThread().getName() + " finished processing " + this.fileName);
		return fileResult;

	}


	private Map<CountParam, Long> calculateFileResult() {
		// TODO Auto-generated method stub
		String words;
		String line;
		Long countword = 0l;
		Long countCharacters = 0l;
		Long vowelCount = 0l;
		Map<CountParam, Long> resultMap = new HashMap<>();

		Scanner input;
		try {
			input = new Scanner(new FileReader(this.fileName));

			if (!input.hasNext()) {
				System.out.println("File is empty. Setting default resultMap with 0 count");
				this.getDefaultResultMap(resultMap);
				return resultMap;
			}

			while (input.hasNextLine()) {
				line = input.nextLine();
				Scanner inLine = new Scanner(line);
				while (inLine.hasNext()) {
					words = inLine.next();
					countword++;
				}
				countCharacters += line.length();
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if ((c == 'a') || (c == 'e') || (c == 'i') || (c == 'o') || (c == 'u'))
						vowelCount++;
				}
			}

			System.out.println("Number of words: " + countword);
			System.out.println("Number of vowels: " + vowelCount);
			System.out.println("Number of letters: " + countCharacters);

			resultMap.put(CountParam.WORDS, countword);
			resultMap.put(CountParam.LETTERS, countCharacters);
			resultMap.put(CountParam.VOWELS, vowelCount);
			resultMap.put(CountParam.SPECIAL_CHAR, 0l);
			
			input.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultMap;
	}

	private void getDefaultResultMap(Map<CountParam, Long> resultMap) {
		// TODO Auto-generated method stub
		resultMap.put(CountParam.WORDS, 0l);
		resultMap.put(CountParam.LETTERS, 0l);
		resultMap.put(CountParam.VOWELS, 0l);
		resultMap.put(CountParam.SPECIAL_CHAR, 0l);
	}

}
