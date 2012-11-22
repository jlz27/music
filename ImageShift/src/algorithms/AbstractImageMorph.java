package algorithms;

import gui.ImageMorphGUI;

import java.awt.image.BufferedImage;

public abstract class AbstractImageMorph {
	
	private BufferedImage sourceImg, destImg;
	private final int width = ImageMorphGUI.IMAGE_WIDTH, height = ImageMorphGUI.IMAGE_HEIGHT;
	
	public void setSourceImg(BufferedImage sourceImg) {
		this.sourceImg = new BufferedImage(sourceImg.getColorModel(), sourceImg.copyData(null), sourceImg.isAlphaPremultiplied(), null);
	}
	
	public void setDestImg(BufferedImage destImg) {
		this.destImg = destImg;
	}
	
	public BufferedImage getSourceImage() {
		return this.sourceImg;
	}
	
	public BufferedImage getDestImage() {
		return this.destImg;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public abstract BufferedImage morphPixels(int numPixels);
}
