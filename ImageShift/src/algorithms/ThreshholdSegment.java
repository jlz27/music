package algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ThreshholdSegment extends AbstractImageMorph {
	private static final double DELTA = 0.001;

	public ThreshholdSegment(BufferedImage img) {
		super(img);
	}
	
	private double avgPixel(int pixel) {
		int red = (pixel >> 16) & 0xFF;
		int blue = (pixel >> 8) & 0xFF;
		int green = pixel & 0xFF;
		return (red + green + blue) / 3;
	}
	
	private double segmentByThreshhold(double thresh) {
		int foreCount = 0, backCount = 0;
		double foreThresh = 0.0, backThresh = 0.0;
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				double pixelVal = avgPixel(this.getImg().getRGB(i, j));
				// Greater than threshhold, this pixel is in the foreground
				if (pixelVal > thresh) {
					foreThresh += pixelVal;
					foreCount++;
				// Smaller than threshhold, this pixel is in the background
				} else {
					backThresh += pixelVal;
					backCount++;
				}
			}
		}
		
		foreThresh = foreCount == 0 ? foreThresh : foreThresh / foreCount;
		backThresh = backCount == 0 ? backThresh : backThresh / backCount;
		return (foreThresh + backThresh) / 2;
	}
	
	private void colorImage(double thresh) {
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				if(avgPixel(this.getImg().getRGB(i, j)) >= thresh) {
					this.getImg().setRGB(i, j, Color.white.getRGB());
				} else {
					this.getImg().setRGB(i, j, Color.black.getRGB());
				}
			}
		}
	}
	
	@Override
	public BufferedImage morphPixels(int numPixels) {
		double initialT = 0.0;
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				int pixel = this.getImg().getRGB(i, j);
				initialT += avgPixel(pixel);
			}
		}
		initialT = initialT / (this.getWidth() * this.getHeight());
		double newT = 0.0;
		do {
			newT = initialT;
			initialT = segmentByThreshhold(initialT);
		} while (Math.abs(newT - initialT) > DELTA);
		
		colorImage((newT + initialT)/2);
		return this.getImg();
	}
}
