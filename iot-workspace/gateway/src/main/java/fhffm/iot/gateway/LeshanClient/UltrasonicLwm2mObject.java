//package fhffm.iot.gateway.LeshanClient;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//
//import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
//import org.eclipse.leshan.core.response.ExecuteResponse;
//import org.eclipse.leshan.core.response.ReadResponse;
//import org.eclipse.leshan.util.NamedThreadFactory;
//
//
//public class UltrasonicLwm2mObject extends BaseInstanceEnabler {
//	
//	private static final String UNIT_CENTIMETER = "cm";
//    private static final int SENSOR_VALUE = 5700;
//    private static final int UNITS = 5701;
//    private static final int MAX_MEASURED_VALUE = 5602;
//    private static final int MIN_MEASURED_VALUE = 5601;
//    private static final int RESET_MIN_MAX_MEASURED_VALUES = 5605;
//    
//    private final ScheduledExecutorService scheduler;
//    
//    private retrieveDistance mockDistance;
//    double currentDistance = 0d;
//    private double minMeasuredValue = currentDistance;
//    private double maxMeasuredValue = currentDistance;
//    
//    
//	private void scheduleLoopOnce() {
//		scheduler.schedule(new Runnable() {
//
//            @Override
//            public void run() {
//                adjustDistance();
//                
//                scheduleLoopOnce();
//            }
//            
//            
//        }, 1, TimeUnit.SECONDS);
//	}
//	private void scheduleLoop() {
//		scheduler.scheduleAtFixedRate(new Runnable() {
//
//            @Override
//            public void run() {
//				try {
//	                adjustDistance();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//            }
//        }, 2, 5, TimeUnit.SECONDS);
//	}
//
//    public UltrasonicLwm2mObject( ) {
//    	mockDistance = new retrieveDistance();
////    	scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Temperature Sensor"));
//    	scheduler = Executors.newScheduledThreadPool(1);
//    	
//    	scheduleLoopOnce();
////    	scheduleLoop();
//    	
//    }
//    
//    public ReadResponse read(int resourceId) {
//        switch (resourceId) {
//        case MIN_MEASURED_VALUE:
//            return ReadResponse.success(resourceId, getTwoDigitValue(minMeasuredValue));
//        case MAX_MEASURED_VALUE:
//            return ReadResponse.success(resourceId, getTwoDigitValue(maxMeasuredValue));
//        case SENSOR_VALUE:
//            return ReadResponse.success(resourceId, getTwoDigitValue(currentDistance));
//        case UNITS:
//            return ReadResponse.success(resourceId, UNIT_CENTIMETER);
//        default:
//            return super.read(resourceId);
//        }
//    }
//    
//    @Override
//    public synchronized ExecuteResponse execute(int resourceId, String params) {
//        switch (resourceId) {
//        case RESET_MIN_MAX_MEASURED_VALUES:
//            resetMinMaxMeasuredValues();
//            return ExecuteResponse.success();
//        default:
//            return super.execute(resourceId, params);
//        }
//    }
//    
//    private double getTwoDigitValue(double value) {
//        BigDecimal toBeTruncated = BigDecimal.valueOf(value);
//        return toBeTruncated.setScale(2, RoundingMode.HALF_UP).doubleValue();
//    }
//    
//    
//    private void adjustDistance() {
//		currentDistance = mockDistance.getDistance();
//		
//		Integer changedResource = adjustMinMaxMeasuredValue(currentDistance);
//        if (changedResource != null) {
//            fireResourcesChange(SENSOR_VALUE, changedResource);
//        } else {
//            fireResourcesChange(SENSOR_VALUE);
//        }  
//    }
//    
//    private Integer adjustMinMaxMeasuredValue(double newDistance) {
//
//        if (newDistance > maxMeasuredValue) {
//            maxMeasuredValue = newDistance;
//            return MAX_MEASURED_VALUE;
//        } else if (newDistance < minMeasuredValue) {
//            minMeasuredValue = newDistance;
//            return MIN_MEASURED_VALUE;
//        } else {
//            return null;
//        }
//    }
//    
//    private void resetMinMaxMeasuredValues() {
//        minMeasuredValue = currentDistance;
//        maxMeasuredValue = currentDistance;
//    }
//    
//    
//}
