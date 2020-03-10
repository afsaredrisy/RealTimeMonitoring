# Real time health monitoring 
This project is the implementation of realtime communication with websocket protocol. <br/>
**System Basic**
We are using reactive approach for transmitting the real time sensors data from senders clients to the receivers clients. System involves one to many relation from one sender to multiple receivers. This document will describe the receivers client APIs to get all Live patient and to register as receivers inorder to receive a patient's vital signals in realtime. Sender implement
a
##Client Usage

# Get all Live Patients
To get all live patient you need to make a HTTP-Post request to application server, Following are the details of provided API.

Request Protocol :  HTTP2<br/>
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

To get real time data to your web, android or iOS client you need to establish an full duplex communication channel with server. To establish realtime duplex communication channel use wesockets. 
Following is the detail description to connect , register & receives data.

**Connect with socket**

Socket End Point: ws://52.15.210.116:8080/RealTimeMonitoring/auth/end
Socket Protocol : ws (Websocket 1.1)

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

```
{"id":"556433","requestType":19,"data":""}
```
Make sure to append correct id in json data, You can get id from list of live patients.

**Receive & Parse realtime sensors data**

Once your device has been register as receiver on a particular patient id then you will be continuously receiving the real time data in JSON format via connected socket connection. To understand the format of data you need here is one example.
To use ECG wave you will get a JSON object in following format which you can parse & use in your application.
```JSON
{
	"id":"556433",
	"requestType":4
	"data" : "{
		\"ecg\": 98,
		\"logical_timestamp\": 5345665256,
		\"physical_timestamp\": \"10 March 2019: 20:18:23\"
	}"

}


```

 

