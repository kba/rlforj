package rlforj.los;

import java.util.List;

/**
 * Implement an algorithm for LOS and projection
 * @author sdatta
 *
 */
public interface ILosAlgorithm
{

	public abstract boolean existsLineOfSight(ILosBoard b, int startX,
			int startY, int x1, int y1, boolean calculateProject);

	public abstract List<Integer> getProjectPathX();

	public abstract List<Integer> getProjectPathY();

}