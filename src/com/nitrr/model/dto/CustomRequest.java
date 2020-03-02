package com.nitrr.model.dto;

import org.json.JSONObject;

public class CustomRequest {
	
	private String id;
	private int requestType;
	private JSONObject data = new JSONObject();;
	public CustomRequest(String id, int requestType) {
		super();
		this.id = id;
		this.requestType = requestType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getRequestType() {
		return requestType;
	}
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "{id=" + id + ", requestType=" + requestType + ", data=" + data + "}";
	}

}
