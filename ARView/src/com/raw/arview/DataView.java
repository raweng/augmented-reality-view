package com.raw.arview;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.raw.utils.Camera;
import com.raw.utils.PaintUtils;
import com.raw.utils.RadarLines;


/**
 * 
 * Currently the markers are plotted with reference to line parallel to the earth surface.
 * We are working to include the elevation and height factors.
 * 
 * */


public class DataView {

	RelativeLayout[] locationMarkerView;
	ImageView[] subjectImageView;
	RelativeLayout.LayoutParams[] layoutParams;
	RelativeLayout.LayoutParams[] subjectImageViewParams;
	RelativeLayout.LayoutParams[] subjectTextViewParams;
	TextView[] locationTextView;

	
	
	/*
	 *  Array or Array lists of latitude and longitude to plot
	 *  In your case you can populate with an ArrayList
	 * */
	// SF Art Commission, SF Dept. of Public Health, SF Ethics Comm, SF Conservatory of Music, All Star Cafe, Magic Curry Cart, SF SEO Marketing, SF Honda, 
	// SF Mun Transport Agency, SF Parking Citation, Mayors Office of Housing, SF Redev Agency, Catario Patrice, Bank of America , SF Retirement System, Bank of America Mortage,
	// Writers Corp., Van Nes Keno Mkt.
	double[] latitudes = new double[] {37.775672, 37.775729, 37.775578, 37.77546, 37.775199, 37.774887, 37.774637, 
			37.774614, 37.774406, 37.774754, 37.774813, 37.774961, 37.774957, 37.775171, 37.775996, 37.775818, 37.775691, 37.775909};
	double[] longitudes = new double[] {-122.419992, -122.419601, -122.419719, -122.42026, -122.419646, -122.419405, -122.42037, 
			-122.41934, -122.41886, -122.418785, -122.418581, -122.418868, -122.418064, -122.418884, -122.418898, -122.418305, -122.418895, -122.419161};
	
	int[] nextXofText ;
	ArrayList<Integer> 	nextYofText = new ArrayList<Integer>();

	double[] bearings;
	float angleToShift;
	float yPosition;
	Location currentLocation = new Location("provider");
	Location destinedLocation = new Location("provider");
	
	String[] places = new String[] {"SF Art Commission", "SF Dept. of Public Health", "SF Ethics Comm", "SF Conservatory of Music", "All Star Cafe", 
			"Magic Curry Cart", "SF SEO Marketing", " SF Honda", "SF Mun Transport Agency", "SF Parking Citation", "Mayors Office of Housing", "SF Redev Agency", "Catario Patrice", "Bank of America", 
			"SF Retirement System", "Bank of America Mortage", "Writers Corp.", "Van Nes Keno Mkt."};
	/** is the view Inited? */
	boolean isInit = false;
	boolean isDrawing = true;
	boolean isFirstEntry;
	Context _context;
	/** width and height of the view*/
	int width, height;
	android.hardware.Camera camera;

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
			nextXofText = new int[latitudes.length];
			
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
						if (v.getId() != -1) {
							
							RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
							Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
							ArrayList<Integer> matchIDs = new ArrayList<Integer>();
							Rect compRect = new Rect();
							int index = 0;
							for (RelativeLayout.LayoutParams layoutparams : layoutParams) {
								compRect.set(layoutparams.leftMargin, layoutparams.topMargin, 
										layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
								if (compRect.intersect(rect)) {
									matchIDs.add(index);
								}
								index++;
							}
							
							if (matchIDs.size() > 1) {
								
							}
							Toast.makeText(_context, "Number of places here = "+matchIDs.size(), Toast.LENGTH_SHORT).show();
							
							locationMarkerView[v.getId()].bringToFront();
							
//							locationMarkerView[v.getId()].bringToFront();
//							Toast.makeText(_context, " LOCATION NO : "+v.getId(), Toast.LENGTH_SHORT).show();
						}
						
					}
				});


				locationTextView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if ((v.getId() != -1)) {
							
							RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
							Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
							ArrayList<Integer> matchIDs = new ArrayList<Integer>();
							Rect compRect = new Rect();
							int index = 0;
							for (RelativeLayout.LayoutParams layoutparams : layoutParams) {
								compRect.set(layoutparams.leftMargin, layoutparams.topMargin, 
										layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
								if (compRect.intersect(rect)) {
									matchIDs.add(index);
								}
								index++;
							}
							
							if (matchIDs.size() > 1) {
								
							}
							Toast.makeText(_context, "Number of places here = "+matchIDs.size(), Toast.LENGTH_SHORT).show();
							
							locationMarkerView[v.getId()].bringToFront();
							
//							locationMarkerView[v.getId()].bringToFront();
//							Toast.makeText(_context, " LOCATION NO : "+v.getId(), Toast.LENGTH_SHORT).show();
						}
						
					}
				});

				locationMarkerView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (v.getId() != -1) {
							RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
							Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
							ArrayList<Integer> matchIDs = new ArrayList<Integer>();
							Rect compRect = new Rect();
							int count = 0;
							int index = 0;
							for (RelativeLayout.LayoutParams layoutparams : layoutParams) {
								compRect.set(layoutparams.leftMargin, layoutparams.topMargin, 
										layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
								if (compRect.intersect(rect)) {
									matchIDs.add(index);
									count+=1;
								}
								index++;
							}
							
							if (count > 1) {
								
							}
							Toast.makeText(_context, "Number of places here = "+count, Toast.LENGTH_SHORT).show();
							
							locationMarkerView[v.getId()].bringToFront();
//							Toast.makeText(_context, " LOCATION NO : "+v.getId(), Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}



			bmp = BitmapFactory.decodeResource(_context.getResources(), R.drawable.icon);

			this.displayMetrics = displayMetrics;
			this.degreetopixelWidth = this.displayMetrics.widthPixels / camera.getParameters().getHorizontalViewAngle();
			this.degreetopixelHeight = this.displayMetrics.heightPixels / camera.getParameters().getVerticalViewAngle();
			System.out.println("camera.getParameters().getHorizontalViewAngle()=="+camera.getParameters().getHorizontalViewAngle());

			bearings = new double[latitudes.length];
			currentLocation.setLatitude(19.413958);
			currentLocation.setLongitude(72.82729);


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
			
			lrl.set(0, -RadarView.RADIUS);
			lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
			lrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
			rrl.set(0, -RadarView.RADIUS);
			rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
			rrl.add(rx + RadarView.RADIUS, ry + RadarView.RADIUS);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		/*
		 * initialization is done, so dont call init() again.
		 * */
		isInit = true;
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
	}

	void drawPOI(PaintUtils dw, float yaw){
		if(isDrawing){
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

			}else{
				dw.setColor(Color.rgb(0, 0, 0));
				dw.setFill(true);
				dw.paintRect((x - w / 2) + PaintUtils.XPADDING , (y - h / 2) + PaintUtils.YPADDING, w, h);
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
	
	public class NearbyPlacesList extends BaseAdapter{

		ArrayList<Integer> matchIDs = new ArrayList<Integer>();
		public NearbyPlacesList(ArrayList<Integer> matchID){
			matchIDs = matchID;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return matchIDs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}