package com.brozowski.lego.plotter.functions;

import java.io.BufferedReader;
import java.io.File;

public class GCodeInterpreter implements Plottable {
	
	private PolarPlotter m_plotter;

	public GCodeInterpreter(PolarPlotter plotter) {
		m_plotter = plotter;
	}
	
	public void parse(File gCodeFile) {
		
	}

	@Override
	public void plot() {
		
		
	}

}
