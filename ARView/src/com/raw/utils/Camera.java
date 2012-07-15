package com.raw.utils;

public class Camera {
	
	public static float DEFAULT_VIEW_ANGLE = (float) Math.toRadians(45);
	public int width, height;

	float viewAngle;
	float dist;

	public Camera(int width, int height) {
		this(width, height, true);
	}

	public Camera(int width, int height, boolean init) {
		this.width = width;
		this.height = height;


	}

	public void setViewAngle(float viewAngle) {
		this.viewAngle = viewAngle;
		this.dist = (this.width / 2)
				/ (float) Math.tan(viewAngle / 2);
	}

	public void setViewAngle(int width, int height, float viewAngle) {
		this.viewAngle = viewAngle;
		this.dist = (width / 2) / (float) Math.tan(viewAngle / 2);
	}

	
	@Override
	public String toString() {
		return " ";
	}
}
