package com.nitrr.test;

import org.json.JSONObject;

/*
 * 
 * 
 *  {"id":"123","requestType":4,"data":"{\"age\":25,\"height\":5,\"weight\":55,\"region\":\"Urban\",\"history\":\"na\",\"gender\":\"Male\",\"dbp\":0,\"sdp\":0,\"temp\":0,\"resp_rate\":0,\"heart_rate\":0,\"pulse_rate\":0,\"logical_timestamp\":1583317645849,\"physical_timestamp\":\"Wed Mar 04 15:57:25 GMT+05:30 2020\"}"}
 */

import com.google.gson.Gson;
import com.nitrr.constants.Constants;
import com.nitrr.model.dto.CustomRequest;
import com.nitrr.model.dto.PatientDTO;

public class JSONTest {

	public static void main(String[] args) {
		
		//String str = "{\"data\":{\"map\":{\"nameValuePairs\":{\"age\":22,\"height\":5.0,\"weight\":55.0,\"region\":\"Urban\",\"history\":\"na\",\"gender\":\"Male\",\"dbp\":0,\"sdp\":0,\"temp\":0,\"resp_rate\":0,\"heart_rate\":0,\"pulse_rate\":0,\"logical_timestamp\":1583315252222,\"physical_timestamp\":\"Wed Mar 04 15:17:32 GMT+05:30 2020\"}}},\"id\":\"123\",\"requestType\":4}";
		String str = "{\"id\":\"123\",\"requestType\":4,\"data\":\"{\\\"age\\\":25,\\\"height\\\":5,\\\"weight\\\":55,\\\"region\\\":\\\"Urban\\\",\\\"history\\\":\\\"na\\\",\\\"gender\\\":\\\"Male\\\",\\\"dbp\\\":0,\\\"sdp\\\":0,\\\"temp\\\":0,\\\"resp_rate\\\":0,\\\"heart_rate\\\":0,\\\"pulse_rate\\\":0,\\\"logical_timestamp\\\":1583317645849,\\\"physical_timestamp\\\":\\\"Wed Mar 04 15:57:25 GMT+05:30 2020\\\"}\"}";
		
		// TODO Auto-generated method stub
		CustomRequest request = new CustomRequest("123",Constants.TEST_TRANMISSION);
		JSONObject json = new JSONObject();
		json.put("TESTDATA", 67);
		request.setData(json.toString());
		
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(request));
		//CustomRequest req = gson.fromJson(str, CustomRequest.class);
		//System.out.println("Values: "+req.getData().getString("gender"));
		//System.out.println(gson.toJson(req));
		//JSONObject json = new JSONObject(req.getData());
		//System.out.println(json.keySet());
		//PatientDTO patient = new PatientDTO("Adil", 28, 5.5f, "Male", 55, "NA", "urbon");
		//CustomRequest req = new CustomRequest("0s",Constants.REQUEST_REGISTER_PATIENT);
		//String rt = gson.toJson(patient);
		//req.setData(gson.toJson(patient));
		//System.out.println(gson.toJson(req));
		//String str1 = "{\"id\":\"0s\",\"requestType\":22,\"data\":\"{\\\"name\\\":\\\"Adil\\\",\\\"age\\\":28,\\\"height\\\":5.5,\\\"gender\\\":\\\"Male\\\",\\\"weight\\\":55,\\\"history\\\":\\\"NA\\\",\\\"region\\\":\\\"urbon\\\"}\"}";
		//System.out.println(gson.fromJson(gson.toJson(req), CustomRequest.class));
		//System.out.println(gson.fromJson(str1, CustomRequest.class));
	}

}




/*
 * Request for register sender
 *{"id":"556433","requestType":18,"data":"{\"TESTDATA\":67}"}
 * 
 *Request for register receiver
 *{"id":"556433","requestType":19,"data":""}
 * 
 * Test data transmission
 * {"id":"556433","requestType":4,"data":"{\"TESTDATA\":8008}"}
 * 
 * Register patient
   {"id":"0s","requestType":22,"data":"{\"name\":\"Adil\",\"age\":28,\"height\":5.5,\"gender\":\"Male\",\"weight\":55,\"history\":\"NA\",\"region\":\"urbon\"}"}
 * 
 */