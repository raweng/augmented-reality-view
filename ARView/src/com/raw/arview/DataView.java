package com.raw.arview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.raw.utils.Camera;
import com.raw.utils.PaintUtils;
import com.raw.utils.RadarLines;


public class DataView {

	RelativeLayout[] locationMarkerView;
	ImageView[] subjectImageView;
	RelativeLayout.LayoutParams[] layoutParams;
	RelativeLayout.LayoutParams[] subjectImageViewParams;
	RelativeLayout.LayoutParams[] subjectTextViewParams;
	TextView[] locationTextView;

	String[] latitudeStr;
	String[] longitudeStr;
	double[] latitudes = new double[] {19.415481, 19.480834, 19.480834,19.410623, 19.48043, 19.477153, 19.472742, 19.43341, 21.201377, 21.201377,21.201377,21.201377,21.201377, 19.470962};
	double[] longitudes = new double[] {72.782922, 72.777557,72.777557, 72.829056, 72.785797, 72.900167, 72.857466, 72.756271, 72.821159, 72.821159,72.821159,72.821159,72.821159, 72.799358};
	int[] nextXofText = new int[20];
	ArrayList<Integer> 	nextYofText = new ArrayList<Integer>();

	double[] bearings;
	float angleToShift;
	float yPosition;
	Location currentLocation = new Location("provider");
	Location destinedLocation = new Location("provider");
	String[] places = new String[] {"Wagholi", "Agashi Church","Agashi Church 223433", "Ach", "Chikhaldongari", "Shirsad Phata", "Viva College", "Rajodi Beach", "Surat, Gujrat", "Ahmedabad","Ahmedabad 1234","Baroda","baroda 1234", "Virar, Maharashtra"};
	int[] colors = new int[] {Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.WHITE, Color.YELLOW, Color.LTGRAY, Color.RED, Color.BLUE}; 

	/** is the view Inited? */
	boolean isInit = false;
	boolean isDrawing = true;
	boolean isFirstEntry;
	Context _context;
	/** width and height of the view*/
	int width, height;
	/** _NOT_ the android camera, the class that takes care of the transformation*/
	Camera cam;
	android.hardware.Camera camera;
	/** */
	/** The view can be "frozen" for debug purposes */
	boolean frozen = false;

	float yawPrevious;
	float yaw = 0;
	float pitch = 0;
	float roll = 0;

	DisplayMetrics displayMetrics;
	RadarView radarPoints;

	RadarLines lrl = new RadarLines();
	RadarLines rrl = new RadarLines();
	float rx = 10, ry = 20;
	public float addX = 0, addY = 0;
	public float degreetopixelWidth;
	public float degreetopixelHeight;
	public float pixelstodp;
	public float bearing;

	public int[][] coordinateArray = new int[20][2];
	public int locationBlockWidth;
	public int locationBlockHeight;

	public float deltaX;
	public float deltaY;
	Bitmap bmp;

	public DataView(Context ctx) {
		this._context = ctx;
	}


	public boolean isInited() {
		return isInit;
	}

	public void init(int widthInit, int heightInit, android.hardware.Camera camera, DisplayMetrics displayMetrics, RelativeLayout rel) {
		try {
			locationMarkerView = new RelativeLayout[latitudes.length];
			layoutParams = new RelativeLayout.LayoutParams[latitudes.length];
			subjectImageViewParams = new RelativeLayout.LayoutParams[latitudes.length];
			subjectTextViewParams = new RelativeLayout.LayoutParams[latitudes.length];
			subjectImageView = new ImageView[latitudes.length];
			locationTextView = new TextView[latitudes.length];
			for(int i=0;i<latitudes.length;i++){
				layoutParams[i] = new RelativeLayout.LayoutParams(10, 10);
				subjectTextViewParams[i] = new RelativeLayout.LayoutParams(50, 30);

				subjectImageView[i] = new ImageView(_context);
				locationMarkerView[i] = new RelativeLayout(_context);
				locationTextView[i] = new TextView(_context);
				locationTextView[i].setText(checkTextToDisplay(places[i]));
				locationTextView[i].setTextColor(Color.WHITE);
				subjectImageView[i].setBackgroundResource(R.drawable.icon);
				locationMarkerView[i].setId(i);
				subjectImageView[i].setId(i);
				locationTextView[i].setId(i);
				subjectImageViewParams[i] = new  RelativeLayout.LayoutParams(40, 40);
				subjectImageViewParams[i].topMargin = 15;
				subjectImageViewParams[i].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
				layoutParams[i].setMargins(displayMetrics.widthPixels/2, displayMetrics.heightPixels/2, 0, 0);
				locationMarkerView[i] = new RelativeLayout(_context);
				locationMarkerView[i].setBackgroundResource(R.drawable.thoughtbubble);
				subjectTextViewParams[i].addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				subjectTextViewParams[i].topMargin = 15;
				locationMarkerView[i].setLayoutParams(layoutParams[i]);
				subjectImageView[i].setLayoutParams(subjectImageViewParams[i]);
				locationTextView[i].setLayoutParams(subjectTextViewParams[i]);

				locationMarkerView[i].addView(subjectImageView[i]);
				locationMarkerView[i].addView(locationTextView[i]);
				rel.addView(locationMarkerView[i]);

				subjectImageView[i].setClickable(false);
				locationTextView[i].setClickable(false);

				subjectImageView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						locationMarkerView[v.getId()].bringToFront();
						Toast.makeText(_context, " LOCATION NO : "+v.getId(), Toast.LENGTH_SHORT).show();
					}
				});


