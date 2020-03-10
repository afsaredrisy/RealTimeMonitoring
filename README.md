# Real time health monitoring 
This project is the implementation of realtime communication with websocket protocol. <br/>
**System Basic**<br/>
We are using reactive approach for transmitting the real time sensors data from senders clients to the receivers clients. System involves one to many relation from one sender to multiple receivers. This document will describe the receivers client APIs to get all Live patient and to register as receivers inorder to receive a patient's vital signals in realtime. Sender implementation is available in Android can be [see here](https://github.com/afsaredrisy/RealTimeMonitoring)
a
## Client Usage

# Get all Live Patients
To get all live patient you need to make a HTTP-Post request to application server, Following are the details of provided API.

Request Protocol :  HTTP<br/>
Request URL: http://52.15.210.116:8080/RealTimeMonitoring/live/patient<br/>
Request Method: POST<br/>
Request Content-Type : application/json<br/>
Request Body: {} <br/>
Response Content-Type: application/json<br/>
Response Body : List of all live patient.<br/>
**Response Body Sample**<br/>
```JSON
{
    "timestamp": 1583855571041,
    "data": [
        {
            "_id": "556438",
            "name": "Alice",
            "age": 23,
            "height": 5.7,
            "gender": "Male",
            "weight": 52,
            "history": "NA",
            "region": "Urban"
        },
        {
            "_id": "556439",
            "name": "Bob",
            "age": 25,
            "height": 5.4,
            "gender": "Male",
            "weight": 58,
            "history": "NA",
            "region": "rural"
        }
    ]
}
```

# Register & Receive realtime sensors data

To get real time data to your web, android or iOS client you need to establish a full duplex communication channel with server. To establish realtime duplex communication channel use wesockets. 
Following is the detail description to connect , register & receives data.

**Connect with socket**

Socket End Point: ws://52.15.210.116:8080/RealTimeMonitoring/auth/end <br/>
Socket Protocol : ws (Websocket 1.1)<br/>

Following is the sample to connect with server in javascript
```javascript
var webSocket = new WebSocket("ws://52.15.210.116:8080/RealTimeMonitoring/auth/end");
        var echoText = document.getElementById("echoText");
        echoText.value = "";
        var message = document.getElementById("message");
        webSocket.onopen = function(message){ wsOpen(message);};
        webSocket.onmessage = function(message){ wsGetMessage(message);};
        webSocket.onclose = function(message){ wsClose(message);};
        webSocket.onerror = function(message){ wsError(message);};
        function wsOpen(message){
            echoText.value += "Connected ... \n";
        }
        function wsSendMessage(){
            webSocket.send(message.value);
            echoText.value += "Message sended to the server : " + message.value + "\n";
            message.value = "";
        }
        function wsCloseConnection(){
            webSocket.close();
        }
        function wsGetMessage(message){
            echoText.value += "Message received from to the server : " + message.data + "\n";
        }
        function wsClose(message){
            echoText.value += "Disconnect ... \n";
        }
 
        function wserror(message){
            echoText.value += "Error ... \n";
        }


```
For complete sample [click-here](WebContent/socket.html)

**Register to receive live data**

To receive live sensors data installed on patient with `id` sends the following json data to server via connected socket connection.

```json
{"id":"556433","requestType":19,"data":""}
```
Make sure to append correct id in json data, You can get id from list of live patients.

**Receive & Parse realtime sensors data**

Once your device has been register as receiver on a particular patient id then you will be continuously receiving the real time data in JSON format via connected socket connection. To understand the format of data here is one example.
To use ECG wave you will get a JSON object in following format which you can parse & use in your application.
```JSON
{
	"id":"556433",
	"requestType":4,
	"data":"{
		\"ecg\": 98,
		\"logical_timestamp\": 5345665256,
		\"physical_timestamp\": \"10 March 2019: 20:18:23\"
	}"

}
```
To understand all data format you can use or see the following java implementation to parse received json into their respective objects.
You will always receive the json object of `CustomRequest` class.
```java

public class CustomRequest {
	
	private String id;
	private int requestType;
	private String data = "";
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "{id=" + id + ", requestType=" + requestType + ", data=" + data + "}";
	}

}

```

