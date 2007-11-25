package rlforj.los;

/**
 * A FOV algorithm.
 * 
 * @author sdatta
 *
 */
public interface IFovAlgorithm
{

	/**
	 * All locations of Board b that are visible
	 * from (x, y) will be visited, ie b.visit(x, y)
	 * will be called on them.
	 *  
	 * @param b
	 * @param x
	 * @param y
	 * @param distance
	 */
	public void visitFieldOfView(ILosBoard b, int x, int y, int distance);
	
}
