package com.sailing.sdp.common;

import java.io.Serializable;

public class FieldInfo implements Serializable {

	private String field;
	
	private String type;
	
	private int participle;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getParticiple() {
		return participle;
	}

	public void setParticiple(int participle) {
		this.participle = participle;
	}
	
	
}
