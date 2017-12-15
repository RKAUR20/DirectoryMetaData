package com.ace.DirectoryMetaData.comparator;

import java.util.Comparator;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.FileResult;
import com.ace.DirectoryMetaData.model.SortOrder;

public class FileResultComparator implements Comparator<FileResult> {

	@Override
	public int compare(FileResult o1, FileResult o2) {
		// TODO Auto-generated method stub
		return (o1.getResultMap().get(sortParam).compareTo(o2.getResultMap().get(sortParam))) * order.getMultiplier();
	}

	private CountParam sortParam;
	private SortOrder order;

	public FileResultComparator(CountParam sortParam, SortOrder order) {
		super();
		this.sortParam = sortParam;
		this.order = order;
	}

}
