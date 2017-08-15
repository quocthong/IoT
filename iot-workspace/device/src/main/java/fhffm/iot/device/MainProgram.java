package fhffm.iot.device;

import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainProgram {

	public static void main(String[] args) throws MqttException {
		
		System.out.println("Choose the communication protocol");
		System.out.println("1. MQTT");
		System.out.println("2. CoAP");

		try(Scanner scanner = new Scanner(System.in)){
			String input = scanner.nextLine();
			if(input.equals("1")){
				try {
					new UltrasonicSensorMQTT().run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			else if(input.equals("2")){
				try {
					new UltrasonicSensorCoAP().run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			else
				System.out.println("Invalid input. Client destroyed !");	
	

		}catch(Exception e){
		    e.printStackTrace();
		}
	}
}
