/*
 * App.java
 *
 * Created on May 7, 2015
 *
 * Copyright(c) {2015} Jack B. Du (Jiadong Du) All Rights Reserved.
 *
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*
 * @ version 0.0.1
 * @ author Jack B. Du (Jiadong Du)
 */

public class App extends JPanel {
	ArrayList<Map<String, String>> shapes;
	public App(ArrayList<Map<String, String>> shapes) {
		this.setBackground(new Color(230, 230, 230));				// set background color to white
		this.setFocusable(true);
		this.shapes = shapes;
	}

	// paint the whole board
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
		for(int i = 1; i < this.shapes.size(); i++) {
			if (Integer.parseInt(this.shapes.get(i).get("shape_pt_sequence")) != 0) {
				double startLat = Double.parseDouble(this.shapes.get(i-1).get("shape_pt_lat"));
				double startLon = Double.parseDouble(this.shapes.get(i-1).get("shape_pt_lon"));
				double endLat = Double.parseDouble(this.shapes.get(i).get("shape_pt_lat"));
				double endLon = Double.parseDouble(this.shapes.get(i).get("shape_pt_lon"));
				Double x1 = (startLat - 40.3) * 1000;
				Double x2 = (endLat - 40.3) * 1000;
				Double y1 = 600 - (startLon + 74.3) * 1000;
				Double y2 = 600 - (endLon + 74.3) * 1000;
				g.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
			}
		}
	} // paint() ends

	// update the status
	public void update() {
	} // update() ends
	
	// main function for the board
	public static void main(String[] args) throws Exception {
		ArrayList<Map<String, String>> shapes = GTFSParser.readCSV("data/mta-new-york-city-transit_20150404_2233/shapes.csv");
		JFrame frame = new JFrame("New York Subway");
		App app = new App(shapes);
		// initialize the frame
		frame.setSize(800, 600);
		frame.add(app);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		



		while(true) {
			app.update();
			app.repaint();
			Thread.sleep(1000);
		}
	} // main() ends
} // App ends
