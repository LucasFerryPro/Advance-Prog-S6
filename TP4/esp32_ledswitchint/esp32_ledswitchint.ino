const int pinSwitch = 27;
const int pinLed = 5;

void ICACHE_RAM_ATTR handleSwitch(){
  int switchState = digitalRead(pinSwitch);
  if (switchState == 1){
    digitalWrite(pinLed, HIGH);
  } else {
    digitalWrite(pinLed, LOW);
  }
}

void setup() {
  Serial.begin(115200);
  pinMode(pinSwitch, INPUT);
  pinMode(pinLed, OUTPUT);
  attachInterrupt(digitalPinToInterrupt(pinSwitch), handleSwitch, CHANGE);
  Serial.println("OK");
}

void loop() {
  // put your main code here, to run repeatedly:

}
