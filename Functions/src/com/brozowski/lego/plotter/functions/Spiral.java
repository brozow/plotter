package com.brozowski.lego.plotter.functions;

public class Spiral implements Plottable {
	
	private PolarPlotter m_plotter;

	public Spiral(PolarPlotter plotter) {
		m_plotter = plotter;
	}
	
	private double helper(double r1, double theta1, double r2, double theta2) {
		return r1*r2*Math.sin(theta2-theta1);
	}
	
	private double f(double theta) {
		return (10.0/(2.0*Math.PI))*theta;
	}
	
	public void plot() {
		
		m_plotter.start();
		
		
		Point p = Point.cartesion(0, 0);

		m_plotter.moveTo(p);
		

		double start = 0.0;
		double end = 3.9*2*Math.PI;
		int resolution = 20;
		int direction = 1;
		double step = resolution*direction*m_plotter.radiansPerStep();
		
		double current = start;
		double currentR = f(current);
		System.err.printf("Current: %f, R: %f\n", currentR, 0.0);
//		m_plotter.move((int)(m_p1.r()/m_plotter.millimetersPerStep()), (int)(start/m_plotter.radiansPerStep()));
		m_plotter.penDown();
		while (direction*(end - current) > 0) {
			current += step;
			double newR = f(current);
			int rSteps = (int) Math.round((newR - currentR)/m_plotter.millimetersPerStep());
			System.err.printf("(%f, %f): ", newR, current);
			m_plotter.move(rSteps, direction*resolution);
			currentR = currentR+rSteps*m_plotter.millimetersPerStep();
		}
		
		m_plotter.penUp();
		
		m_plotter.finished();
				
	}

}
