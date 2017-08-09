package fhffm.iot.device;

public class MainProgram {

	public static void main(String[] args) {
		try {
			new UltrasonicSensor().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
