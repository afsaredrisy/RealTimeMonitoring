package com.nitrr.model.dto;
import java.util.ArrayList;

import com.nitrr.model.*;
public class LivePatientsDTO {
	
	private long timestamp;
	private ArrayList<Patient> data;
	public LivePatientsDTO(long timestamp, ArrayList<Patient> data) {
		super();
		this.timestamp = timestamp;
		this.data = data;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public ArrayList<Patient> getData() {
		return data;
	}
	public void setData(ArrayList<Patient> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "{timestamp=" + timestamp + ", data=" + data + "}";
	}
	
	

}
