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
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

/*
 * @ version 0.0.1
 * @ author Jack B. Du (Jiadong Du)
 */

// main app of the project
public class App extends JPanel {
	ArrayList<Map<String, String>> shapes;// stores the shapes of the transit lines
	Color[] colors = new Color[14];// stores the color for the lines and subways
	long time = 0;// stores the simulated time
	ArrayList<Trajectory> trajectories; // stores the trajectories
	String day = "SUN";// stores the day as String
	boolean isPaused = false;// stores whether or not the app is paused
	boolean isHiden = true;// stores whether or not the lines are hidden
	int sleepTime = 6;// stores the sleep time for thread, adjusting the speed
	BufferedImage img;// stores the background`
	String[] routeIds;// stores all the routes that we have
	int currentRoute = 0;// stores the route that is being viewed
	boolean singleRouteMode = false;// stores whether or not the app is in single route mode

	// the constructor of the App class
	public App(ArrayList<Map<String, String>> shapes) {
		this.setBackground(new Color(0, 0, 0));				// set background color to white
		this.setFocusable(true);
		this.shapes = shapes;
		// assign values for the colors
		this.colors[0] = new Color(100,100,100);
		this.colors[1] = new Color(0,0,100);
		this.colors[2] = new Color(0,100,0);
		this.colors[3] = new Color(0,100,100);
		this.colors[4] = new Color(100,0,0);
		this.colors[5] = new Color(100,0,100);
		this.colors[6] = new Color(100,100,0);
		this.colors[7] = new Color(150,150,150);
		this.colors[8] = new Color(0,0,150);
		this.colors[9] = new Color(0,150,0);
		this.colors[10] = new Color(0,150,150);
		this.colors[11] = new Color(150,0,0);
		this.colors[12] = new Color(150,0,150);
		this.colors[13] = new Color(150,150,0);
	}

	// loads the background image
	public void loadImage() throws Exception {
		this.img = ImageIO.read(new File("data/map.png"));
	}
	
	// parse all the All the routes from the file
	public void parseRoutes() throws Exception {
		ArrayList<Map<String, String>> routes = GTFSParser.readCSV("data/mta-new-york-city-transit_20150404_2233/routes.csv");
		System.out.println(routes.size());
		this.routeIds = new String[routes.size()];
		for (int i = 0; i < routes.size(); i++) {
			this.routeIds[i] = routes.get(i).get("route_id");
		}
	}

	// switches to the previous route
	public void prevRoute() {
		if (this.currentRoute == 0) {
			this.currentRoute = this.routeIds.length - 1;
		} else {
			this.currentRoute--;
		}
	}

	// switches to the next route
	public void nextRoute() {
		if (this.currentRoute == this.routeIds.length - 1) {
			this.currentRoute = 0;
		} else {
			this.currentRoute++;
		}	
	}

	// toggles the single route mode
	public void allRoutes() {
		this.singleRouteMode = !this.singleRouteMode;
	}

	// assigns the parsed trajectories to the App class
	public void setTrajectories(ArrayList<Trajectory> trajectories) {
		this.trajectories = trajectories;
	}

	// handles the pause and play
	public void handlePause() {
		this.isPaused = !this.isPaused;
	}

	// handles whether or not to hide the route lines
	public void handleHide() {
		this.isHiden = !this.isHiden;
	}

	// set the day displayed
	public void setDay(String day) {
		this.day = day;
	}

