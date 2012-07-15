package com.raw.utils;

import com.raw.arview.RadarView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class PaintUtils {
	
	public static int XPADDING = 0;
	public static int YPADDING = 50;
	
	Canvas canvas;
	int width, height;
	Typeface typeface2;
	public Paint paint = new Paint();
	Paint bPaint = new Paint();

	public PaintUtils() {
		paint.setTextSize(16);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setFill(boolean fill) {
		if (fill)
			paint.setStyle(Paint.Style.FILL);
		else
			paint.setStyle(Paint.Style.STROKE);
	}

	public void setColor(int c) {
		paint.setColor(c);
	}

	public void setStrokeWidth(float w) {
		paint.setStrokeWidth(w);
	}

	public void paintLine(float x1, float y1, float x2, float y2) {
		canvas.drawLine(x1+XPADDING, y1+YPADDING, x2+XPADDING, y2+YPADDING, paint);
	}

	public void paintRect(float x, float y, float width, float height) {
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	public void paintCircle(float x, float y, float radius) {
		canvas.drawCircle(x, y, radius, paint);
	}

	public void paintText(float x, float y, String text) {
		canvas.drawText(text, x, y, paint);
	}

	public void paintObj(RadarView obj, float x, float y, float rotation,
			float scale, float yaw) {
		canvas.save();
		canvas.translate(x + obj.getWidth() / 2, y + obj.getHeight() / 2);
		canvas.rotate(rotation);
		canvas.scale(scale, scale);
		canvas.translate(-(obj.getWidth() / 2), -(obj.getHeight() / 2));
		obj.paint(this, yaw);
		canvas.restore();
	}

	public float getTextWidth(String txt) {
		return paint.measureText(txt);
	}

	public float getTextAsc() {
		return -paint.ascent();
	}

	public float getTextDesc() {
		return paint.descent();
	}

	public void setFontSize(float size) {
		paint.setTextSize(size);
	}
	
	public void setFontStyle(int typeface){
		typeface2 = Typeface.create(Typeface.DEFAULT, typeface);
		paint.setTypeface(typeface2);
	}
}