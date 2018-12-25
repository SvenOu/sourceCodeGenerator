package com.sql.code.generator.commom.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataPackage<I> implements Serializable {
	private static final long serialVersionUID = 7967509003811059598L;

	@JsonProperty("data")
	private List<I> data = new ArrayList<I>();

	@JsonProperty("total")
	private int total = 0;

	@JsonProperty("extra")
	private Map<String, Object> extra = new HashMap<String, Object>();

	public DataPackage() {
	}

	public DataPackage(List<I> data) {
		this.data = data;
		this.total = (data == null ? 0 : data.size());
	}

	public DataPackage(List<I> data, int total) {
		this.data = data;
		this.total = total;
	}

	public List<I> getData() {
		return this.data;
	}

	public void setData(List<I> data) {
		this.data = data;
	}

	public int getTotal() {
		return this.total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Map<String, Object> getExtra() {
		return this.extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}
}
