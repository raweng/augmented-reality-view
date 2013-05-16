# Augmented Reality View for Android 


**AugmentedRealityView** project was created by the mobile development team at [raw engineering](http://raweng.com "raw engineering."), and is available on [Github](https://github.com/Bhide/AugmentedRealityView.git). This project can be integrated into any other app, needing AugmentedRealityView.


[Augmented reality](http://en.wikipedia.org/wiki/Augmented_reality) (AR) is a live, direct or indirect, view of a physical, real-world environment whose elements are augmented by computer-generated sensory input such as sound, video, graphics or GPS data. It is related to a more general concept called mediated reality, in which a view of reality is modified (possibly even diminished rather than augmented) by a computer. As a result, the technology functions by enhancing one’s current perception of reality.


## Software and Algorithms 
https://raw.github.com/Bhide/AugmentedRealityView/master/screenshots/places_near_list.png
A key measure of AR systems is how realistically they integrate augmentations with the real world. The software must derive real world coordinates, independent of camera images.

Our AugmentedRealityView app uses android phone sensors and certain physics concepts for calculating the Point Of Interest (POIs).
The Camera Angle of View and Android's magnetic and accelerometer sensors are considered in calculating the azimuth (yaw), pitch and roll positions.

Android's `SurfaceView` is used to render the device camera and put markers onto it.  
![image](https://github.com/Bhide/AugmentedRealityView/blob/master/screenshots/ARView.png)
![image](https://raw.github.com/Bhide/AugmentedRealityView/master/screenshots/ARView.png)  
**Figure: AugmentedRealityView for Android**

Two main classes that comprise this View are:

1. RadarView.
2. DataView.

`RadarView` takes care of the Radar shown on the left top corner. It shows the number of points that are in the neighborhood; the points in the "V" notch is the area that is facing the camera.

The points in the "V" notch are in the "[Angle of View](http://en.wikipedia.org/wiki/Angle_of_view)" of camera.

`DataView` takes care of all the data and markers that are shown on the phone screen. Just pass the current location coordinates (latitude,longitude) in variables and the `ArrayList` of locations of places you have to plot.  

	public void init(int widthInit, int heightInit, android.hardware.Camera camera, DisplayMetrics displayMetrics, RelativeLayout rel) {
		try {
			locationMarkerView = new RelativeLayout[latitudes.length];
			layoutParams = new RelativeLa.......... ... . .
	                        .......... . ........... .. ... .
		    }
	}  
	
	
The `init ()` method allows you to initialize the variable sets, which is used for calculating the necessary values for plotting the markers.   
ex. `onClickListeners` of markers, camera view angles, screen dimensions.  


	public void draw(PaintUtils dw, float yaw, float pitch, float roll) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;	
	}


The `draw ()` method paints the Radar View on the left top corner.

	void radarText(PaintUtils dw, String txt, float x, float y, boolean bg, boolean isLocationBlock, int count) 		
	{
		if(isLocationBlock){
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2+10;
		}
		else{
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
		}
		if (bg) {
			if(isLocationBlock){
				layoutParams[count].setMargins((int)(x - w / 2 - 10), (int)(y - h / 2 - 10), 0, 0);
				layoutParams[count].height = 90;
				layoutParams[count].width = 90;
				locationMarkerView[count].setLayoutParams(layoutParams[count]);
			......... . ....... . ........ ......... . .
		}
	}
		
The `radarText ()` method is responsible for plotting the markers.
