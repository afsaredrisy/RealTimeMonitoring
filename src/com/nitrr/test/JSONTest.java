package com.nitrr.test;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.nitrr.constants.Constants;
import com.nitrr.model.dto.CustomRequest;

public class JSONTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CustomRequest request = new CustomRequest("123",Constants.TEST_TRANMISSION);
		JSONObject json = new JSONObject();
		json.put("TESTDATA", 67);
		request.setData(json);
		Gson gson = new Gson();
		System.out.println(gson.toJson(request));
	}

}




/*
 * Request for register sender
 * {"id":"123","requestType":18,"data":{"map":{}}}
 * 
 *Request for register receiver
 *{"id":"123","requestType":19,"data":{"map":{}}}
 * 
 * Test data transmission
 * {"id":"123","requestType":4,"data":{"map":{"TESTDATA":67}}}

 * 
 */