package com.ace.DirectoryMetaData.util;

import com.ace.DirectoryMetaData.model.CountParam;
import com.ace.DirectoryMetaData.model.SortOrder;

public class SortPreferrence {

	private CountParam param;
	
	private SortOrder order;

	private volatile static SortPreferrence instance;
	
	public CountParam getParam() {
		return param;
	}

	public void setParam(CountParam param) {
		this.param = param;
	}

	public SortOrder getOrder() {
		return order;
	}

	public void setOrder(SortOrder order) {
		this.order = order;
	}
	
	public static SortPreferrence getInstance() {
		if(instance == null) {
			synchronized (SortPreferrence.class) {
				if(instance == null) {
					instance = new SortPreferrence();
				}
			}
		}
		return instance;
	}
	
}
