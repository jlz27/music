package algorithms;

import gui.ImageMorphGUI;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

public class ContrastMorph extends AbstractImageMorph {

	private List<Pair<Integer, Integer>> points;
	
	private void generatePoints() {
		LinkedList<Pair<Pair<Integer, Integer>, Double>> tempPoints = new LinkedList<Pair<Pair<Integer, Integer>, Double>>();
		for (int i = 0; i < ImageMorphGUI.IMAGE_WIDTH; ++i) {
			for (int j = 0; j < ImageMorphGUI.IMAGE_HEIGHT; ++j) {
				tempPoints.add(new Pair<Pair<Integer, Integer>, Double>(new Pair<Integer, Integer>(i, j), computeContrast(i, j)));
			}
		}
		Collections.sort(tempPoints, new Comparator<Pair<Pair<Integer, Integer>, Double>>() {
			@Override
			public int compare(Pair<Pair<Integer, Integer>, Double> o1,
					Pair<Pair<Integer, Integer>, Double> o2) {
				return o2.second.compareTo(o1.second);
			}
		});
		this.points = new LinkedList<Pair<Integer, Integer>>();
		for (Pair<Pair<Integer, Integer>, Double> pair : tempPoints) {
			this.points.add(pair.first);
		}
	}
	
	private double computeContrast(int x, int y) {
		int pixelA =  this.getSourceImage().getRGB(x, y);
		int pixelB = this.getDestImage().getRGB(x, y);
		int redA = (pixelA >> 16) & 0xFF, blueA = (pixelA >> 8) & 0xFF, greenA = pixelA & 0xFF;
		int redB = (pixelB >> 16) & 0xFF, blueB = (pixelB >> 8) & 0xFF, greenB = pixelB & 0xFF;
		return Math.sqrt((Math.pow(redA - redB, 2) + Math.pow(blueA - blueB, 2) + Math.pow(greenA - greenB, 2))/3);
	}
	
	@Override
	public void setDestImg(BufferedImage img) {
		super.setDestImg(img);
		generatePoints();
	}
	
	@Override
	public BufferedImage morphPixels(int numPixels) {
		for (int i = 0 ; i < numPixels; ++i) {
			if (!this.points.isEmpty()) {
				Pair<Integer, Integer> dot = this.points.get(0);
				this.points.remove(0);
				this.getSourceImage().setRGB(dot.first, dot.second, this.getDestImage().getRGB(dot.first, dot.second));	
			} else {
				break;
			}
		}
		return this.getSourceImage();
	}

}
