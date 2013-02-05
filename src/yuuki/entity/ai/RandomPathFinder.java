package yuuki.entity.ai;

import java.awt.Point;

import yuuki.world.WalkGraph;

/**
 * Randomly finds a path.
 */
public class RandomPathFinder extends OverworldPathFinder {
	
	@Override
	@SuppressWarnings("fallthrough")
	public Point getNextMove(WalkGraph graph) {
		int choice = (int) Math.floor((Math.random() * 8));
		Point dest = null;
		switch (choice) {
			case 0:
				dest = graph.getNorth();
				if (dest != null) {
					break;
				}
			case 1:
				dest = graph.getNorthEast();
				if (dest != null) {
					break;
				}
			case 2:
				dest = graph.getEast();
				if (dest != null) {
					break;
				}
			case 3:
				dest = graph.getSouthEast();
				if (dest != null) {
					break;
				}
			case 4:
				dest = graph.getSouth();
				if (dest != null) {
					break;
				}
			case 5:
				dest = graph.getSouthWest();
				if (dest != null) {
					break;
				}
			case 6:
				dest = graph.getWest();
				if (dest != null) {
					break;
				}
			case 7:
				dest = graph.getNorthWest();
				if (dest != null) {
					break;
				}
		}
		return dest;
	}
	
}
