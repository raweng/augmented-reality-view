package com.raw.arview;

import android.graphics.Color;
import android.location.Location;
import android.util.TypedValue;

import com.raw.utils.PaintUtils;


public class RadarView{
	/** The screen */
	public DataView view;
	/** The radar's range */
	float range;
	/** Radius in pixel on screen */
	public static float RADIUS = 40;
	/** Position on screen */
	static float originX = 0 , originY = 0;
	/** Color */
	static int radarColor = Color.argb(100, 220, 0, 0);
	Location currentLocation = new Location("provider");
	Location destinedLocation = new Location("provider");
	
//	double[] latitudes = new double[] {19.415481};
//	double[] longitudes = new double[] {72.782922};
	
	double[] latitudes = new double[] {19.415481, 19.480834, 19.410623, 19.48043, 19.477153, 19.472742, 19.43341, 21.201377};
	double[] longitudes = new double[] {72.782922, 72.777557, 72.829056, 72.785797, 72.900167, 72.857466, 72.756271, 72.821159};
	
	public float[][] coordinateArray = new float[latitudes.length][2];
	
//	double[] bearings = new double[latitudes.length];
	float angleToShift;
	public float degreetopixel;
	public float bearing;
	public float circleOriginX;
	public float circleOriginY;
	float scale;
	
	String[] places = new String[] {"Achole Talao Ring"};
	
	
	public float x = 0;
	public float y = 0;
	public float z = 0;

	float  yaw = 0;
	double[] bearings;
	ARView arView = new ARView();
	
	public RadarView(DataView dataView, double[] bearings){
		this.bearings = bearings;
	}
	
	public void paint(PaintUtils dw, float yaw) {
		
		circleOriginX = originX + RADIUS;
		circleOriginY = originY + RADIUS;
		this.yaw = yaw;
		range = arView.convertToPix(10) * 1000;		/** Draw the radar */
		dw.setFill(true);
		dw.setColor(radarColor);
		dw.paintCircle(originX + RADIUS, originY + RADIUS, RADIUS);
//		scale = range / RADIUS;
//		
//		calculateDistances(dw, this.yaw);
		/** put the markers in it */
		float scale = range / arView.convertToPix((int)RADIUS);
		currentLocation.setLatitude(19.474037);
		currentLocation.setLongitude(72.800388);

		
		for(int i = 0; i <latitudes.length;i++){
			destinedLocation.setLatitude(latitudes[i]);
			destinedLocation.setLongitude(longitudes[i]);
			convLocToVec(currentLocation, destinedLocation);
			float x = this.x / scale;
			float y = this.z / scale;

//			System.out.println("--------x"+i+"------------"+"("+x+")");
//			System.out.println("--------y"+i+"------------"+"("+y+")");
			
			if (x * x + y * y < RADIUS * RADIUS) {
				dw.setFill(true);
				dw.setColor(Color.rgb(255, 255, 255));
				dw.paintRect(x + RADIUS, y + RADIUS, 2, 2);
			}
		}
	}

	public void calculateDistances(PaintUtils dw, float yaw){
		currentLocation.setLatitude(19.474037);
		currentLocation.setLongitude(72.800388);
		for(int i = 0; i <latitudes.length;i++){
			if(bearings[i]<0){
				bearings[i] = 360 - bearings[i];
			}
			if(Math.abs(coordinateArray[i][0] - yaw) > 3){
				angleToShift = (float)bearings[i] - this.yaw;
				coordinateArray[i][0] = this.yaw;
			}else{
				angleToShift = (float)bearings[i] - coordinateArray[i][0] ;
				
			}
			destinedLocation.setLatitude(latitudes[i]);
			destinedLocation.setLongitude(longitudes[i]);
			float[] z = new float[1];
			z[0] = 0;
			Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), destinedLocation.getLatitude(), destinedLocation.getLongitude(), z);
			bearing = currentLocation.bearingTo(destinedLocation);
//			System.out.println("-----------YAW-----------|");
//			System.out.println("-----------angleToShift-----------|"+angleToShift);
			this.x = (float) (circleOriginX + 40 * (Math.cos(angleToShift)));
			this.y = (float) (circleOriginY + 40 * (Math.sin(angleToShift)));
//			float x = this.x / scale;
//			float y = this.y / scale;
			System.out.println("------DISTANCES---"+z[0]+"----");
//			System.out.println("----------CIRCLE ORIGIN----------X|"+circleOriginX+"----Y|"+circleOriginY);
//			System.out.println("------------X COORDINATE"+i+"----------"+"("+this.x +")");
//			System.out.println("------------Y COORDINATE"+i+"----------"+"("+this.y+")");
			if (x * x + y * y < RADIUS * RADIUS) {
				dw.setFill(true);
				dw.setColor(Color.rgb(255, 255, 255));
				dw.paintRect(x + RADIUS - 1, y + RADIUS - 1, 2, 2);
			}
		}
	}
	
	/** Width on screen */
	public float getWidth() {
		return RADIUS * 2;
	}

	/** Height on screen */
	public float getHeight() {
		return RADIUS * 2;
	}
	
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void convLocToVec(Location source, Location destination) {
		float[] z = new float[1];
		z[0] = 0;
		Location.distanceBetween(source.getLatitude(), source.getLongitude(), destination
				.getLatitude(), source.getLongitude(), z);
		float[] x = new float[1];
		Location.distanceBetween(source.getLatitude(), source.getLongitude(), source
				.getLatitude(), destination.getLongitude(), x);
		double y = destination.getAltitude() - source.getAltitude();
		if (source.getLatitude() < destination.getLatitude())
			z[0] *= -1;
		if (source.getLongitude() > destination.getLongitude())
			x[0] *= -1;

		set(x[0], (float) 0, z[0]);
	}
}

