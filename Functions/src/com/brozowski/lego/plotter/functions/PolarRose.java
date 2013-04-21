package com.brozowski.lego.plotter.functions;

public class PolarRose implements Plottable {
	
	public static final int TEN = 10;
	
	public static final double PETAL_LENGTH_IN_MM = 35;
	
	private PolarPlotter m_plotter;
    
	public PolarRose(PolarPlotter plotter) {
		m_plotter = plotter;
	}
	
	public double f(double theta) {
		return PETAL_LENGTH_IN_MM*Math.sin(4*theta);
	}
	
	public double fprime(double theta) {
		return PETAL_LENGTH_IN_MM*Math.cos(4*theta);
	}
	
	public double fprimeInv(double theta) {
		return 1.0/fprime(theta);
	}
	
	private double g(double b) {
		return f(radiansPerThetaStep()*b)/millimetersPerRStep();
	}
	
	private double gprime(double b) {
		return fprime(radiansPerThetaStep()*b)*radiansPerThetaStep()/millimetersPerRStep();
	}
	
	private double gprimeInv(double b) {
		return fprimeInv(radiansPerThetaStep()*b)*millimetersPerRStep()/radiansPerThetaStep();
	}

	private double millimetersPerRStep() {
		return m_plotter.millimetersPerStep();
	}
	
	private double G(double c, double b) {
		return c - g(b);
	}
	
	// the dy/dx of G(x,y) is -GsubX(x,y)/GsubY(x,y)
	private double slope(double c, double b) {
		return gprime(b);
	}
	
	double theta(double b) {
		return radiansPerThetaStep() * b;
	}

	private double radiansPerThetaStep() {
		return m_plotter.radiansPerStep();
	}

	double r(double c) {
		return millimetersPerRStep()*c;
	}
	
	double b(double theta) {
		return theta/radiansPerThetaStep();
	}
	
	double c(double r) {
		return r/millimetersPerRStep();
	}

	public void plot(double startTheta, double endTheta) {
		
		
		// assume that pen is at (f(startTheta), startTheta) 
		// and that this point is the starting point of the curve
		
		double startB = startTheta/radiansPerThetaStep();
		double endB   = endTheta/radiansPerThetaStep();
		double startC = g(startB);
		
		double b = startB;
		double c = startC;

		m_plotter.start();
		
		m_plotter.penDown();

		// B is only increasing?
		while(b < endB) {
			double slope = slope(c, b);
			if (slope > 1) {
				// sharp positive slope
				// increment C and try B and B+1 to see which is closer
				c += 1;
				if (Math.abs(G(c, b)) <= Math.abs(G(c, b+1))) {
					draw(c, b, 1, 0);
				} else {
					b += 1;
					draw(c, b, 1, 1);
				}
			} else if (slope >= 0) {
				// gentle positive slope
				// increment B and try C and C+1 to see which is closer
				b += 1;
				if (Math.abs(G(c, b)) <= Math.abs(G(c + 1, b))) {
					draw(c, b, 0, 1);
				} else {
					c += 1;
					draw(c, b, 1, 1);
				}
			} else if (slope >= -1) {
				// gentle negative slope
				// increment B and try C and C-1 to see which is closer
				b += 1;
				if (Math.abs(G(c, b)) <= Math.abs(G(c - 1, b))) {
					draw(c, b, 0, 1);
				} else {
					c -= 1;
					draw(c, b, -1, 1);
				}
			} else {
				// sharp negative slope
				// increment C and try B and B-1 to see which is closer
				c -= 1;
				if (Math.abs(G(c, b)) <= Math.abs(G(c, b+1))) {
					draw(c, b, -1, 0);
				} else {
					b += 1;
					draw(c, b, -1, 1);
				}
			}
		}
		
		m_plotter.penUp();
		
		m_plotter.finished();
		
	}
	
	public void plot() {
		plot(-Math.PI, Math.PI);
	}

	/**
	 * The draws relative to the current position either forward one, backward one or 
	 * stay the same by passing in 1, 0, or -1 respectively.
	 * @param c
	 * @param d
	 */
	private void draw(double c, double b, int rSteps, int thetaSteps) {
		//System.err.printf("(%f, %f) m = (%f): [%f, %f] %d %d\n", r(c), theta(b)*360/(2*Math.PI), slope(c,b), c, b, incC, incB);
		
		m_plotter.move(rSteps, thetaSteps);
	}

}
