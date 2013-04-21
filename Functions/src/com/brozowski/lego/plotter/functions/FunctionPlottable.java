package com.brozowski.lego.plotter.functions;

public class FunctionPlottable implements Plottable {
	
	public static interface Function {
		double f(double theta);
	}
	
	private PolarPlotter m_plotter;
	private Function m_f;
	private Point m_start;
	private Point m_end;

	public FunctionPlottable(PolarPlotter plotter, Function f, Point start, Point end) {
		m_plotter = plotter;
		m_f = f;
		m_start = start;
		m_end = end;
	}
	
	private double f(double theta) {
		return m_f.f(theta);
	}
	
	public void plot() {
		
		System.err.printf("Expected: %f, Actual: %f\n", m_start.r(), f(m_start.theta()));
		System.err.printf("Expected: %f, Actual: %f\n", m_end.r(), f(m_end.theta()));
		
		m_plotter.moveTo(m_start);

		int resolution = 1;
		double start = m_start.theta();
		double end = m_end.theta();
		int direction = (end-start) < 0 ? -1 : 1; 
		double step = direction*resolution*m_plotter.radiansPerStep();
		
		
		double current = start;
		double currentR = f(current);
		System.err.printf("Current: %f, R: %f\n", currentR, m_start.r());
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