	// paints the whole board
	public void paint(Graphics g) {
		int colorN = 0;
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(img, 0, 0, null);
		// draw the lines
		if (!this.isHiden) {
			g.setColor(this.colors[colorN]);
			for(int i = 1; i < this.shapes.size(); i++) {
				if (Integer.parseInt(this.shapes.get(i).get("shape_pt_sequence")) != 0) {
					double startLat = Double.parseDouble(this.shapes.get(i-1).get("shape_pt_lat"));
					double startLon = Double.parseDouble(this.shapes.get(i-1).get("shape_pt_lon"));
					double endLat = Double.parseDouble(this.shapes.get(i).get("shape_pt_lat"));
					double endLon = Double.parseDouble(this.shapes.get(i).get("shape_pt_lon"));
					Double y1 = 600 - (startLat - 40.45) * 1200;
					Double y2 = 600 - (endLat - 40.45) * 1200;
					Double x1 = (startLon + 74.3) * 1200;
					Double x2 = (endLon + 74.3) * 1200;
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
		}
		colorN = 0;
		Color dotColor = new Color(this.colors[colorN].getRed()+105, this.colors[colorN].getGreen()+105, this.colors[colorN].getBlue()+105);
		g.setColor(dotColor);
		
		// draws the trajectories
		for (Trajectory trajectory : this.trajectories) {
			//System.out.println(trajectory.getPosition(this.time));
			if (this.day.equals(trajectory.getServiceId().substring(9, 12))) {
				if (!this.singleRouteMode || this.routeIds[this.currentRoute].equals(trajectory.getRouteId())) {
					Coordinate screenCord = formatCord(trajectory.getPosition(this.time));
					//System.out.println(this.time);
					Double y = screenCord.getLat();
					Double x = screenCord.getLon();
					g.fillOval(x.intValue(), y.intValue(), 5, 5);
				}
			}
			if (colorN == this.colors.length - 1) {
				colorN = 0;
			} else {
				colorN++;
			}
			dotColor = new Color(this.colors[colorN].getRed()+105, this.colors[colorN].getGreen()+105, this.colors[colorN].getBlue()+105);
			g.setColor(dotColor);
		}
	} // paint() ends

	// format the lat, lon to x and y
	public Coordinate formatCord(Coordinate cord) {
		Double lon = (cord.getLon() + 74.3) * 1200;
		Double lat = 600 - (cord.getLat() - 40.45) * 1200;
		return new Coordinate(lat, lon);
	}

	// set the simluated time
	public void setTime(long time) {
		this.time = time;
	}

	// set the speed of the simulation
	public void setSpeed(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	// get the String of time to display
	public String getTimeString() {
		String hourStr, minStr, secStr;
		String routeId = this.routeIds[this.currentRoute];
		int hour = (int) this.time/3600;
		hourStr = hour<10?"0"+hour:""+hour;
		int  min = (int) (this.time - hour*3600)/60;
		minStr = min<10?"0"+min:""+min;
		int sec = (int) (this.time - hour*3600 - min*60);
		secStr = sec<10?"0"+sec:""+sec;
		return this.day + " " + hourStr + ":" + minStr + ":" + secStr + "  " + "Route " + routeId;
	}

	// update the status
	public void update() {
		if (!this.isPaused) {
			if (this.time < 86400) {
				this.time += 2;
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
		// creates the components
		JFrame frame = new JFrame("New York Subway Visualizatoin");
		frame.setLayout(new BorderLayout());
		Box sideBar = new Box(BoxLayout.Y_AXIS);
		JButton pbtn = new JButton("Play/Pause");
		JButton sunBtn = new JButton("Sunday");
		JButton satBtn = new JButton("Saturday");
		JButton wkdBtn = new JButton("Workday");
		JButton hideBtn = new JButton("Show/Hide");
		JButton prevBtn = new JButton("Prev Route");
		JButton nextBtn = new JButton("Next Route");
		JButton allBtn = new JButton("All/Single");
		JLabel timeLabel = new JLabel();
		JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, 0, 86400);
		JScrollBar speedBar = new JScrollBar(JScrollBar.HORIZONTAL, 5, 1, 1, 10);
		// set the sizes
		Dimension d = new Dimension(100,50);
		pbtn.setSize(d);
		sunBtn.setSize(d);
		satBtn.setSize(d);
		wkdBtn.setSize(d);
		hideBtn.setSize(d);

		final App app = new App(shapes);

		// ActionListenrs
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
		hideBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.handleHide();
			}
		});
		prevBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.prevRoute();
			}
		});
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.nextRoute();
			}
		});
		allBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.allRoutes();
			}
		});
		scrollBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				app.setTime(e.getValue());
			}
		});
		speedBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				app.setSpeed(11 - e.getValue());
			}
		});

		// add components to sideBar
		sideBar.add(speedBar);
		sideBar.add(pbtn);
		sideBar.add(sunBtn);
		sideBar.add(satBtn);
		sideBar.add(wkdBtn);
		sideBar.add(hideBtn);
		sideBar.add(prevBtn);
		sideBar.add(nextBtn);
		sideBar.add(allBtn);
		//sideBar.add(hourTF);
		//sideBar.add(minTF);
		//sideBar.add(secTF);
		app.setSize(600, 600);
		app.loadImage();
		app.parseRoutes();
		app.setTrajectories(trajectories);
		// initialize the frame
		frame.setSize(800, 600);
		frame.add(app, BorderLayout.CENTER);
		frame.add(sideBar, BorderLayout.EAST);
		frame.add(scrollBar, BorderLayout.SOUTH);
		frame.add(timeLabel, BorderLayout.NORTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setBackground(Color.BLACK);

		// the main loop of the whole animation
		while(true) {
			app.update();
			app.repaint();
			Thread.sleep(app.sleepTime);
			scrollBar.setValue((int)app.time);
			timeLabel.setText(app.getTimeString());
		}
		
	} // main() ends
} // App ends
