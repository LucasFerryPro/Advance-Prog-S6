#include <Arduino.h>
#include <HCSR04.h>
#include <TM1637.h>

HCSR04 hc(22, 19); // initialisation class HCSR04 (trig pin, echo pin)
TM1637 tm1637(32, 33);

#define TRIGGER_PIN 14
#define ECHO_PIN 12
#define SWITCH_PIN 27
#define LED_PIN 5
#define PWM_CHANNEL 0
#define PWM_FREQ 1000
#define PWM_RESOLUTION 10

float dist;
volatile byte stateSwitch;
int sign;

int frequence = 1;

void setup() {
  Serial.begin(115200);
  pinMode(SWITCH_PIN, INPUT);
  pinMode(LED_PIN, OUTPUT);

  tm1637.init();
  tm1637.set(2);//BRIGHT_TYPICAL = 2,BRIGHT_DARKEST = 0,BRIGHTEST = 7;

  ledcSetup(PWM_CHANNEL, PWM_FREQ, PWM_RESOLUTION);
  ledcAttachPin(LED_PIN, PWM_CHANNEL);

  attachInterrupt(digitalPinToInterrupt(SWITCH_PIN), handleSwitch, CHANGE);

  dist = 0;
  stateSwitch = digitalRead(SWITCH_PIN);

  Serial.println("OK");
}

void ICACHE_RAM_ATTR handleSwitch() {
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

void displayDistance() {
  int truncatedDist = int(dist); // Tronquer les décimales

  tm1637.display(3,sign);

  if (dist >= 100) {
    tm1637.display(0, truncatedDist / 100);
    tm1637.display(1, (truncatedDist / 10) % 10);
    tm1637.display(2, truncatedDist % 10);
  } else {
    tm1637.display(0, 0x7f);
    tm1637.display(1, truncatedDist / 10);
    tm1637.display(2, truncatedDist % 10);
  }
}

void loop() {
  int new_dist = hc.dist();

  if (dist < new_dist){
    sign = 16;
  }else if (dist > new_dist){
    sign = 17;
  }

  if (abs(new_dist - dist) > 1) {
    dist = new_dist;
    changeBlink();
    displayDistance(); // Afficher la distance sur l'afficheur
  }

  delay(100);
}
