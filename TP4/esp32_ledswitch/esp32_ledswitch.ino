const int pinSwitch = 27;
const int pinLed = 5;

void setup() {
  Serial.begin(115200);
  pinMode(pinSwitch, INPUT);
  pinMode(pinLed, OUTPUT);
  Serial.println("OK");
}

void loop() {
  // Read the state of the switch
  int switchState = digitalRead(pinSwitch);

  // Print the switch state to the serial monitor
  if (switchState == 0){
    digitalWrite(pinLed, LOW);
  }else{
    digitalWrite(pinLed, HIGH);
  }

  // Add a small delay to make the output readable
  delay(500);
}
