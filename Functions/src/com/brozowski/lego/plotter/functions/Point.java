package com.brozowski.lego.plotter.functions;

public class Point {
	
	public static Point cartesion(double x, double y) {
		double r = Math.sqrt(x*x + y*y);
		double theta = Math.atan2(y, x);
		return polar(r, theta);
	}
	
	public static Point polar(double r, double theta) {
		return new Point(r, theta);
	}
	

	public double m_r = 0.0;
	public double m_theta = 0.0;
	
	public Point(double r, double theta) {
		m_r = r;
		m_theta = theta;
	}

	public double r() {
		return m_r;
	}
	
	public double theta() {
		return m_theta;
	}
	
	public double x() {
		return m_r*Math.cos(m_theta);
	}
	
	public double y() {
		return m_r*Math.sin(m_theta);
	}

}
