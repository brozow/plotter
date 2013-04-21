package com.brozowski.lego.plotter.functions;

import com.brozowski.lego.plotter.functions.FunctionPlottable.Function;

public class Line extends FunctionPlottable {
	
	private static class LineFunction implements Function {
		private Point m_p1;
		private Point m_p2;
		
		public LineFunction(Point p1, Point p2) {
			m_p1 = p1;
			m_p2 = p2;
		}
		
		private double helper(double r1, double theta1, double r2, double theta2) {
			return r1*r2*Math.sin(theta2-theta1);
		}
		
		public double f(double theta) {
			double numerator = helper(m_p1.r(), m_p1.theta(), m_p2.r(), m_p2.theta());
			double den1 = helper(m_p1.r(), m_p1.theta(), 1, theta);
			double den2 = helper(m_p2.r(), m_p2.theta(), 1, theta);
			
			return numerator/(den1 - den2);
		}
	}
	
	private PolarPlotter m_plotter;

	public Line(PolarPlotter plotter, Point p1, Point p2) {
		super(plotter, new LineFunction(p1, p2), p1, p2);
		m_plotter = plotter;
	}
	


}
