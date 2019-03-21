package imageProcessing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.HashMap;
import java.util.prefs.Preferences;

/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class IMP implements MouseListener {
	HashMap<Integer, Integer> redAmt = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> greenAmt = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> blueAmt = new HashMap<Integer, Integer>();

	MyPanel rPanel = null;
	MyPanel gPanel = null;
	MyPanel bPanel = null;

	public int rgb[] = new int[4];

	JFrame frame;
	JPanel mp;
	JButton start;
	JScrollPane scroll;
	JMenuItem openItem, exitItem, resetItem;
	Toolkit toolkit;
	File pic;
	ImageIcon img;
	int colorX, colorY;
	int[] pixels;
	int[] results;

	// Instance Fields you will be using below

	// This will be your height and width of your 2d array
	int height = 0, width = 0;

	// your 2D array of pixels
	int picture[][];
	int firstPicture[][];

	/*
	 * In the Constructor I set up the GUI, the frame the menus. The open
	 * pulldown menu is how you will open an image to manipulate.
	 */
	IMP() {
		toolkit = Toolkit.getDefaultToolkit();
		frame = new JFrame("Image Processing Software by Hunter");
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu functions = getFunctions();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				quit();
			}
		});
		openItem = new JMenuItem("Open");
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				handleOpen();
			}
		});
		resetItem = new JMenuItem("Reset");
		resetItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					reset();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				quit();
			}
		});
		file.add(openItem);
		file.add(resetItem);
		file.add(exitItem);
		bar.add(file);
		bar.add(functions);
		frame.setSize(600, 600);
		mp = new JPanel();
		mp.setBackground(new Color(0, 0, 0));
		scroll = new JScrollPane(mp);
		frame.getContentPane().add(scroll, BorderLayout.CENTER);
		JPanel butPanel = new JPanel();
		butPanel.setBackground(Color.black);
		start = new JButton("start");
		start.setEnabled(false);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun1();
			}
		});
		butPanel.add(start);
		frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
		frame.setJMenuBar(bar);
		frame.setVisible(true);
		frame.setLocation(175, 75);
	}

	/*
	 * This method creates the pulldown menu and sets up listeners to selection
	 * of the menu choices. If the listeners are activated they call the methods
	 * for handling the choice, fun1, fun2, fun3, fun4, etc. etc.
	 */

	private JMenu getFunctions() {
		JMenu fun = new JMenu("Functions");

		JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
		JMenuItem secondItem = new JMenuItem("Rotate");
		JMenuItem thirdItem = new JMenuItem("Grayscale");
		JMenuItem fourthItem = new JMenuItem("Blur");
		JMenuItem fifthItem = new JMenuItem("Histogram");
		JMenuItem sixthItem = new JMenuItem("Equalize");
		JMenuItem seventhItem = new JMenuItem("Track");

		firstItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun1();
			}

		});

		secondItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun2();
			}

		});

		thirdItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun3();
			}
		});

		fourthItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun4();
			}
		});
		fifthItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun5();
			}

		});
		sixthItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun6();
			}

		});
		seventhItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				fun7();
			}

		});

		fun.add(firstItem);
		fun.add(secondItem);
		fun.add(thirdItem);
		fun.add(fourthItem);
		fun.add(fifthItem);
		fun.add(sixthItem);
		fun.add(seventhItem);
		return fun;

	}

	/*
	 * This method handles opening an image file, breaking down the picture to a
	 * one-dimensional array and then drawing the image on the frame. You don't
	 * need to worry about this method.
	 */
	private void handleOpen() {
		img = new ImageIcon();
		JFileChooser chooser = new JFileChooser();
		Preferences pref = Preferences.userNodeForPackage(IMP.class);
		String path = pref.get("DEFAULT_PATH", "");

		chooser.setCurrentDirectory(new File(path));
		int option = chooser.showOpenDialog(frame);

		if (option == JFileChooser.APPROVE_OPTION) {
			pic = chooser.getSelectedFile();
			pref.put("DEFAULT_PATH", pic.getAbsolutePath());
			img = new ImageIcon(pic.getPath());
		}
		width = img.getIconWidth();
		height = img.getIconHeight();

		JLabel label = new JLabel(img);
		label.addMouseListener(this);
		pixels = new int[width * height];

		results = new int[width * height];

		Image image = img.getImage();

		PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			System.err.println("Interrupted waiting for pixels");
			return;
		}
		for (int i = 0; i < width * height; i++)
			results[i] = pixels[i];
		turnTwoDimensional();
		mp.removeAll();
		mp.add(label);

		mp.revalidate();
	}

	/*
	 * The libraries in Java give a one dimensional array of RGB values for an
	 * image, I thought a 2-Dimensional array would be more usefull to you So
	 * this method changes the one dimensional array to a two-dimensional.
	 */
	private void turnTwoDimensional() {
		picture = new int[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				picture[i][j] = pixels[i * width + j];
		firstPicture = picture;

	}

	/*
	 * This method takes the picture back to the original picture
	 */
	private void reset() throws InterruptedException {
		if (img != null) {
			JLabel newLabel = new JLabel(img);
			newLabel.addMouseListener(this);
			Image img2 = img.getImage();
			PixelGrabber pg = new PixelGrabber(img2, 0, 0, width, height, pixels, 0, width);

			pg.grabPixels();

			for (int i = 0; i < width * height; i++) {
				results[i] = pixels[i];
			}
			turnTwoDimensional();
			mp.removeAll();
			mp.repaint();
			mp.add(newLabel);
		}
		mp.revalidate();
	}

	/*
	 * This method is called to redraw the screen with the new image.
	 */
	private void resetPicture(int height, int width, int[][] img) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				pixels[i * width + j] = img[i][j];
			}
		}
		Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));
		JLabel label2 = new JLabel(new ImageIcon(img2));
		mp.removeAll();
		mp.repaint();
		mp.add(label2);
		mp.revalidate();
	}

	/*
	 * This method takes a single integer value and breaks it down doing bit
	 * manipulation to 4 individual int values for A, R, G, and B values
	 */
	private int[] getPixelArray(int pixel) {
		int temp[] = new int[4];
		temp[0] = (pixel >> 24) & 0xff;
		temp[1] = (pixel >> 16) & 0xff;
		temp[2] = (pixel >> 8) & 0xff;
		temp[3] = (pixel) & 0xff;
		return temp;

	}

	/*
	 * This method takes an array of size 4 and combines the first 8 bits of
	 * each to create one integer.
	 */
	private int getPixels(int rgb[]) {
		int alpha = 0;
		int rgba = (rgb[0] << 24) | (rgb[1] << 16) | (rgb[2] << 8) | rgb[3];
		return rgba;
	}

	public void getValue() {
		int pix = picture[colorY][colorX];
		int temp[] = getPixelArray(pix);
		System.out.println("Color value " + temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);
	}

	/**************************************************************************************************
	 * This is where you will put your methods. Every method below is called
	 * when the corresponding pulldown menu is used. As long as you have a
	 * picture open first the when your fun1, fun2, fun....etc method is called
	 * you will have a 2D array called picture that is holding each pixel from
	 * your picture.
	 *************************************************************************************************/
	/*
	 * Example function that just removes all red values from the picture. Each
	 * pixel value in picture[i][j] holds an integer value. You need to send
	 * that pixel to getPixelArray the method which will return a 4 element
	 * array that holds A,R,G,B values. Ignore [0], that's the Alpha channel
	 * which is transparency, we won't be using that, but you can on your own.
	 * getPixelArray will breaks down your single int to 4 ints so you can
	 * manipulate the values for each level of R, G, B. After you make changes
	 * and do your calculations to your pixel values the getPixels method will
	 * put the 4 values in your ARGB array back into a single integer value so
	 * you can give it back to the program and display the new picture.
	 */
	private void fun1() {

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				int rgb[] = new int[4];

				// get three ints for R, G and B
				rgb = getPixelArray(picture[i][j]);

				rgb[1] = 0;
				// take three ints for R, G, B and put them back into a single
				// int
				picture[i][j] = getPixels(rgb);
			}
		resetPicture(height, width, picture);
	}

	// Function to rotate the picture 90 degrees.
	private void fun2() {
		int[][] newPicture = new int[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb = getPixelArray(picture[i][j]);
				newPicture[j][i] = getPixels(rgb);
			}
		}
		resetPicture(width, height, newPicture);

	}

	// Function to transform image into gray scale.
	private void fun3() {
		int[][] newPicture = new int[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb = getPixelArray(picture[i][j]);
				rgb[0] = 255;
				// Red:
				rgb[1] = (rgb[1] + rgb[2] + rgb[3]) / 3;
				rgb[2] = (rgb[1] + rgb[2] + rgb[3]) / 3;
				rgb[3] = (rgb[1] + rgb[2] + rgb[3]) / 3;
				// rgb[1] = (int) ((rgb[1] * 0.21) + (rgb[2] * 0.72) + (rgb[3] *
				// 0.07));
				// // Green:
				// rgb[2] = (int) ((rgb[1] * 0.21) + (rgb[2] * 0.72) + (rgb[3] *
				// 0.07));
				// // Blue:
				// rgb[3] = (int) ((rgb[1] * 0.21) + (rgb[2] * 0.72) + (rgb[3] *
				// 0.07));
				picture[i][j] = getPixels(rgb);
			}
		}
		resetPicture(height, width, picture);
	}

	// Function to blur
	private void fun4() {
		int[][] newPicture = picture;
		for (int i = 2; i < height - 2; i++) {
			for (int j = 2; j < width - 2; j++) {
				int[] topA = getPixelArray(picture[i - 2][j - 2]);
				int[] topB = getPixelArray(picture[i - 2][j - 1]);
				int[] topC = getPixelArray(picture[i - 2][j]);
				int[] topD = getPixelArray(picture[i - 2][j + 1]);
				int[] topE = getPixelArray(picture[i - 2][j + 2]);
				int[] botA = getPixelArray(picture[i + 2][j - 2]);
				int[] botB = getPixelArray(picture[i + 2][j - 1]);
				int[] botC = getPixelArray(picture[i + 2][j]);
				int[] botD = getPixelArray(picture[i + 2][j + 1]);
				int[] botE = getPixelArray(picture[i + 2][j + 2]);
				int[] leftA = getPixelArray(picture[i - 1][j - 2]);
				int[] leftB = getPixelArray(picture[i][j - 2]);
				int[] leftC = getPixelArray(picture[i + 1][j - 2]);
				int[] rightA = getPixelArray(picture[i - 1][j + 2]);
				int[] rightB = getPixelArray(picture[i][j + 2]);
				int[] rightC = getPixelArray(picture[i + 1][j + 2]);

				int redAvg = (topA[1] + topB[1] + topC[1] + topD[1] + topE[1] + leftA[1] + leftB[1] + leftC[1]
						+ rightA[1] + rightB[1] + rightC[1] + botA[1] + botB[1] + botC[1] + botD[1] + botE[1]) / 16;

				int greenAvg = (topA[2] + topB[2] + topC[2] + topD[2] + topE[2] + leftA[2] + leftB[2] + leftC[2]
						+ rightA[2] + rightB[2] + rightC[2] + botA[2] + botB[2] + botC[2] + botD[2] + botE[2]) / 16;

				int blueAvg = (topA[3] + topB[3] + topC[3] + topD[3] + topE[3] + leftA[3] + leftB[3] + leftC[3]
						+ rightA[3] + rightB[3] + rightC[3] + botA[3] + botB[3] + botC[3] + botD[3] + botE[3]) / 16;

				rgb[0] = 255;
				rgb[1] = redAvg;
				rgb[2] = greenAvg;
				rgb[3] = blueAvg;
				newPicture[i][j] = getPixels(rgb);
			}
		}
		resetPicture(height, width, newPicture);
	}

	// Function to draw histo's
	private void fun5() {
		int[][] newPicture = new int[width][height];
		for (int i = 0; i <= 255; i++) {
			redAmt.put(i, 0);
			greenAmt.put(i, 0);
			blueAmt.put(i, 0);
		}

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb = getPixelArray(picture[i][j]);
				redAmt.put(rgb[1], redAmt.get(rgb[1]) + 1);
				greenAmt.put(rgb[2], greenAmt.get(rgb[2]) + 1);
				blueAmt.put(rgb[3], blueAmt.get(rgb[3]) + 1);
			}
		}

		rPanel = new MyPanel("red", redAmt);
		gPanel = new MyPanel("green", greenAmt);
		bPanel = new MyPanel("blue", blueAmt);

		rPanel.drawHisto(start, rPanel);
		gPanel.drawHisto(start, gPanel);
		bPanel.drawHisto(start, bPanel);
	}

	// Function to equalize, histo option must be chosen already to execute.
	private void fun6() {
		int[][] newPicture = new int[width][height];
		if (rPanel == null || gPanel == null || bPanel == null) {
			JOptionPane.showMessageDialog(mp, "Display histo before equalizing.");
			return;
		}

		double pixelCount = width * height;

		double redCount = 0;
		double greenCount = 0;
		double blueCount = 0;

		double[] redAvg = new double[256];
		double[] greenAvg = new double[256];
		double[] blueAvg = new double[256];

		for (int i = 0; i < 256; i++) {
			redCount += redAmt.get(i);
			greenCount += greenAmt.get(i);
			blueCount += blueAmt.get(i);

			redAvg[i] = (redCount / pixelCount) * 255;
			greenAvg[i] = (greenCount / pixelCount) * 255;
			blueAvg[i] = (blueCount / pixelCount) * 255;
		}

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb = getPixelArray(picture[i][j]);

				rgb[0] = 255;
				rgb[1] = (int) redAvg[rgb[1]];
				rgb[2] = (int) greenAvg[rgb[2]];
				rgb[3] = (int) blueAvg[rgb[3]];
				picture[i][j] = getPixels(rgb);
			}
		}
		resetPicture(height, width, picture);
	}

	// Fuction to track an object
	private void fun7() {
		int[][] newPicture = new int[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb = getPixelArray(picture[i][j]);
				if ((rgb[1] > 215 && rgb[1] < 255) && (rgb[2] > 135 && rgb[2] < 255) && (rgb[3] > 0 && rgb[3] < 255)) {
					rgb[0] = 255;
					rgb[1] = 255;
					rgb[2] = 255;
					rgb[3] = 255;
				} else {
					rgb[0] = 255;
					rgb[1] = 0;
					rgb[2] = 0;
					rgb[3] = 0;
				}
				picture[i][j] = getPixels(rgb);
			}
		}
		resetPicture(height, width, picture);
	}

	private void quit() {
		System.exit(0);
	}

	@Override
	public void mouseEntered(MouseEvent m) {
	}

	@Override
	public void mouseExited(MouseEvent m) {
	}

	@Override
	public void mouseClicked(MouseEvent m) {
		colorX = m.getX();
		colorY = m.getY();
		System.out.println(colorX + "  " + colorY);
		getValue();
		start.setEnabled(true);
	}

	@Override
	public void mousePressed(MouseEvent m) {
	}

	@Override
	public void mouseReleased(MouseEvent m) {
	}

	public static void main(String[] args) {
		IMP imp = new IMP();
	}

}