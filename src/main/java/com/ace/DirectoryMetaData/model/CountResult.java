package com.ace.DirectoryMetaData.model;

public class CountResult {
	
	private CountParam param;
	private Long count;
	public CountParam getParam() {
		return param;
	}
	public void setParam(CountParam param) {
		this.param = param;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public CountResult(CountParam param, Long count) {
		super();
		this.param = param;
		this.count = count;
	}
	
	public String toString() {
		return param + " : " + count;
	}
	
}
