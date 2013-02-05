package com.brozowski.lego.plotter.functions;

public class Line {
	
	private PolarPlotter m_plotter;
	private Point m_p1;
	private Point m_p2;

	public Line(PolarPlotter plotter, Point p1, Point p2) {
		m_plotter = plotter;
		m_p1 = p1;
		m_p2 = p2;
	}
	
	private double helper(double r1, double theta1, double r2, double theta2) {
		return r1*r2*Math.sin(theta2-theta1);
	}
	
	private double f(double theta) {
		double numerator = helper(m_p1.r(), m_p1.theta(), m_p2.r(), m_p2.theta());
		double den1 = helper(m_p1.r(), m_p1.theta(), 1, theta);
		double den2 = helper(m_p2.r(), m_p2.theta(), 1, theta);
		
		return numerator/(den1 - den2);
	}
	
	public void plot() {
		
		System.err.printf("Expected: %f, Actual: %f\n", m_p1.r(), f(m_p1.theta()));
		System.err.printf("Expected: %f, Actual: %f\n", m_p2.r(), f(m_p2.theta()));
		
		m_plotter.start();
		
		
		double start = m_p1.theta();
		double end = m_p2.theta();
		int direction = (end-start) < 0 ? -1 : 1; 
		double step = direction*m_plotter.radiansPerStep();
		
		
		double current = start;
		double currentR = f(current);
		m_plotter.move((int)(currentR/m_plotter.millimetersPerStep()), (int)(start/m_plotter.radiansPerStep()));
		m_plotter.penDown();
		while (direction*(end - current) > 0) {
			current += step;
			double newR = f(current);
			int rSteps = (int) Math.round((newR - currentR)/m_plotter.millimetersPerStep());
			System.err.printf("(%f, %f): ", newR, current);
			m_plotter.move(rSteps, direction);
			currentR = currentR+rSteps*m_plotter.millimetersPerStep();
		}
		
		m_plotter.penUp();
		
		m_plotter.finished();
				
	}

}
