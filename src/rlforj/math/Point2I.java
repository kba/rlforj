package rlforj.math;

import java.awt.Point;
import java.io.Serializable;

public class Point2I extends Point implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1360560915480181893L;

	public Point2I(int x, int y)
	{
		this.x=x; this.y=y;
	}

	/**
	 * Uses x+y as hash
	 */
	public int hashCode()
	{
		return x<<7-x+y;//x*prime+y
	}
	
	public String toString()
	{
		return "Point2I[ "+x+", "+y+" ]";
	}
}
