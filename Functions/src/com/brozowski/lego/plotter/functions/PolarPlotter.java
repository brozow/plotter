package com.brozowski.lego.plotter.functions;

public interface PolarPlotter {
	
	public static final double MM_PER_C_ROTATION = 3.18;
	
	// r = MM_PER_DEGREE_C * c;
	// c = r / MM_PER_DEGREE_C;
	public static final double MM_PER_DEGREE_C = MM_PER_C_ROTATION/360.0;
	
    // theta = RAD_PER_DEGREE_B * b;
	// b = theta / RAD_PER_DEGREE_B;
	public static final double RAD_PER_B_ROTATION = 2*Math.PI/56.0;
	
	public static final double RAD_PER_DEGREE_B = RAD_PER_B_ROTATION/360.0;
	
	public static final double RAD_PER_STEP = RAD_PER_B_ROTATION;

	double millimetersPerStep();
	
	double radiansPerStep();
	
	void start();
	
	void penDown();
	
	void penUp();
	
	void moveTo(Point p);

	void move(int rSteps, int thetaSteps);
	
	void finished();

}
