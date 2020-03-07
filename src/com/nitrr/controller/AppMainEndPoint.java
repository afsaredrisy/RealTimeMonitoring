package com.nitrr.controller;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


import org.json.JSONObject;

import com.google.gson.Gson;
import com.nitrr.constants.Constants;
import com.nitrr.model.Patient;
import com.nitrr.model.dto.CustomRequest;
import com.nitrr.model.dto.PatientDTO;
import com.nitrr.utils.SystemIdGenerator;


@ServerEndpoint("/auth/end")
public class AppMainEndPoint {
	
	// Static Live In memory data
	private static List<Session> clients = new ArrayList<Session>();
	private static Map<String, Session> senders = new HashMap<String, Session>();
	private static Map<String, ArrayList<Session>> receivers = new HashMap<String, ArrayList<Session>>();
	private static Map<Session, String> maps = new HashMap<Session, String>();
	private static Map<String, Patient> live = new HashMap<String, Patient>();
	private static Thread conThread;
	private JSONObject greeting = new JSONObject();
	
	@OnMessage
	public void onMessage(Session session,String msg) {
		
		try {
			System.out.println("New Message from "+session+" "+msg);
			Gson gson = new Gson();
			CustomRequest request = gson.fromJson(msg, CustomRequest.class);
			this.processRequest(request, session);
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	@OnOpen
	public void onOpen(Session session) {
		try {
			System.out.println("New client connected "+session.getId());
			clients.add(session);
			greeting.put("Success", "Welcome Real time monitoring");
			sendMessage(greeting.toString(),session);
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	@OnClose
	public void closedConnection(Session session) {
		System.out.println("Client "+session.getId()+" disconnected");
		try {
			
			if(senders.values().contains(session)) {
				onUnregistredSender(session);
				return;
			}
			if(receivers.containsKey(maps.get(session))) {
				onUnregisterReceiver(session);
			}
			removemapping(session);
			clients.remove(session);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
	
	public void onUnregisterReceiver(Session session) {
		try {
			String id = maps.get(session);
			onUnregisterReceiver(session,id);
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	public void onUnregistredSender(Session session) {
		try {
			String id = maps.get(session);
			onUnregistredSender(session,id);
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
	public void onRegistredSender(Session session, String id) {
		try {
			maps.put(session, id);
			senders.put(id, session); 
			System.out.println("Sender has been registred "+senders);
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	public void onRegistredReceiver(Session session, String id) {
		try {
			maps.put(session, id);
			if(receivers.get(id) == null) {
				receivers.put(id, new ArrayList<Session>());
			}
			receivers.get(id).add(session);
			System.out.println("receiver has been registred  "+receivers);
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public void onUnregistredSender(Session session, String id) {
		try {
			if(senders.containsKey(id)) {
				senders.remove(id);
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	public void onUnregisterReceiver(Session session, String id) {
		try {
			if(receivers.containsKey(id)) {
				receivers.get(id).remove(session);
				removemapping(session);
				if(receivers.get(id).size() == 0) {
					receivers.remove(id);
				}
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	public void removemapping(Session session) {
		try {
			maps.remove(session);
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	private void registerPatient(Patient patient, Session session) {
		if(live.containsKey(patient.get_id())) {
			System.out.println("Already register");
			return;
		}
		live.put(patient.get_id(), patient);
		CustomRequest request = new CustomRequest(patient.get_id(),Constants.REQUEST_PATIENT_REGISTRATION_SUCCESS);
		Gson gson = new Gson();
		sendMessage(gson.toJson(request), session);
	}
	private void registerPatient(PatientDTO patient, Session session) {
		registerPatient(patient.getPatient(String.valueOf(SystemIdGenerator.getNextId())),session);
	}
	
	private void registerPatient(String json, Session session) {
		try {
			Gson gson = new Gson();
			PatientDTO patient = gson.fromJson(json, PatientDTO.class);
			registerPatient(patient, session);
		
		}catch(Exception e) {
			System.out.println(e.toString());
			e.printStackTrace(System.out);
		}
	}
	
	protected void processRequest(CustomRequest request, Session session) {
		switch(request.getRequestType()) {
		case Constants.REQUEST_REGISTER_PATIENT:
			this.registerPatient(request.getData(), session);
			break;
		case Constants.REQUEST_REGISTER_RECEIVER:
			this.onRegistredReceiver(session, request.getId());
			break;
		case Constants.REQUEST_REGISTER_SENDER:
			this.onRegistredSender(session, request.getId());
			break;
			
		case Constants.REQUEST_UNREGISTER_RECEIVER:
			this.onUnregisterReceiver(session, request.getId());
			break;
		case Constants.REQUEST_UNREGISTER_SENDER:
			this.onUnregistredSender(session, request.getId());
			break;
		case Constants.TEST_TRANMISSION:
			relay(request);
			break;
		default:
			break;
			
		}
	}
	protected void relay(CustomRequest request) {
		if(receivers.containsKey(request.getId())){
			if(receivers.get(request.getId())==null) {
				return;
			}
			Gson gsn = new Gson();
			String json = gsn.toJson(request);
			for(Session receiver: receivers.get(request.getId())) {
				sendMessage(json,receiver);
			}
			
			//relay(receivers.get(request.getId()), request.getData(), request.getId());
		}
	}
	protected void relay(Session receiver, String data, String id) {
		CustomRequest request = new CustomRequest(id, Constants.TEST_TRANMISSION);
		request.setData(data);
		Gson gsn = new Gson();
		sendMessage(gsn.toJson(request),receiver);
		
	}
	
	private void sendMessage(String msg, Session session) {
		try {
			if(session.isOpen()) {
				session.getBasicRemote().sendText(msg);
				session.getBasicRemote().sendText("Text from server to "+session.getId());
			}
			else {
				System.out.println("Connection close");
			}
		}catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	public static ArrayList<Patient> getAllLive(){
		ArrayList<Patient> lives = new ArrayList<Patient>();
		for(String id: senders.keySet()) {
			if(live.containsKey(id)) {
				lives.add(live.get(id));
			}
		}
		return lives;
	}
	
	/**
	 * Live Thread
	 */
	
	static {
		conThread=new Thread() {
			public void run() {
				while(true) {
					if(clients!=null) {
						// Do Nothing
						System.out.println("Socket working...");
						try {
							Thread.sleep(1000);
						}catch(Exception e) {
							System.out.println(""+e.toString());
						}
						
					}
				}
			}
		};
		conThread.start();
	}
}
