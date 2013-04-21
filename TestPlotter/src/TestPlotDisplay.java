/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestPlotDisplay {
	

	private static final double PIXELS_PER_MM = 5.2;
	private static final double NOTE_WIDTH = 77.0;
	private static final Color NOTE_COLOR = Color.yellow;
	private static final double SCALE = 1;
	
	public static void main(String[] args) {
		TestPlotDisplay display = new TestPlotDisplay();
		display.drawRay(0, 0, 40);
		display.drawArc(40, 0, Math.PI);
	    display.drawRay(Math.PI, 40, 25);
	}

	private JPanel m_panel;
	private double m_rotation = 0;
	private double m_position = 0;
	private boolean m_followPen = false;
	private Shape m_note;
	private Path2D m_path = new Path2D.Double();
	private BlockingQueue<Shape> m_elements = new LinkedBlockingQueue<Shape>();
	private List<Shape> m_pathList = new ArrayList<Shape>();
	private double m_scale = 1.0;
	
	public TestPlotDisplay() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				init();
			}
			
		});
	}
	
	public void init() {
		System.out.println("Created GUI on EDT? "+SwingUtilities.isEventDispatchThread());
		m_note = new Rectangle2D.Double(mm(-NOTE_WIDTH/2), mm(-NOTE_WIDTH/2), mm(NOTE_WIDTH), mm(NOTE_WIDTH));
		JFrame f = new JFrame("LEGO Plotter Simulation");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		f.setLayout(new BorderLayout());
		m_panel = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				Graphics2D g = (Graphics2D) graphics;

				AffineTransform transform = g.getTransform();

				double penOffset = 0.0;
				if (m_followPen) {
					penOffset = m_position;
				}
				g.translate(m_panel.getWidth()/2-penOffset, m_panel.getHeight()/2);
				g.rotate(m_rotation);
				
				paintNote(g);
				paintPlot(g);

				g.setTransform(transform);

				paintRAxis(g);

				g.translate(m_panel.getWidth()/2-penOffset, m_panel.getHeight()/2);
				paintPen(g);
			}

			private void paintPen(Graphics2D g) {
				g.setColor(Color.red);
				g.drawRect((int)(m_position-3), -3, 6, 6);
			}

			private void paintRAxis(Graphics2D g) {
				g.setColor(Color.blue);
				g.drawLine(0, m_panel.getHeight()/2, m_panel.getWidth(), m_panel.getHeight()/2);
			}

			private void paintPlot(Graphics2D g) {
				g.setColor(Color.black);

				int stepSize = Integer.MAX_VALUE;
				int initial = m_pathList.size();
				m_elements.drainTo(m_pathList, stepSize);
								
				for(Shape element : m_pathList) {
					g.draw(element);
				}

				if (m_pathList.size() - initial == stepSize) {
					repaint(50);
				}
					//g.draw(m_path);

			}

			private void paintNote(Graphics2D g) {
				g.setColor(NOTE_COLOR);
				g.fill(m_note);
			}
			
		};
		m_panel.setPreferredSize(new Dimension(Math.min(800, (int)mm(2*NOTE_WIDTH)), Math.min(800,(int)mm(2*NOTE_WIDTH))));
		//m_panel.setBackground(Color.red);
		f.add(m_panel, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
	}

	public void drawRay(double theta, double r1, double r2) {
		double cos = Math.cos(-theta);
		double sin = Math.sin(-theta);
        //System.err.printf("Line2D.Double(%f, %f, %f, %f\n", mm(r1*cos), mm(r1*sin), mm(r2*cos), mm(r2*sin));
		Shape element = new Line2D.Double(mm(r1*cos), mm(r1*sin), mm(r2*cos), mm(r2*sin));
		
		m_rotation = theta;
		m_position= mm(r2);

		m_elements.offer(element);
		
		repaint();
		sleep();
	}

	private void repaint() {
		if (m_panel != null) {
			m_panel.repaint(50);
		}
	}
	
	private void sleep() {
		sleep(1);
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.yield();
	}

	public void drawArc(double r, double theta1, double extentInRadians) {
		double angle1 = Math.toDegrees(-theta1);
		double extent = Math.toDegrees(-extentInRadians);
		m_rotation = (theta1+extentInRadians);
		m_position = mm(r);
		
		double radius = Math.abs(mm(r));
		double x = -radius;
		double y = -radius;
		double width = 2*radius;
		double height = 2*radius;
		double offset = r < 0 ? 180.0 : 0.0;
		//System.err.printf("Arc2D.Double(%f, %f, %f, %f, %f, %g, %d \n",x, y, width, height, -angle1+offset, extent, Arc2D.OPEN);
		
		Shape element = new Arc2D.Double(x, y, width, height, -angle1+offset, extent, Arc2D.OPEN);
		
		m_elements.offer(element);
		
		repaint();
		sleep();
	}
	
	private double mm(double mm) {
		return (mm*PIXELS_PER_MM*scale());
	}
	
	private double cm(double cm) {
		return (cm*10*PIXELS_PER_MM*scale());
	}

	private double scale() {
		return m_scale;
	}

	public void followPen() {
		m_followPen  = true;
	} 
	
	public void goToZero() {
		
		double ROT_DECR = 0.01;
		double POS_DECR = 0.75;

		while(Math.abs(m_rotation) > ROT_DECR || Math.abs(m_position) > POS_DECR*scale()) {
			double rotationDecr = ROT_DECR*Math.signum(m_rotation);
			rotationDecr = Math.abs(rotationDecr) < Math.abs(m_rotation) ? rotationDecr : m_rotation;
			m_rotation = m_rotation - rotationDecr;
			double positionDecr = POS_DECR*scale()*Math.signum(m_position);
			positionDecr = Math.abs(positionDecr) < Math.abs(m_position) ? positionDecr : m_position;
			m_position = m_position - positionDecr;
			repaint();
			sleep(100);
		}
		
		m_rotation = 0.0;
		m_position = 0.0;
		repaint();
		sleep();
	}

	public void scale(double scale) {
		m_scale = scale;
	}

}

