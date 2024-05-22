#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <Servo.h> 

Servo myservo;
int buttonPin = 3;
String userPass, slotPass, changePass;
 
const char* ssid = "iBall-Baton";
const char* password = "04816451852";

int BUILTIN_LED1 = 2; //GPIO2

void setup () {

  myservo.attach(4);
  pinMode(buttonPin, INPUT);
  pinMode(BUILTIN_LED1, OUTPUT);
  Serial.begin(115200);
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED) {
 
    delay(1000);
    Serial.print("Connecting..");
 
  }
 
}
 
void loop() {

  String payload;

  
  int pos;
 
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient
 
    http.begin("http://192.168.1.101/techpark/getstatus.php?id=1");  //Specify request destination
    int httpCode = http.GET();                                                                  //Send the request
 
    if (httpCode > 0) { //Check the returning code
 
      payload = http.getString();   //Get the request response payload
      Serial.println(payload);                     //Print the response payload
      
 
    } 
 
    http.end();   //Close connection 
  }

    if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient
 
    http.begin("http://192.168.1.101/techpark/getPass.php?id=1");  //Specify request destination
    int httpCode = http.GET();                                                                  //Send the request
 
    if (httpCode > 0) { //Check the returning code
 
      userPass = http.getString();   //Get the request response payload
      Serial.println(userPass);                     //Print the response payload
      
 
    }
 
    http.end();   //Close connection 
    }

    if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient
 
    http.begin("http://192.168.1.101/login/userPass.php?id=1");  //Specify request destination
    int httpCode = http.GET();                                                                  //Send the request
 
    if (httpCode > 0) { //Check the returning code
 
      slotPass = http.getString();   //Get the request response payload
      Serial.println(slotPass);                     //Print the response payload
      
 
    }
 
    http.end();   //Close connection
    }
    if(payload=="1" && (userPass==slotPass))
      {
        Serial.println("gate open");
        digitalWrite(BUILTIN_LED1, LOW);  // Turn the LED off by making the voltage HIGH
        delay(1000); 
        digitalWrite(BUILTIN_LED1, HIGH);  // Turn the LED off by making the voltage HIGH
        delay(500);

         for(pos = 0; pos <= 90; pos += 1) // goes from 0 degrees to 180 degrees 
           {                                  // in steps of 1 degree 
            myservo.write(pos);              // tell servo to go to position in variable 'pos' 
            delay(15);                       // waits 15ms for the servo to reach the position 
             }
        delay(2000);

          for(pos = 90; pos>=0; pos-=1)     // goes from 180 degrees to 0 degrees 
        {                                
           myservo.write(pos);              // tell servo to go to position in variable 'pos' 
           delay(15);                       // waits 15ms for the servo to reach the position 
         } 

         if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient
 
    http.begin("http://192.168.1.101/login/changePass.php?id=1");  //Specify request destination
    int httpCode = http.GET();                                                                  //Send the request
 
    if (httpCode > 0) { //Check the returning code
 
      changePass = http.getString();   //Get the request response payload
      Serial.println(changePass);                     //Print the response payload
      
 
    }
 
    http.end();   //Close connection
    }
 
      }
      else
      {
        Serial.println("gate closed");
        digitalWrite(BUILTIN_LED1, HIGH);  // Turn the LED off by making the voltage HIGH
        delay(500);
      }

        delay(30000);    //Send a request every 30 seconds
 
  }
  

 

