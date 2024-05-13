/*
BME280 I2C Test.ino

This code shows how to record data from the BME280 environmental sensor
using I2C interface. This file is an example file, part of the Arduino
BME280 library.

GNU General Public License

Written: Dec 30 2015.
Last Updated: Oct 07 2017.

Connecting the BME280 Sensor:
Sensor              ->  Board
-----------------------------
Vin (Voltage In)    ->  3.3V
Gnd (Ground)        ->  Gnd
SDA (Serial Data)   ->  A4 on Uno/Pro-Mini, 20 on Mega2560/Due, 2 Leonardo/Pro-Micro
SCK (Serial Clock)  ->  A5 on Uno/Pro-Mini, 21 on Mega2560/Due, 3 Leonardo/Pro-Micro

 */

#include <BME280I2C.h>
#include <Wire.h>
#include <WiFi.h>

#define SERIAL_BAUD 115200

#define SDAPIN 18
#define SCLPIN 23

const char* ssid = "testingiot";
const char* password = "testingiot";
const int mcId = -1;

WiFiClient client;
byte connState;
const char* ipServ = "192.168.0.1";
int portServ = 12345;
byte buf[1024];
int idx = 0;

BME280I2C bme;    // Default : forced mode, standby time = 1000 ms
                  // Oversampling = pressure ×1, temperature ×1, humidity ×1, filter off,

void setupWifi() {  
  WiFi.setAutoConnect(false);  // see comments   
  
  WiFi.mode (WIFI_STA); // setup as a wifi client
  WiFi.begin(ssid,password); // try to connect
  while (WiFi.status() != WL_CONNECTED) { // check connection
    delay(500); 
    Serial.print(".");
  } 
  // debug messages
  Serial.print("Connected, IP address: ");
  Serial.println(WiFi.localIP());
  
}

bool doConnection() {
  if (client.connect(ipServ, portServ)) {
    connState = 1;
    Serial.println("connected to server");
    // special for esp8266: disable buffering small mesg
    client.setNoDelay(true);
    return true;
  }
  else {
    connState = 0;
    Serial.println("connection failed");
    return false;
  }
}


void setup()
{
  Serial.begin(SERIAL_BAUD);
  delay(1000);
  setupWifi();
  
  Serial.begin(115200);
  connState = 0;

  while(!Serial) {} // Wait

  Wire.begin(SDAPIN,SCLPIN);

  while(!bme.begin())
  {
    Serial.println("Could not find BME280 sensor!");
    delay(1000);
  }

  switch(bme.chipModel())
  {
     case BME280::ChipModel_BME280:
       Serial.println("Found BME280 sensor! Success.");
       break;
     case BME280::ChipModel_BMP280:
       Serial.println("Found BMP280 sensor! No Humidity available.");
       break;
     default:
       Serial.println("Found UNKNOWN sensor! Error!");
  }
}

void loop()
{
   //printBME280Data(&Serial);
   //delay(500);
   if (connState == 0) {
    if (! doConnection()) { delay(1000); }
  }
  if (connState == 1) {
    client.write(10); // write a byte equal to 10
    client.println("10"); // write 3 bytes: ascii code of 1, ascii code of 0, ascii code of new_line
    // wait something from the server
    while (!client.available()) {} // see comments below
    // read what is available
    while (client.available()) {
      buf[idx++] = client.read(); // read & store a byte
    }
    Serial.println(String((char*)buf)); // print the content of buf as a string
    for(int i=0;i<idx;i++) buf[i] = 0; // reset buf
  }
}

void printBME280Data
(
   Stream* client
)
{
   float temp(NAN), hum(NAN), pres(NAN);

   BME280::TempUnit tempUnit(BME280::TempUnit_Celsius);
   BME280::PresUnit presUnit(BME280::PresUnit_Pa);

   bme.read(pres, temp, hum, tempUnit, presUnit);

   client->print("Temp: ");
   client->print(temp);
   client->print("°"+ String(tempUnit == BME280::TempUnit_Celsius ? 'C' :'F'));
   client->print("\t\tHumidity: ");
   client->print(hum);
   client->print("% RH");
   client->print("\t\tPressure: ");
   client->print(pres);
   client->println("Pa");

   delay(1000);
}
