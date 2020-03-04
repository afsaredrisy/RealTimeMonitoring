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

public class JSONTest {

	public static void main(String[] args) {
		
		//String str = "{\"data\":{\"map\":{\"nameValuePairs\":{\"age\":22,\"height\":5.0,\"weight\":55.0,\"region\":\"Urban\",\"history\":\"na\",\"gender\":\"Male\",\"dbp\":0,\"sdp\":0,\"temp\":0,\"resp_rate\":0,\"heart_rate\":0,\"pulse_rate\":0,\"logical_timestamp\":1583315252222,\"physical_timestamp\":\"Wed Mar 04 15:17:32 GMT+05:30 2020\"}}},\"id\":\"123\",\"requestType\":4}";
		String str = "{\"id\":\"123\",\"requestType\":4,\"data\":\"{\\\"age\\\":25,\\\"height\\\":5,\\\"weight\\\":55,\\\"region\\\":\\\"Urban\\\",\\\"history\\\":\\\"na\\\",\\\"gender\\\":\\\"Male\\\",\\\"dbp\\\":0,\\\"sdp\\\":0,\\\"temp\\\":0,\\\"resp_rate\\\":0,\\\"heart_rate\\\":0,\\\"pulse_rate\\\":0,\\\"logical_timestamp\\\":1583317645849,\\\"physical_timestamp\\\":\\\"Wed Mar 04 15:57:25 GMT+05:30 2020\\\"}\"}";
		
		// TODO Auto-generated method stub
		//CustomRequest request = new CustomRequest("123",Constants.REQUEST_REGISTER_RECEIVER);
		//JSONObject json = new JSONObject();
		//json.put("TESTDATA", 67);
		//request.setData(json);
		Gson gson = new Gson();
		CustomRequest req = gson.fromJson(str, CustomRequest.class);
		//System.out.println("Values: "+req.getData().getString("gender"));
		System.out.println(gson.toJson(req));
		JSONObject json = new JSONObject(req.getData());
		System.out.println(json.keySet());
	}

}




/*
 * Request for register sender
 * {"id":"123","requestType":18,"data":{"map":{}}}
 * 
 *Request for register receiver
 *{"id":"123","requestType":19,"data":""}
 * 
 * Test data transmission
 * {"id":"123","requestType":4,"data":{"map":{"TESTDATA":67}}}

 * 
 */