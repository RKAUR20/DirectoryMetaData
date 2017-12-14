package com.ace.DirectoryMetaData.model;

import java.util.Map;

public class FileResult {
	
	private String fileName;
	
	private Map<CountParam, Long> resultMap;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileResult(String fileName) {
		this.fileName = fileName;
	}

	public Map<CountParam, Long> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<CountParam, Long> resultMap) {
		this.resultMap = resultMap;
	}
	
	public String toString() {
		return "File Name :: " + fileName + " Result Map :: " + resultMap;
	}

}
