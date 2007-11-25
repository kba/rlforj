package rlforj.los.test;

import java.util.HashSet;
import java.util.Set;

import rlforj.los.ILosBoard;
import rlforj.math.Point2I;

public class TestBoard implements ILosBoard
{

	public boolean def; // true => obstacle

	public Set<Point2I> exception = new HashSet<Point2I>();

	public Set<Point2I> visited = new HashSet<Point2I>();

	public Set<Point2I> chkb4visit = new HashSet<Point2I>();

	public Set<Point2I> visiterr = new HashSet<Point2I>();

	public TestBoard(boolean def)
	{
		this.def = def;
	}

	public boolean contains(int x, int y)
	{
		return true;
	}

	public boolean isObstacle(int x, int y)
	{
		Point2I p = new Point2I(x, y);
		if (!visited.contains(p))
			chkb4visit.add(p);
		return def ^ exception.contains(new Point2I(x, y));
	}

	public void visit(int x, int y)
	{
		Point2I p = new Point2I(x, y);
		if (visited.contains(p))
			visiterr.add(p);
		visited.add(new Point2I(x, y));
	}

	public void print(int fromx, int tox, int fromy, int toy)
	{
		for (int x = fromx; x <= tox; x++)
		{
			for (int y = fromy; y <= toy; y++)
			{
				if (isObstacle(x, y))
					System.out.print(visited.contains(new Point2I(x, y)) ? "X"
							: "#");
				else
					System.out.print(visited.contains(new Point2I(x, y)) ? "O"
							: "*");
			}
			System.out.println();
		}
	}

}
