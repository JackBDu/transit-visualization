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
	Color[] colors = new Color[7];
	long time = 27000;
	ArrayList<Trajectory> trajectories; 
	public App(ArrayList<Map<String, String>> shapes) {
		this.setBackground(new Color(230, 230, 230));				// set background color to white
		this.setFocusable(true);
		this.shapes = shapes;
		this.colors[0] = new Color(0,0,0);
		this.colors[1] = new Color(0,0,255);
		this.colors[2] = new Color(0,255,0);
		this.colors[3] = new Color(0,255,255);
		this.colors[4] = new Color(255,0,0);
		this.colors[5] = new Color(255,0,255);
		this.colors[6] = new Color(255,255,0);
	}

	public void setTrajectories(ArrayList<Trajectory> trajectories) {
		this.trajectories = trajectories;
	}

	// paint the whole board
	public void paint(Graphics g) {
		int colorN = 0;
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(this.colors[colorN]);
		for(int i = 1; i < this.shapes.size(); i++) {
			if (Integer.parseInt(this.shapes.get(i).get("shape_pt_sequence")) != 0) {
				double startLat = Double.parseDouble(this.shapes.get(i-1).get("shape_pt_lat"));
				double startLon = Double.parseDouble(this.shapes.get(i-1).get("shape_pt_lon"));
				double endLat = Double.parseDouble(this.shapes.get(i).get("shape_pt_lat"));
				double endLon = Double.parseDouble(this.shapes.get(i).get("shape_pt_lon"));
				Double y1 = 600 - (startLat - 40.4) * 1000;
				Double y2 = 600 - (endLat - 40.4) * 1000;
				Double x1 = (startLon + 74.4) * 1000;
				Double x2 = (endLon + 74.4) * 1000;
				g.drawLine(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
			} else {
				if (colorN == this.colors.length - 1) {
					colorN = 0;
				} else {
					colorN++;
				}
				g.setColor(this.colors[colorN]);
			}
		}

		g.setColor(Color.BLACK);
		System.out.println(this.trajectories.get(0).getPosition(this.time));
		for (Trajectory trajectory : this.trajectories) {
			//System.out.println(trajectory.getPosition(this.time));
			Coordinate screenCord = formatCord(trajectory.getPosition(this.time));
			//System.out.println(this.time);
			Double y = screenCord.getLat();
			Double x = screenCord.getLon();
			g.fillOval(x.intValue(), y.intValue(), 2, 2);
		}
	} // paint() ends

	public Coordinate formatCord(Coordinate cord) {
		Double lon = (cord.getLon() + 74.4) * 1000;
		Double lat = 600 - (cord.getLat() - 40.4) * 1000;
		return new Coordinate(lat, lon);
	}

	// update the status
	public void update() {
		if (this.time < 86400) {
			this.time += 1;
		} else {
			this.time = 0;
		}
	} // update() ends
	
	// main function for the board
	public static void main(String[] args) throws Exception {
		ArrayList<Map<String, String>> shapes = GTFSParser.readCSV("data/mta-new-york-city-transit_20150404_2233/shapes.csv");
		ArrayList<Trajectory> trajectories = GTFSParser.parseTrips("data/mta-new-york-city-transit_20150404_2233");
		JFrame frame = new JFrame("New York Subway");
		App app = new App(shapes);
		app.setTrajectories(trajectories);
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
			Thread.sleep(10);
		}
		
	} // main() ends
} // App ends
