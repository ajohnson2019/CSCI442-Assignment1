package imageProcessing;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyPanel extends JPanel {

	HashMap<Integer, Integer> frequency;
	String color;

	int startX, flag, startY, endX, endY;

	BufferedImage grid;
	Graphics2D gc;

	public MyPanel(String color, HashMap<Integer, Integer> frequency) {
		startX = startY = 0;
		endX = endY = 100;
		this.color = color;
		this.frequency = frequency;
	}

	public void clear() {
		grid = null;
		repaint();
	}

	public void drawHisto(JButton start, MyPanel panel) {
		JFrame frame = new JFrame(color);

		frame.setSize(275, 500);

		if (color == "red") {
			frame.setLocation(0, 150);
		}
		if (color == "green") {
			frame.setLocation(775, 150);
		}
		if (color == "blue") {
			frame.setLocation(1050, 150);
		}

		frame.getContentPane().add(panel, BorderLayout.CENTER);

		frame.setVisible(true);

		start.setEnabled(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (grid == null) {
			int width = this.getWidth();
			int height = this.getHeight();
			grid = (BufferedImage) (this.createImage(width, height));
			gc = grid.createGraphics();

			for (int i = 0; i < frequency.size(); i++) {
				gc.drawLine(i + 25, 400, i + 25, 400 - (frequency.get(i) / 5));
			}

		}
		g2.drawImage(grid, null, 0, 0);
	}

	public void drawing() {
		gc.drawLine(startX, startY, endX, endY);
		repaint();
	}
}
