package rlforj.math;

public class Point3I
{
	int x, y, z;
	
	public Point3I(double[] d)
	{
		x=(int) Math.floor(d[0]);
		y=(int) Math.floor(d[1]);
		z=(int) Math.floor(d[2]);
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + x;
		result = PRIME * result + y;
		result = PRIME * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Point3I other = (Point3I) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	
	
}
