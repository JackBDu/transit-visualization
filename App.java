/*
 * Board.java
 *
 * Created on April 14, 2015
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
	public App() {
		this.setBackground(new Color(230, 230, 230));				// set background color to white
		this.setFocusable(true);
	}

	// paint the whole board
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	} // paint() ends

	// update the status
	public void update() {
	} // update() ends
	
	// main function for the board
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("New York Subway");
		App app = new App();

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
			Thread.sleep(1);
		}
	} // main() ends
} // App ends
