package algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class ThreshholdSegment extends AbstractImageMorph {
	private static final double DELTA = 0.001;

	private List<Pair<Integer, Integer>> blackList, whiteList;
	
	private double avgPixel(int pixel) {
		int red = (pixel >> 16) & 0xFF;
		int green = (pixel >> 8) & 0xFF;
		int blue = pixel & 0xFF;
		return (red + green + blue) / 3;
	}
	
	private double segmentByThreshhold(double thresh) {
		int foreCount = 0, backCount = 0;
		double foreThresh = 0.0, backThresh = 0.0;
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				double pixelVal = avgPixel(this.getDestImage().getRGB(i, j));
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
	
	private void generatePoints(double thresh) {
		this.whiteList = new LinkedList<Pair<Integer, Integer>>();
		this.blackList = new LinkedList<Pair<Integer, Integer>>();
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				if(avgPixel(this.getDestImage().getRGB(i, j)) >= thresh) {
					this.whiteList.add(new Pair<Integer, Integer>(i, j));
					//this.getSourceImage().setRGB(i, j, Color.white.getRGB());
				} else {
					this.blackList.add(new Pair<Integer, Integer>(i,j));
					//this.getSourceImage().setRGB(i, j, Color.black.getRGB());
				}
			}
		}
	}
	
	@Override
	public void setDestImg(BufferedImage img) {
		super.setDestImg(img);
		double initialT = 0.0;
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				int pixel = this.getDestImage().getRGB(i, j);
				initialT += avgPixel(pixel);
			}
		}
		initialT = initialT / (this.getWidth() * this.getHeight());
		double newT = 0.0;
		do {
			newT = initialT;
			initialT = segmentByThreshhold(initialT);
		} while (Math.abs(newT - initialT) > DELTA);
		generatePoints((newT + initialT)/2);
	}
	
	@Override
	public BufferedImage morphPixels(int numPixels) {
		for (int i = 0; i < numPixels; ++i) {
			Pair<Integer, Integer> dot;
			if (!this.blackList.isEmpty()) {
				int loc = (int) Math.floor(this.blackList.size()*Math.random());
				dot = this.blackList.get(loc);
				this.blackList.remove(loc);
			} else {
				if (!this.whiteList.isEmpty()) {
					int loc = (int) Math.floor(this.whiteList.size()*Math.random());
					dot = this.whiteList.get(loc);
					this.whiteList.remove(loc);
				} else {
					break;
				}
			}
			this.getSourceImage().setRGB(dot.first, dot.second, this.getDestImage().getRGB(dot.first, dot.second));
		}
		return this.getSourceImage();
	}
}
