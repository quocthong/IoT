package fhffm.iot.gateway;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;

import fhffm.iot.gateway.Collector.UltrasonicResource;

public class Gateway {

	public static void main(String[] args) {
		//Initiate CoAP server on 5683 port
        CoapServer server = new CoapServer();
        
        //Create new resource
        server.add(new UltrasonicResource());

        //Start the server
        server.start();
        CoapCl val = new CoapCl();
        while(true)
        {
        System.out.println("Testing: " + val.val);
        }   
	}
	
	public static class CoapCl{
    	
		double val = 0;
		CoapClient client = new CoapClient("coap://localhost/Ultrasonic");
		CoapObserveRelation relation = client.observe(new CoapHandler() {
    	@Override public void onLoad(CoapResponse response)
    	{
    		val = Double.parseDouble(response.getResponseText());
    		System.out.println();
    	}

		@Override
		public void onError() {
			System.out.println("Failed");
			
		}
    });
	}

}
