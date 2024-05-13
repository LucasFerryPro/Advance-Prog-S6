#include <HCSR04.h>
#include <Arduino.h>

HCSR04 hc(22, 19); //initialisation class HCSR04 (trig pin , echo pin)

#define TRIGGER_PIN 14
#define ECHO_PIN 12
#define SWITCH_PIN 27
#define LED_PIN 5
#define PWM_CHANNEL 0
#define PWM_FREQ 1000
#define PWM_RESOLUTION 10

float dist;
volatile byte stateSwitch;

int frequence = 1;

void ICACHE_RAM_ATTR handleSwitch(){
  stateSwitch = digitalRead(SWITCH_PIN);
}

void changeBlink() {
  if (stateSwitch == 1) {
    if (dist <= 10) {
      ledcWrite(PWM_CHANNEL, 512); // 50% duty cycle
      ledcChangeFrequency(PWM_CHANNEL, 1, PWM_RESOLUTION); // 1Hz
    } else if (dist >= 100) {
      ledcWrite(PWM_CHANNEL, 512); // 50% duty cycle
      ledcChangeFrequency(PWM_CHANNEL, 50, PWM_RESOLUTION); // 50Hz
    } else {
      int freq = map(dist, 10, 100, 1, 50);
      ledcWrite(PWM_CHANNEL, 512); // 50% duty cycle
      ledcChangeFrequency(PWM_CHANNEL, freq, PWM_RESOLUTION);
    }
  } else {
    ledcWrite(PWM_CHANNEL, 0); // Turn off LED
  }
}

void setup() {
  Serial.begin(115200);
  pinMode(SWITCH_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);

  ledcSetup(PWM_CHANNEL, PWM_FREQ, PWM_RESOLUTION);
  ledcAttachPin(LED_PIN, PWM_CHANNEL);

  attachInterrupt(digitalPinToInterrupt(SWITCH_PIN), handleSwitch, CHANGE);

  dist = 0;
  stateSwitch = digitalRead(SWITCH_PIN);

  Serial.println("OK");
}

void loop() {
  // put your main code here, to run repeatedly:
  int new_dist = hc.dist();

  if (abs(new_dist - dist) > 1) {
    dist = new_dist;
    changeBlink();
  }

  delay(100);
}
