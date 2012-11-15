package gui;

import java.io.File;
import javax.swing.JFrame;

public class ImageMorphGUI extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private static final String RESOURCE_PATH = "resources" + File.separator;

	public static void main(String[] args) {
		ImageMorphGUI gui = new ImageMorphGUI();
		javax.swing.SwingUtilities.invokeLater(gui);
	}
	
	private void createAndShowGUI() {
		this.add(new ImageComponent(RESOURCE_PATH + "cat.jpg"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void run() {
		createAndShowGUI();
	}

}
