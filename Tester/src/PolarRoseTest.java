import static org.junit.Assert.*;

import org.junit.Test;


public class PolarRoseTest {

	@Test
	public void test() {
		PolarRose polarRose = new PolarRose();
		polarRose.plot(0, 2*Math.PI);
	}

}
