package com.ace.DirectoryMetaData.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.apache.commons.io.FilenameUtils;

import com.ace.DirectoryMetaData.model.FileResult;
import com.ace.DirectoryMetaData.model.ResultFileExtension;

public class OutputFilePublisher {

	public void createOutputFromResultList(FileResult fileResultList, ResultFileExtension extension) {
		// TODO Auto-generated method stub
		switch (extension) {
		case MTD:
			createMTDFileFromResultList(fileResultList);
			break;
		case SMTD:
			createSMTDFileFromResultList(fileResultList);
			break;
		case DMTD:
			createDMTDFileFromResultList(fileResultList);
			break;
		}
	}

	private void createDMTDFileFromResultList(FileResult fileResultList) {
		// TODO Auto-generated method stub
		PrintStream out;
		try {
			String directoryName = fileResultList.getFileName();
			System.out.println("Creating DMTD for : " + directoryName);
			System.out.println(directoryName + "/" + directoryName.substring(directoryName.lastIndexOf("\\") + 1));

			out = new PrintStream(
					new File(directoryName + "/" + directoryName.substring(directoryName.lastIndexOf("\\") + 1))
							+ ".dmtd");

			fileResultList.getResultMap().forEach((k,v) -> out.println(k + " : " + v));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createSMTDFileFromResultList(FileResult fileResultList) {
		// TODO Auto-generated method stub
		
	}

	private void createMTDFileFromResultList(FileResult fileResultList) {
		// TODO Auto-generated method stub
		PrintStream out;
		try {
			out = new PrintStream(new File(FilenameUtils.removeExtension(fileResultList.getFileName()) + ".mtd"));

			fileResultList.getResultMap().forEach((k,v) -> out.println(k + " : " + v));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
