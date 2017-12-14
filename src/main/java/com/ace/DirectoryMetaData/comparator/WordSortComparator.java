package com.ace.DirectoryMetaData.comparator;

import java.util.Comparator;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.FileResult;
import com.ace.DirectoryMetaData.model.SortOrder;

public class WordSortComparator implements Comparator<FileResult> {

	@Override
	public int compare(FileResult o1, FileResult o2) {
		// TODO Auto-generated method stub
		switch (order) {
		case ASC:
			return o1.getResultMap().get(sortParam).compareTo(o2.getResultMap().get(sortParam));
		case DESC:
			return -1 * (o1.getResultMap().get(sortParam).compareTo(o2.getResultMap().get(sortParam)));
		}
		return 0;

	}
	
	private CountParam sortParam;
	private SortOrder order;
	
	public WordSortComparator(CountParam sortParam, SortOrder order) {
		super();
		this.sortParam = sortParam;
		this.order = order;
	}

}
