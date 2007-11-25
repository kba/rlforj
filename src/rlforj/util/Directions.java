package rlforj.util;

import rlforj.math.Point2I;

public enum Directions
{
	NORTH, WEST, SOUTH, EAST;
	public static final int[] 
	    dx = { 0, -1, 0, 1},
		dy = { 1, 0, -1, 0};
	
	public Point2I getDir() {
		return new Point2I(dx[this.ordinal()], dy[this.ordinal()]);
	}
	
	public int dx() {
		return dx[this.ordinal()];
	}
	
	public int dy() {
		return dy[this.ordinal()];
	}
	
	public static Directions getDirection(int i) {
		return values()[i];
	}
}
