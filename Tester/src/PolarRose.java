
public class PolarRose {
	
	public static final int TEN = 10;
	
	public static final double PETAL_LENGTH_IN_MM = 35;
	
	public static final double MM_PER_C_ROTATION = 3.18;
	
	// r = MM_PER_DEGREE_C * c;
	// c = r / MM_PER_DEGREE_C;
	public static final double MM_PER_DEGREE_C = MM_PER_C_ROTATION/360.0;
	
	public static final double RAD_PER_B_ROTATION = 2*Math.PI/56.0;
	
	public static final double RAD_PER_DEGREE_B = RAD_PER_B_ROTATION/360.0;
	
    // theta = RAD_PER_DEGREE_B * b;
	// b = theta / RAD_PER_DEGREE_B;
	
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
		return f(RAD_PER_DEGREE_B*b)/MM_PER_DEGREE_C;
	}
	
	private double gprime(double b) {
		return fprime(RAD_PER_DEGREE_B*b)*RAD_PER_DEGREE_B/MM_PER_DEGREE_C;
	}
	
	private double gprimeInv(double b) {
		return fprimeInv(RAD_PER_DEGREE_B*b)*MM_PER_DEGREE_C/RAD_PER_DEGREE_B;
	}
	
	private double G(double c, double b) {
		return c - g(b);
	}
	
	// the dy/dx of G(x,y) is -GsubX(x,y)/GsubY(x,y)
	private double slope(double c, double b) {
		return gprime(b);
	}
	
	double theta(double b) {
		return RAD_PER_DEGREE_B * b;
	}
	
	double r(double c) {
		return MM_PER_DEGREE_C*c;
	}
	
	double b(double theta) {
		return theta/RAD_PER_DEGREE_B;
	}
	
	double c(double r) {
		return r/MM_PER_DEGREE_C;
	}
	
	void plot(double startTheta, double endTheta) {
		// assume that pen is at (f(startTheta), startTheta) 
		// and that this point is the starting point of the curve
		
		double startB = startTheta/RAD_PER_DEGREE_B;
		double endB   = endTheta/RAD_PER_DEGREE_B;
		double startC = g(startB);
		
		double b = startB;
		double c = startC;

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
		
	}

	/**
	 * The draws relative to the current position either forward one, backward one or 
	 * stay the same by passing in 1, 0, or -1 respectively.
	 * @param c
	 * @param d
	 */
	private void draw(double c, double b, int incC, int incB) {
		System.err.printf("(%f, %f) m = (%f): [%f, %f] %d %d\n", r(c), theta(b)*360/(2*Math.PI), slope(c,b), c, b, incC, incB);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
