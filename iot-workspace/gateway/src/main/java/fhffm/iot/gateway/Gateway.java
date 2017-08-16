package fhffm.iot.gateway;

import org.eclipse.californium.core.CoapServer;


import static org.eclipse.leshan.LwM2mId.*;
import static org.eclipse.leshan.client.object.Security.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.leshan.client.californium.LeshanClient;
import org.eclipse.leshan.client.californium.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Server;	
import org.eclipse.leshan.client.resource.LwM2mObjectEnabler;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.model.LwM2mModel;
import org.eclipse.leshan.core.model.ObjectLoader;
import org.eclipse.leshan.core.model.ObjectModel;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.util.Hex;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fhffm.iot.gateway.Collector.UltrasonicResource;

public class Gateway{
	
	
	//Declaration for Leshan client
	private static final Logger LOG = LoggerFactory.getLogger(Gateway.class);

    private final static String[] modelPaths = new String[] { "3303.xml" };

    private static final int OBJECT_ID_DISTANCE_SENSOR = 3303;
    private final static String DEFAULT_ENDPOINT = "Ultrasonic HC-SR04";
    private final static String LeshanServerURI = "coaps://192.168.50.31:5684";
    private final static String pskIdentityDefault = "mypskid";
	private final static String pskKeyDefault = "aabbccdd";
	
	public static void main(String[] args) {
		byte[] pskId = null;
	    byte[] pskKey = null;
	    String endpoint = null;
	    String serverURI = null;
        String localAddress = null;
        int localPort = 0;
        String secureLocalAddress = null;
        int secureLocalPort = 0;
        boolean needbootstrap = false;
        
	    //Start CoAP server for collecting data from device
		//Initiate CoAP server on 5683 port
        CoapServer server = new CoapServer();
        
        //Create new resource
        server.add(new UltrasonicResource());

        //Start the server
        server.start();
        
        try {
			new MqttReceiver().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
        
        //Start Leshan client to send data to server
        try {
			endpoint = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
            endpoint = DEFAULT_ENDPOINT;
		}
        serverURI = LeshanServerURI;
        pskId = pskIdentityDefault.getBytes();
        pskKey = Hex.decodeHex(pskKeyDefault.toCharArray());
     
        
        // Initialize model        
        createAndStartLeshanClient(endpoint, localAddress, localPort, secureLocalAddress, secureLocalPort, needbootstrap,
                serverURI, pskId, pskKey);	}

	public static void createAndStartLeshanClient(String endpoint, String localAddress, int localPort,
            String secureLocalAddress, int secureLocalPort, boolean needBootstrap, String serverURI, byte[] pskIdentity,
            byte[] pskKey) {
		
		// Initialize LWM2M model
		List<ObjectModel> models = ObjectLoader.loadDefault();      
        models.addAll(ObjectLoader.loadDdfResources("/models", modelPaths));
		
        // Initialize object list
        ObjectsInitializer initializer = new ObjectsInitializer(new LwM2mModel(models));
        initializer.setInstancesForObject(SECURITY, psk(serverURI, 123, pskIdentity, pskKey));
//        initializer.setInstancesForObject(SECURITY, noSec(serverURI, 123));

        initializer.setInstancesForObject(SERVER, new Server(123, 30, BindingMode.U, false));
         
        initializer.setClassForObject(DEVICE, DeviceLwm2mObject.class);
        initializer.setInstancesForObject(OBJECT_ID_DISTANCE_SENSOR, new UltrasonicLwm2mObject());
      
        List<LwM2mObjectEnabler> enablers = initializer.create(SECURITY, SERVER, DEVICE, OBJECT_ID_DISTANCE_SENSOR);
      
      // Create client
      
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);
        builder.setLocalAddress(localAddress, localPort);    
        builder.setLocalSecureAddress(secureLocalAddress, secureLocalPort);      
        builder.setObjects(enablers);     
        builder.setNetworkConfig(NetworkConfig.getStandard());   
        final LeshanClient client = builder.build();

      // Start the client
      client.start();

      // De-register on shutdown and stop client.
      
      Runtime.getRuntime().addShutdownHook(new Thread() {
          @Override
          public void run() {
              client.destroy(true); // send de-registration request before destroy
          }
      });
	}

}
