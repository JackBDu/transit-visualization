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
	long time = 0;
	ArrayList<Trajectory> trajectories; 
	String day = "SUN";
	boolean isPaused = false;
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

	public void handlePause() {
		this.isPaused = !this.isPaused;
	}

	public void setDay(String day) {
		this.day = day;
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
		for (Trajectory trajectory : this.trajectories) {
			//System.out.println(trajectory.getPosition(this.time));
			if (this.day.equals(trajectory.getServiceId().substring(9, 12))) {
				Coordinate screenCord = formatCord(trajectory.getPosition(this.time));
				//System.out.println(this.time);
				Double y = screenCord.getLat();
				Double x = screenCord.getLon();
				g.fillOval(x.intValue(), y.intValue(), 3, 3);
			}
		}
	} // paint() ends

	public Coordinate formatCord(Coordinate cord) {
		Double lon = (cord.getLon() + 74.4) * 1000;
		Double lat = 600 - (cord.getLat() - 40.4) * 1000;
		return new Coordinate(lat, lon);
	}

	// update the status
	public void update() {
		if (!this.isPaused) {
			if (this.time < 86400) {
				this.time += 1;
			} else {
				this.time = 0;
			}
		}
	} // update() ends
	
	// main function for the board
	public static void main(String[] args) throws Exception {
		ArrayList<Map<String, String>> shapes = GTFSParser.readCSV("data/mta-new-york-city-transit_20150404_2233/shapes.csv");
		// parsing the trips
		ArrayList<Trajectory> trajectories = GTFSParser.parseTrips("data/mta-new-york-city-transit_20150404_2233");
		JFrame frame = new JFrame("New York Subway");
		frame.setLayout(new BorderLayout());
		Box sideBar = new Box(BoxLayout.Y_AXIS);
		JButton pbtn = new JButton("Play/Pause");
		JButton sunBtn = new JButton("Sunday");
		JButton satBtn = new JButton("Saturday");
		JButton wkdBtn = new JButton("Weekday");
		JTextField hourTF = new JTextField(3);
		JTextField minTF = new JTextField(3);
		JTextField secTF = new JTextField(3);
		JScrollBar scrollBar = new JScrollBar(0, 0, 1, 0, 86400);
		Dimension d = new Dimension(100,50);
		pbtn.setSize(d);
		sunBtn.setSize(d);
		satBtn.setSize(d);
		wkdBtn.setSize(d);
		hourTF.setSize(d);
		minTF.setSize(d);
		secTF.setSize(d);

		final App app = new App(shapes);
		pbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.handlePause();
			}
		});
		sunBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.setDay("SUN");
			}
		});
		satBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.setDay("SAT");
			}
		});
		wkdBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.setDay("WKD");
			}
		});
		sideBar.add(pbtn);
		sideBar.add(sunBtn);
		sideBar.add(satBtn);
		sideBar.add(wkdBtn);
		//sideBar.add(hourTF);
		//sideBar.add(minTF);
		//sideBar.add(secTF);
		app.setSize(600, 600);
		app.setTrajectories(trajectories);
		// initialize the frame
		frame.setSize(800, 600);
		frame.add(app, BorderLayout.CENTER);
		frame.add(sideBar, BorderLayout.EAST);
		frame.add(scrollBar, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setBackground(Color.WHITE);


		while(true) {
			app.update();
			app.repaint();
			Thread.sleep(10);
		}
		
	} // main() ends
} // App ends
