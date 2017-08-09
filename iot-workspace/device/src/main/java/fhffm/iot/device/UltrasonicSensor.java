package fhffm.iot.device;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

public class UltrasonicSensor {
    CoapClient client = new CoapClient("coap://192.168.50.31:5683/Ultrasonic");
	//GPIO Pins
	private static GpioPinDigitalOutput sensorTriggerPin ;
	private static GpioPinDigitalInput sensorEchoPin ;
	
	double distance;
	
	final static GpioController gpio = GpioFactory.getInstance();
	
    
	public void run() throws InterruptedException{
		sensorTriggerPin =  gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04); // Trigger pin as OUTPUT
		sensorEchoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,PinPullResistance.PULL_DOWN); // Echo pin as INPUT

		while(true){
			try {
			Thread.sleep(2000);
			sensorTriggerPin.high();
			Thread.sleep((long) 0.01);
			sensorTriggerPin.low();
		
			while(sensorEchoPin.isLow()){
				
			}
			long startTime= System.nanoTime();
			while(sensorEchoPin.isHigh()){
				
			}
			long endTime= System.nanoTime();
		
			distance = (((endTime-startTime)/1e3)/2) / 29.1;
			System.out.println("Distance :"+ distance +" cm"); 
	        
			CoapResponse resp = client.put(Double.toString(distance), MediaTypeRegistry.TEXT_PLAIN);
//			System.out.println(resp.isSuccess());
//			System.out.println(resp.getResponseText());

			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		}
	}
}
