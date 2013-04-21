import com.brozowski.lego.plotter.functions.Point;
import com.brozowski.lego.plotter.functions.PolarPlotter;

public class TestPlotter implements PolarPlotter {
	
	// this is stored in +/- step counts with the center being zero
	private int m_rPosition = 0;
	
	// this is store in +/- step counts with horizontal right being zero.
	private int m_thetaPosition = 0;
	
	// only draws with pen down
	private boolean m_down = false;
	
	private final TestPlotDisplay m_display;
	
	public TestPlotter() {
		m_display = new TestPlotDisplay();
	}
	
	
	@Override
	public double millimetersPerStep() {
		// R axis is supposed to move 3.18 mm for each complete turn of the motor
		return 3.18/360.0;
	}

	@Override
	public double radiansPerStep() {
		// For each complete turn of the motor, the turn table turns 1/56 of the way a round
		// since their are 56 teeth on the turn table gear. 
		return Math.PI/(56.0*180.0);
	}
	
	public double degressPerStep() {
		return 1.0/56.0;
	}
	
	@Override
	public void start() {
	}
	
	public boolean isPenDown() {
		return m_down;
	}

	@Override
	public void penDown() {
		m_down = true;
	}

	@Override
	public void penUp() {
		m_down = false;
	}
	
	private double r(double rPosition) {
		return rPosition*millimetersPerStep();
	}
 	
	private double r() {
		return r(m_rPosition);
	}
	
	private double theta(double thetaPosition) {
		return thetaPosition*radiansPerStep();
	}
	
	private double thetaDegrees( ) {
		return m_thetaPosition*degressPerStep();
	}
	
	
	@Override
	public void move(int rSteps, int thetaSteps) {
		
		int oldRPosition = m_rPosition;
		int oldThetaPosition = m_thetaPosition;
		m_rPosition += rSteps;
		m_thetaPosition += thetaSteps;
		System.err.printf("move(%d, %d) => %d, %d\n", rSteps, thetaSteps, m_rPosition, m_thetaPosition);
		if (isPenDown()) {
			if (rSteps != 0) {
				//System.err.printf("drawRay(%f, %f, %f\n", theta(oldThetaPosition), r(oldRPosition), r(m_rPosition));
				m_display.drawRay(theta(oldThetaPosition), r(oldRPosition), r(m_rPosition));
			}
			if (thetaSteps != 0) {
				//System.err.printf("drawArc(%f, %f, %f\n", r(m_rPosition), theta(oldThetaPosition), theta(m_thetaPosition));
				m_display.drawArc(r(m_rPosition), theta(m_thetaPosition), thetaSteps*radiansPerStep());
			}
		}
		
	}

	@Override
	public void finished() {
		m_display.goToZero();
	}


	@Override
	public void moveTo(Point p) {
		m_rPosition = (int) (p.r()/millimetersPerStep());
		m_thetaPosition = (int) (p.theta()/radiansPerStep());
	}


	public void followPen() {
		m_display.followPen();
	}


	public void scale(double scale) {
		m_display.scale(scale);
	}

}
