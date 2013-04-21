package com.brozowski.lego.plotter.functions;

public class CenterStar implements Plottable {
	
	private PolarPlotter m_plotter;

	public CenterStar(PolarPlotter plotter) {
		m_plotter = plotter;
	}
	
	public void plot() {
		
		int rSteps = (int) Math.round(30.0 / m_plotter.millimetersPerStep());
		int thetaSteps = (int) Math.round( Math.toRadians(120.0) / m_plotter.radiansPerStep() );

		m_plotter.start();
		
		// start at center
		m_plotter.moveTo(Point.polar(0, 0));

		// move out
		m_plotter.move(rSteps/2, 0);
		
		m_plotter.penDown();
		
		// draw line
		m_plotter.move(-rSteps, 0);
		
		m_plotter.penUp();

		// turn 120 degrees
		m_plotter.move(0, thetaSteps);
		
		m_plotter.penDown();
		
		// draw line
		m_plotter.move(rSteps, 0);
		
		m_plotter.penUp();
		
		// turn 120 degrees
		m_plotter.move(0,  thetaSteps);
		
		m_plotter.penDown();
		
		// draw line
		m_plotter.move(-rSteps, 0);
		
		m_plotter.penUp();
		
		m_plotter.finished();
				
	}

}
