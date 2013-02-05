import com.brozowski.lego.plotter.functions.Line;
import com.brozowski.lego.plotter.functions.Point;
import com.brozowski.lego.plotter.functions.PolarRose;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestPlotDisplay display = new TestPlotDisplay();
		//display.followPen();
		TestPlotter plotter = new TestPlotter(display);
		PolarRose plot = new PolarRose(plotter);
		//Line plot = new Line(plotter, Point.cartesion(20, 20), Point.cartesion(-20, -25));
		
		plot.plot();
		
		plotter.penUp();
		//plotter.move(0, 0);

	}

}
