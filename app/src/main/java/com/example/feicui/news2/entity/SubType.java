package com.example.feicui.news2.entity;

public class SubType{
	
	private int subid;
	private String subgroup;
	
	public SubType(int subid, String subgroup) {
		this.subid = subid;
		this.subgroup = subgroup;
	}
	
	public int getSubid() {
		return subid;
	}
	public String getSubgroup() {
		return subgroup;
	}
	
}