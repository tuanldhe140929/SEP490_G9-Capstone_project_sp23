package com.SEP490_G9.dto;

public class Change {
	public enum Type {
		REMOVED, UPDATED
	}

	private String item;
	private Type type;

	public Change() {
		// TODO Auto-generated constructor stub
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
