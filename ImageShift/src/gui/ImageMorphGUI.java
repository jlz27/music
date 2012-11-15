package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import algorithms.ThreshholdSegment;

public class ImageMorphGUI extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private static final String RESOURCE_PATH = "resources" + File.separator;

	public static void main(String[] args) {
		ImageMorphGUI gui = new ImageMorphGUI();
		javax.swing.SwingUtilities.invokeLater(gui);
	}
	
	private void createAndShowGUI() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(RESOURCE_PATH + "cornell.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ThreshholdSegment segment = new ThreshholdSegment(img);
		this.add(new ImageComponent(segment.morphPixels(0)));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void run() {
		createAndShowGUI();
	}

}
