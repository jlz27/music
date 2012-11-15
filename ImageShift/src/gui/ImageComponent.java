package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ImageComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_DIMENSION = 100;
	
	BufferedImage img;
	
	public ImageComponent(BufferedImage img) {
		this.img = img;
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (this.img == null) {
			return new Dimension(DEFAULT_DIMENSION, DEFAULT_DIMENSION);
		} else {
			return new Dimension(this.img.getWidth(null), this.img.getHeight(null));
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}
