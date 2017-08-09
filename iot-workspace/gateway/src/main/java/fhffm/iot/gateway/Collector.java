package fhffm.iot.gateway;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.server.resources.CoapExchange;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;

public class Collector {
  
	//Define resource
	public static class UltrasonicResource extends CoapResource {

	    public String value = "0";
	    //Define resource as observable
	    public UltrasonicResource() {
	        super("Ultrasonic");
	        setObservable(true);
	    }

	    //Handle get request
	    @Override
	    public void handleGET(CoapExchange exchange) {
	        exchange.respond(value);

	    }


	    //Handle put request
	    @Override
	    public void handlePUT(CoapExchange exchange) {
	        String payload = exchange.getRequestText();
	        

	        try {
	            exchange.respond(CHANGED, payload);
	            value = new String(payload);
	            IntermediateBroker.setSensorValue(Double.parseDouble(value));
	            changed();
	    	  	    	    
	        } catch (Exception e) {
	            e.printStackTrace();
	            exchange.respond(BAD_REQUEST, "Invalid String");
	        }
//	        System.out.println(a.getSensorValue() + " cm");
	    }
	
	}
	
	
}
