# IoT

A small project of IoT implmentation.

Scenario: Sends data from ultrasonic HC-SR04 sensor and temperature/humidity DHT11 sensor to a server through an intermediate gateway.

The devices/sensors use Californium and Paho to implement CoAP and MQTT communication protocol

Likewise, the gateway use Californium/Paho for CoAP and MQTT. Leshan library is used in combination with Californium to map the ultrasonic sensor as a lwm2m device

For server, use Eclipse Hono as a connector to receive telemetry data from gateway. 
