package algorithms;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KMeans extends AbstractImageMorph{
	public static int K = 5;
	public static int DISTANCE_WEIGHT = 2;
	
	private double computeDist(int x, int y, Pair<Integer, Integer> b) {
		int pixelA =  this.getDestImage().getRGB(x, y);
		int pixelB = this.getDestImage().getRGB(b.first, b.second);
		int redA = (pixelA >> 16) & 0xFF, blueA = (pixelA >> 8) & 0xFF, greenA = pixelA & 0xFF;
		int redB = (pixelB >> 16) & 0xFF, blueB = (pixelB >> 8) & 0xFF, greenB = pixelB & 0xFF;
		
		return DISTANCE_WEIGHT*Math.pow((b.first - x), 2) + DISTANCE_WEIGHT*Math.pow((b.second - y), 2) + 
				Math.pow((redA - redB), 2) + Math.pow((blueA - blueB), 2) + Math.pow((greenA - greenB), 2);
	}
	
	private Set<Pair<Integer, Integer>> generateCenters() {
		Set<Pair<Integer, Integer>> centerSet = new HashSet<Pair<Integer, Integer>>();
		for (int i = 0; i < K; ++i) {
			centerSet.add(new Pair<Integer, Integer>((this.getWidth()-1)/(i+1), (this.getHeight()-1)/(i+1)));
		}
		return centerSet;
	}
	
	private Collection<Set<Pair<Integer, Integer>>> assignSets(Set<Pair<Integer, Integer>> centerSet) {
		Map<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>> centerMap = new HashMap<Pair<Integer, Integer>, Set<Pair<Integer, Integer>>>();
		for (Pair<Integer, Integer> key : centerSet) {
			centerMap.put(key, new HashSet<Pair<Integer, Integer>>());
		}
		for (int i = 0; i < this.getWidth(); ++i) {
			for (int j = 0; j < this.getHeight(); ++j) {
				double minDist = Double.MAX_VALUE;
				Pair<Integer, Integer> destKey = null;
				for (Pair<Integer, Integer> key : centerMap.keySet()) {
					double curDist = computeDist(i, j, key);
					if (curDist < minDist) {
						minDist = curDist;
						destKey = key;
					}
				}
				centerMap.get(destKey).add(new Pair<Integer, Integer>(i, j));
			}
		}
		return centerMap.values();
	}
	
	private Set<Pair<Integer, Integer>> computeNewCenters(Collection<Set<Pair<Integer, Integer>>> kMeanSets) {
		Set<Pair<Integer, Integer>> centerSet = new HashSet<Pair<Integer, Integer>>();
		for (Set<Pair<Integer, Integer>> set : kMeanSets) {
			int newX = 0, newY = 0;
			for (Pair<Integer, Integer> coord : set) {
				newX += coord.first;
				newY += coord.second;
			}
			centerSet.add(new Pair<Integer, Integer>(newX/set.size(), newY/set.size()));
		}
		return centerSet;
	}
	
	private boolean checkSetSame(Set<Pair<Integer, Integer>> oldSet, Set<Pair<Integer, Integer>> newSet) {
		for (Pair<Integer, Integer> center : oldSet) {
			newSet.remove(center);
		}
		return newSet.isEmpty();
	}
	
	@Override
	public BufferedImage morphPixels(int numPixels) {
		Set<Pair<Integer, Integer>> oldCenters;
		Set<Pair<Integer, Integer>> newCenters = generateCenters();
		Collection<Set<Pair<Integer, Integer>>> pixelSets;
		do {
			oldCenters = newCenters;
			pixelSets = assignSets(oldCenters);
			newCenters = computeNewCenters(pixelSets);
		} while(!checkSetSame(oldCenters, newCenters));
		
		return this.getDestImage();
	}

}
