package com.ace.DirectoryMetaData.model;

public enum CountParam {
	
	WORDS("WORDS"), LETTERS("LETTERS"), VOWELS("VOWELS"), SPECIAL_CHAR("SPECIAL_CHAR");
	
	private String param;
	
	CountParam(String param){
		this.setParam(param);
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	public static CountParam getEnumFromParam(String iParam) {
		for (CountParam c : CountParam.values()) {
			if(c.getParam().equals(iParam)) {
				return c;
			}
		}
		return null;
	}

}
