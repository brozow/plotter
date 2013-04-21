package com.brozowski.lego.plotter.functions;

public class PlotBuilder {
	
	private static class PolyPlottable implements Plottable {
		
		

		@Override
		public void plot() {
			throw new UnsupportedOperationException("Plottable.plot is not yet implemented.");
		}
		
	}
	
	public PlotBuilder() {
		
	}
	
	Plottable get() {
		return null;
	}

}
