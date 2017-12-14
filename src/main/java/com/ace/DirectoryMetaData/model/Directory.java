package com.ace.DirectoryMetaData.model;

import java.util.ArrayList;
import java.util.List;

public class Directory {

	private String name;
	private List<String> files = new ArrayList<>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getFiles() {
		return files;
	}
	public void setFiles(List<String> files) {
		this.files = files;
	}
}