				locationTextView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						locationMarkerView[v.getId()].bringToFront();
						Toast.makeText(_context, " LOCATION NO : "+v.getId(), Toast.LENGTH_SHORT).show();
					}
				});

				locationMarkerView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						locationMarkerView[v.getId()].bringToFront();
						Toast.makeText(_context, " LOCATION NO : "+v.getId(), Toast.LENGTH_SHORT).show();
					}
				});
			}

			latitudeStr = new String[latitudes.length];
			longitudeStr = new String[longitudes.length];

			for(int i=0;i<latitudes.length;i++){
				latitudeStr[i] = Double.toString(latitudes[i]);
				longitudeStr[i] = Double.toString(longitudes[i]);
			}

			bmp = BitmapFactory.decodeResource(_context.getResources(), R.drawable.icon);

			this.displayMetrics = displayMetrics;
			this.degreetopixelWidth = this.displayMetrics.widthPixels / camera.getParameters().getHorizontalViewAngle();
			this.degreetopixelHeight = this.displayMetrics.heightPixels / camera.getParameters().getVerticalViewAngle();


			bearings = new double[latitudes.length];
			currentLocation.setLatitude(19.413983);
			currentLocation.setLongitude(72.827511);


			if(bearing < 0)
				bearing  = 360 + bearing;

			for(int i = 0; i <latitudes.length;i++){
				destinedLocation.setLatitude(latitudes[i]);
				destinedLocation.setLongitude(longitudes[i]);
				bearing = currentLocation.bearingTo(destinedLocation);

				if(bearing < 0){
					bearing  = 360 + bearing;
				}
				bearings[i] = bearing;

			}
			radarPoints = new RadarView(this, bearings);
			this.camera = camera;
			width = widthInit;
			height = heightInit;

			cam = new Camera(width, height, true);
			cam.setViewAngle(Camera.DEFAULT_VIEW_ANGLE);

			lrl.set(0, -RadarView.RADIUS);
			lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
			lrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
			rrl.set(0, -RadarView.RADIUS);
			rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
			rrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		frozen = false;
		isInit = true;
		isFirstEntry = true;
	}

	public void draw(PaintUtils dw, float yaw, float pitch, float roll) {


		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;

		// Draw Radar
		String	dirTxt = "";
		int bearing = (int) this.yaw; 
		int range = (int) (this.yaw / (360f / 16f));
		if (range == 15 || range == 0) dirTxt = "N"; 
		else if (range == 1 || range == 2) dirTxt = "NE"; 
		else if (range == 3 || range == 4) dirTxt = "E"; 
		else if (range == 5 || range == 6) dirTxt = "SE";
		else if (range == 7 || range == 8) dirTxt= "S"; 
		else if (range == 9 || range == 10) dirTxt = "SW"; 
		else if (range == 11 || range == 12) dirTxt = "W"; 
		else if (range == 13 || range == 14) dirTxt = "NW";


		radarPoints.view = this;

		dw.paintObj(radarPoints, rx+PaintUtils.XPADDING, ry+PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
		dw.setFill(false);
		dw.setColor(Color.argb(100,220,0,0));
		dw.paintLine( lrl.x, lrl.y, rx+RadarView.RADIUS, ry+RadarView.RADIUS); 
		dw.paintLine( rrl.x, rrl.y, rx+RadarView.RADIUS, ry+RadarView.RADIUS);
		dw.setColor(Color.rgb(255,255,255));
		dw.setFontSize(12);
		radarText(dw, "" + bearing + ((char) 176) + " " + dirTxt, rx + RadarView.RADIUS, ry - 5, true, false, -1);


		drawTextBlock(dw);
		//		drawPOI(dw,yawPrevious);
	}

	void drawPOI(PaintUtils dw, float yaw){
		if(isDrawing){
			//			yawPrevious = this.yaw;
			dw.paintObj(radarPoints, rx+PaintUtils.XPADDING, ry+PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
			isDrawing = false;
		}
	}

	void radarText(PaintUtils dw, String txt, float x, float y, boolean bg, boolean isLocationBlock, int count) {

		float padw = 4, padh = 2;
		float w = dw.getTextWidth(txt) + padw * 2;
		float h;
		if(isLocationBlock){
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2+10;
		}else{
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
		}
		if (bg) {

			if(isLocationBlock){
				layoutParams[count].setMargins((int)(x - w / 2 - 10), (int)(y - h / 2 - 10), 0, 0);
				layoutParams[count].height = 90;
				layoutParams[count].width = 90;
				locationMarkerView[count].setLayoutParams(layoutParams[count]);

				//				dw.setColor(Color.argb(120, 0, 0, 0));
				//				dw.setFill(true);
				//				locationBlockHeight = bmp.getHeight();
				//				locationBlockWidth = (int)(w +bmp.getWidth()+10);
				//				dw.paintRect(x - w / 2 - 10, y - h / 2 - 10, bmp.getWidth()+200, bmp.getHeight());
				//				dw.setColor(Color.rgb(255, 255, 255));
				//				dw.setFill(false);
				//				dw.setFontStyle(Typeface.BOLD);
				//				dw.getCanvas().drawBitmap(bmp,x - w / 2 - 10, y - h / 2 - 10, dw.paint);
				//				dw.paintText(padw + x - w / 2 + bmp.getWidth(), dw.getTextAsc() + y - h / 2 ,str);
				//				dw.paintText(padw + x - w / 2 + bmp.getWidth(), dw.getTextAsc() + y - h / 2 + 15,Float.toString(this.pitch));

			}else{
				dw.setColor(Color.rgb(0, 0, 0));
				dw.setFill(true);
				dw.paintRect((x - w / 2) + PaintUtils.XPADDING , (y - h / 2) + PaintUtils.YPADDING, w, h);
//				float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (padw + x - w / 2), displayMetrics);
				pixelstodp = (padw + x - w / 2)/((displayMetrics.density)/160);
				dw.setColor(Color.rgb(255, 255, 255));
				dw.setFill(false);
				dw.paintText((padw + x -w/2)+PaintUtils.XPADDING, ((padh + dw.getTextAsc() + y - h / 2)) + PaintUtils.YPADDING,txt);
			}
		}

	}

	String checkTextToDisplay(String str){

		if(str.length()>15){
			str = str.substring(0, 15)+"...";
		}
		return str;

	}
	/*nextXofText[i] array stores the xposition of rect to be drawn. Updates on every onDraw*/
	boolean checkForOverLapping(int XCoordinate,int position){
		boolean isInRange = false;

		for (int i = 0; i < nextXofText.length; i++) {
			if(coordinateArray[i][0]<= coordinateArray[position][0] && coordinateArray[position][0]<= coordinateArray[i][0]+40){
				isInRange = true;
				break;
			}
		}

		return isInRange;
	}



	void drawTextBlock(PaintUtils dw){

		for(int i = 0; i<bearings.length;i++){
			if(bearings[i]<0){

				if(this.pitch != 90){
					yPosition = (this.pitch - 90) * this.degreetopixelHeight+200;
				}else{
					yPosition = (float)this.height/2;
				}

				bearings[i] = 360 - bearings[i];
				angleToShift = (float)bearings[i] - this.yaw;
				nextXofText[i] = (int)(angleToShift*degreetopixelWidth);
				yawPrevious = this.yaw;
				isDrawing = true;
				radarText(dw, places[i], nextXofText[i], yPosition, true, true, i);
				coordinateArray[i][0] =  nextXofText[i];
				coordinateArray[i][1] =   (int)yPosition;

			}else{
				angleToShift = (float)bearings[i] - this.yaw;

				if(this.pitch != 90){
					yPosition = (this.pitch - 90) * this.degreetopixelHeight+200;
				}else{
					yPosition = (float)this.height/2;
				}


				nextXofText[i] = (int)((displayMetrics.widthPixels/2)+(angleToShift*degreetopixelWidth));
				if(Math.abs(coordinateArray[i][0] - nextXofText[i]) > 50){
					radarText(dw, places[i], (nextXofText[i]), yPosition, true, true, i);
					coordinateArray[i][0] =  (int)((displayMetrics.widthPixels/2)+(angleToShift*degreetopixelWidth));
					coordinateArray[i][1] =  (int)yPosition;

					isDrawing = true;
				}else{
					radarText(dw, places[i],coordinateArray[i][0],yPosition, true, true, i);
					isDrawing = false;
				}
			}
		}
	}
}
