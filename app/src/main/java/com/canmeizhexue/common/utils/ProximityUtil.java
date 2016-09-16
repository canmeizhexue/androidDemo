package com.canmeizhexue.common.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashSet;
import java.util.Set;
/**
 * 距离传感器
 * @author silence
 *
 */
public class ProximityUtil {
	private static final String TAG="ProximityUtil";
	private static final float SCREEN_BRIGHTNESS=0.01f;
	private static boolean sLastProximitySensorValueNearby;
	public static boolean isLastProximitySensorValueNearby() {
		return sLastProximitySensorValueNearby;
	}
	private static Set<Activity> sProximityDependentActivities = new HashSet<Activity>();
	private static SensorEventListener sProximitySensorListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.timestamp == 0) return; //just ignoring for nexus 1
			sLastProximitySensorValueNearby = isProximitySensorNearby(event);
			proximityNearbyChanged();
		}
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};


	private static void simulateProximitySensorNearby(Activity activity, boolean nearby) {
		final Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		View view = ((ViewGroup) window.getDecorView().findViewById(android.R.id.content)).getChildAt(0);
		if (nearby) {
            params.screenBrightness = SCREEN_BRIGHTNESS;
            if (view!=null){
            	view.setVisibility(View.INVISIBLE);
            }
            //Compatibility.hideNavigationBar(activity);
		} else  {
			params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
			if (view!=null){
				view.setVisibility(View.VISIBLE);
			}
           // Compatibility.showNavigationBar(activity);
		}
        window.setAttributes(params);
	}

	private static void proximityNearbyChanged() {
		boolean nearby = sLastProximitySensorValueNearby;
		for (Activity activity : sProximityDependentActivities) {
			simulateProximitySensorNearby(activity, nearby);
		}
	}

	public static synchronized void startProximitySensorForActivity(Activity activity) {
		if (sProximityDependentActivities.contains(activity)) {
			//Log.i(TAG,"proximity sensor already active for " + activity.getLocalClassName());
			return;
		}
		
		if (sProximityDependentActivities.isEmpty()) {
			SensorManager sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
			Sensor s = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
			if (s != null) {
				sm.registerListener(sProximitySensorListener,s,SensorManager.SENSOR_DELAY_UI);
				//Log.i(TAG,"Proximity sensor detected, registering");
			}
		} else if (sLastProximitySensorValueNearby){
			simulateProximitySensorNearby(activity, true);
		}

		sProximityDependentActivities.add(activity);
	}

	public static synchronized void stopProximitySensorForActivity(Activity activity) {
		sProximityDependentActivities.remove(activity);
		simulateProximitySensorNearby(activity, false);
		if (sProximityDependentActivities.isEmpty()) {
			SensorManager sm = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
			sm.unregisterListener(sProximitySensorListener);
			sLastProximitySensorValueNearby = false;
		}
	}

	public static Boolean isProximitySensorNearby(final SensorEvent event) {
		float threshold = 4.001f; // <= 4 cm is near

		final float distanceInCm = event.values[0];
		final float maxDistance = event.sensor.getMaximumRange();
		//Log.d(TAG,"Proximity sensor report [" + distanceInCm + "] , for max range [" + maxDistance + "]");

		if (maxDistance <= threshold) {
			// Case binary 0/1 and short sensors
			threshold = maxDistance;
		}

		return distanceInCm < threshold;
	}
}
