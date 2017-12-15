package com.ace.DirectoryMetaData.model;

public enum SortOrder {

	ASC(1), DESC(-1);
	
	private int multiplier;

	private SortOrder(int multiplier) {
		this.setMultiplier(multiplier);
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
	
}
