import com.brozowski.lego.plotter.functions.CenterStar;
import com.brozowski.lego.plotter.functions.Line;
import com.brozowski.lego.plotter.functions.Plottable;
import com.brozowski.lego.plotter.functions.Point;
import com.brozowski.lego.plotter.functions.PolarRose;
import com.brozowski.lego.plotter.functions.PolarRose2;
import com.brozowski.lego.plotter.functions.Spiral;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestPlotter plotter = new TestPlotter();
		//plotter.followPen();
		//plotter.scale(2);
		Plottable plot = getPlottable(plotter);
		
		plot.plot();
		
		plotter.penUp();
		//plotter.move(0, 0);

	}

	private static Plottable getPlottable(TestPlotter plotter) {
		//return new PolarRose2(plotter);
		//return new Spiral(plotter);
		//return new CenterStar(plotter);
		return new Line(plotter, Point.cartesion(20, 20), Point.cartesion(-20, -25));
		//return new PolarRose(plotter);
	}

}
