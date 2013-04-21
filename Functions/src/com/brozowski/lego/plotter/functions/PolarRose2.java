package com.brozowski.lego.plotter.functions;

public class PolarRose2 implements Plottable {
	
	private PolarPlotter m_plotter;
	private Point m_p1;
	private Point m_p2;

	public PolarRose2(PolarPlotter plotter) {
		m_plotter = plotter;
		m_p1 = Point.polar(0, -1*Math.PI);
		m_p2 = Point.polar(0, Math.PI);
	}
	
	private double f(double theta) {
		return 40*Math.sin(4*theta);
	}
	
	public void plot() {
		
		System.err.printf("Expected: %f, Actual: %f\n", m_p1.r(), f(m_p1.theta()));
		System.err.printf("Expected: %f, Actual: %f\n", m_p2.r(), f(m_p2.theta()));
		
		m_plotter.start();
		
		
		m_plotter.moveTo(m_p1);

		int resolution = 1;
		double start = m_p1.theta();
		double end = m_p2.theta();
		int direction = (end-start) < 0 ? -1 : 1; 
		double step = direction*resolution*m_plotter.radiansPerStep();
		
		
		double current = start;
		double currentR = f(current);
		System.err.printf("Current: %f, R: %f\n", currentR, m_p1.r());
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
