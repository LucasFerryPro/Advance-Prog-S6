#include <HCSR04.h>

HCSR04 hc(22, 19); //initialisation class HCSR04 (trig pin , echo pin)

const int pinSwitch = 27;
const int pinLed = 5;

void setup() {
  Serial.begin(115200);
  pinMode(pinSwitch, INPUT);
  pinMode(pinLed, OUTPUT);
  Serial.println("OK");
}

void loop() {
  int switchState = digitalRead(pinSwitch);
  int dist = hc.dist();

  if(switchState == 1){
    if(dist<=20){
      digitalWrite(pinLed, HIGH);
    }else{
      digitalWrite(pinLed, LOW);
    }
  }else{
    if(dist>20){
      digitalWrite(pinLed, HIGH);
    }else{
      digitalWrite(pinLed, LOW);
    }
  }
}
