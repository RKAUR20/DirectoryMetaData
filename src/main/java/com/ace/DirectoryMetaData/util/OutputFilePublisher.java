package com.ace.DirectoryMetaData.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.FileResult;
import com.ace.DirectoryMetaData.model.OutputFileExtension;
import com.ace.DirectoryMetaData.model.SortOrder;

public class OutputFilePublisher {

	public void createOutputFromFileResult(FileResult fileResult, OutputFileExtension extension) {
		// TODO Auto-generated method stub
		switch (extension) {
		case MTD:
			createMTDFileFromResultList(fileResult);
			break;
		case DMTD:
			createDMTDFileFromResultList(fileResult);
			break;
		default:
			break;
		}
	}

	private void createDMTDFileFromResultList(FileResult fileResult) {
		// TODO Auto-generated method stub
		PrintStream out;
		try {
			String directoryName = fileResult.getFileName();
			System.out.println("Creating DMTD for directory : " + directoryName);

			out = new PrintStream(
					new File(directoryName + "/" + directoryName.substring(directoryName.lastIndexOf("\\") + 1))
							+ ".dmtd");

			this.writeResultMapToFile(fileResult.getResultMap(), out);

			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeResultMapToFile(Map<CountParam, Long> resultMap, PrintStream out) {
		// TODO Auto-generated method stub
		resultMap.forEach((k,v) -> out.println(k + " : " + v));
	}

	public void createSMTDFileFromResultList(List<FileResult> fileResultList, String directoryName, SortOrder sortOrder, CountParam sortParam) {
		// TODO Auto-generated method stub
		PrintStream out;
		try {
			System.out.println("Creating SMTD for directory : " + directoryName);
			
			out = new PrintStream(
					new File(directoryName + "/" + directoryName.substring(directoryName.lastIndexOf("\\") + 1))
							+ ".smtd");
			
			out.println("Result Sorted " + sortOrder + " on " + sortParam);

			fileResultList.stream().forEach(fileResult -> {
				out.println(fileResult.getFileName());
				this.writeResultMapToFile(fileResult.getResultMap(), out);
				out.println();
			});
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createMTDFileFromResultList(FileResult fileResult) {
		// TODO Auto-generated method stub
		PrintStream out;
		try {
			System.out.println("Creating MTD for file : " + fileResult.getFileName());
			out = new PrintStream(new File(FilenameUtils.removeExtension(fileResult.getFileName()) + ".mtd"));
			this.writeResultMapToFile(fileResult.getResultMap(), out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
