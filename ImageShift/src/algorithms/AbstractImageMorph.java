package algorithms;

import java.awt.image.BufferedImage;

public abstract class AbstractImageMorph {
	
	private final BufferedImage img;
	private final int width, height;
	
	public AbstractImageMorph(BufferedImage img) {
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
	}
	
	
	public BufferedImage getImg() {
		return img;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}


	public abstract BufferedImage morphPixels(int numPixels);
}
