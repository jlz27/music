package algorithms;

import gui.ImageMorphGUI;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class RandomMorph extends AbstractImageMorph {

	private List<Pair<Integer, Integer>> points;
	
	private void generatePoints() {
		this.points = new LinkedList<Pair<Integer, Integer>>();
		for (int i = 0; i < ImageMorphGUI.IMAGE_WIDTH; ++i) {
			for (int j = 0; j < ImageMorphGUI.IMAGE_HEIGHT; ++j) {
				this.points.add(new Pair<Integer, Integer>(i, j));
			}
		}
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
				int loc = (int) Math.floor(this.points.size() * Math.random());
				Pair<Integer, Integer> dot = this.points.get(loc);
				this.points.remove(loc);
				this.getSourceImage().setRGB(dot.first, dot.second, this.getDestImage().getRGB(dot.first, dot.second));	
			} else {
				break;
			}
		}
		return this.getSourceImage();
	}

}
