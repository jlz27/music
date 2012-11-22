package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import algorithms.AbstractImageMorph;

public class ImageComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	
	AbstractImageMorph morphAlg;
	
	public ImageComponent(AbstractImageMorph morphAlg) {
		this.morphAlg = morphAlg;
	}
	
	public void setImage(BufferedImage srcImg, BufferedImage destImg) {
		this.morphAlg.setSourceImg(srcImg);
		this.morphAlg.setDestImg(destImg);
		this.repaint();
	}
	
	public void morphPixels(int numPixels) {
		this.morphAlg.morphPixels(numPixels);
		this.repaint();
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(ImageMorphGUI.IMAGE_WIDTH, ImageMorphGUI.IMAGE_HEIGHT);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(this.morphAlg.getSourceImage(), 0, 0, null);
	}
}
