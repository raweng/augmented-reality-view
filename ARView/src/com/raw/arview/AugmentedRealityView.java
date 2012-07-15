package com.raw.arview;

import java.util.List;

import com.raw.utils.PaintUtils;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class AugmentedRealityView extends View implements SensorEventListener{

	private static Context _context;
	WakeLock mWakeLock;
	CameraView cameraView;
	RadarMarkerView radarMarkerView;
	static PaintUtils paintScreen;
	static DataView dataView;
	static boolean isInited = false;
	public static float azimuth;
	public static float pitch;
	public static float roll;
	public double latitudeprevious;
	public double longitude;
	String locationContext;
	String	provider;
	LocationManager locationManager;
	DisplayMetrics displayMetrics;
	Camera camera;
	public int screenWidth;
	public int screenHeight;

	private float RTmp[] = new float[9];
	private float Rot[] = new float[9];
	private float I[] = new float[9];
	private float grav[] = new float[3];
	private float mag[] = new float[3];
	private float results[] = new float[3];
	private SensorManager sensorMgr;
	private List<Sensor> sensors;
	private Sensor sensorGrav, sensorMag;


	public AugmentedRealityView(Context context) {
		super(context);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

	}

}
class CameraPreView extends SurfaceView implements SurfaceHolder.Callback {

	public CameraPreView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}
}
class RadarMarkersView extends View{

	public RadarMarkersView(Context context) {
		super(context);
	}

}
class ResolutionOrder implements java.util.Comparator<Camera.Size> {
	public int compare(Camera.Size left, Camera.Size right) {

		return Float.compare(left.width + left.height, right.width + right.height);
	}
}