Here is parser.
```java

public class RemoteDataParser {
    private DataParser.onPackageReceivedListener mListner;
    public RemoteDataParser(DataParser.onPackageReceivedListener mListner){
        this.mListner = mListner;
    }
    public void onDataReceived(JSONObject jsonObject){
         parseJsonData(jsonObject);
    }
    private void parseJsonData(JSONObject json){
        if(mListner == null){
            return;
        }
        Set<String> keys = new HashSet<String>();
        Iterator<String> iterator = json.keys();
        while(iterator.hasNext()){
            keys.add(iterator.next());
        }
        if(keys.contains(Constants.ECG)){
            parseECGWaveFromJson(json,keys);
        }
        else if(keys.contains(Constants.SPO2_SIGNAL)){
            parseSpO2WaveFromJson(json,keys);
        }
        else{
            parseJsonData(json,keys);
        }
    }
    private void parseJsonData(JSONObject json, Set<String> keys){
            parseECGFromJson(json,keys);
            parseNIBPFromJson(json,keys);
            parseSpo2FromJson(json,keys);
            parseTempratureFromJson(json,keys);
    }
    private void parseSpo2FromJson(JSONObject json, Set<String> keys){
        try{
            if(keys.contains(Constants.PULSE_RATE) && keys.contains(Constants.SPO2) ){
               int pulseRate = json.getInt(Constants.PULSE_RATE);
               int spo2v = json.getInt(Constants.SPO2);
               SpO2 spO2 = new SpO2(spo2v,pulseRate,0);
               mListner.onSpO2Received(spO2);
            }
            return;
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }
    private void parseECGWaveFromJson(JSONObject json, Set<String> keys){
        try{
            if(keys.contains(Constants.ECG)){
                mListner.onECGWaveReceived(json.getInt(Constants.ECG));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void parseECGFromJson(JSONObject json,Set<String> keys){
        try{
            if(keys.contains(Constants.HEART_RATE) && keys.contains(Constants.RESP_RATE)){

                ECG ecg = new ECG(json.getInt(Constants.HEART_RATE),json.getInt(Constants.RESP_RATE),1);
                mListner.onECGReceived(ecg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void parseTempratureFromJson(JSONObject json,Set<String> keys){
        try{
            if(keys.contains(Constants.TEMPRATURE)){
                double temp = json.getDouble(Constants.TEMPRATURE);
                Temp temprature = new Temp(temp,1);
                mListner.onTempReceived(temprature);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void parseNIBPFromJson(JSONObject json,Set<String> keys){
        try {
            if(keys.contains(Constants.SDP) && keys.contains(Constants.DBP)){
                int highPressure = json.getInt(Constants.SDP);
                int lowPressure = json.getInt(Constants.DBP);
                int meanPressure = (int)(highPressure+lowPressure)/2;
                NIBP nibp = new NIBP(highPressure,meanPressure,lowPressure,highPressure,1);
                mListner.onNIBPReceived(nibp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void parseSpO2WaveFromJson(JSONObject json,Set<String> keys){
        try{
            if(keys.contains(Constants.SPO2_SIGNAL)){
                   mListner.onSpO2WaveReceived(json.getInt(Constants.SPO2_SIGNAL));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


```	
Usefull Constants here.

```java
public class Constants {
	// TODO: Registry constants
	public static final int REQUEST_REGISTER_RECEIVER = 0x13;
	public static final int REQUEST_UNREGISTER_RECEIVER = 0x15;
	public static final int DATA_TRANMISSION = 0004;
	public static final String AGE = "age";
	public static final String AGE = "age";
    	public static final String HEIGHT = "height";
   	public static final String WEIGHT = "weight";
   	public static final String REMARK = "history";
   	public static final String REGION = "region";
   	public static final String GENDER  = "gender";
    	public static final String PULSE_RATE = "pulse_rate";
    	public static final String RESP_RATE = "resp_rate";
    	public static final String HEART_RATE = "heart_rate";
    	public static final String SDP= "sdp";
    	public static final String DBP="dbp";
    	public static final String TEMPRATURE = "temp";
    	public static final String SPO2 = "spo2";
    	public static final String SPO2_SIGNAL = "spo2_signal";
    	public static final String ECG = "ecg";
    	public static final String TIMESTAMP_LOGICAL = "logical_timestamp";
    	public static final String TIMESTAMP_PHYSICAL = "physical_timestamp";    
}

```

 

