package rlforj.los;

import java.util.List;
import java.util.Vector;

import rlforj.los.test.TestBoard;
import rlforj.math.Point2I;
import rlforj.util.BresenhamLine;

public class BresOpportunisticLos implements ILosAlgorithm
{

	public boolean SYMMETRIC_ENABLED=true;
	
	private Vector<Point2I> path;
	
	public boolean existsLineOfSight(ILosBoard b, int startX, int startY,
			int x1, int y1, boolean calculateProject)
	{
		int dx=startX-x1, dy=startY-y1;
		int adx=dx>0?dx:-dx, ady=dy>0?dy:-dy;
		int len=(adx>ady?adx:ady) + 1;
		
		if(calculateProject)
			path=new Vector<Point2I>(len);
		
		int[] px=new int[len], py=new int[len];
		int[] px1=null, py1=null;
		if(SYMMETRIC_ENABLED) {
			px1=new int[len]; py1=new int[len];
		}
		BresenhamLine.plot(startX, startY, x1, y1, px, py);
		if(SYMMETRIC_ENABLED) {
			BresenhamLine.plot(x1, y1, startX, startY, px1, py1);
		}

		boolean los=false;
		boolean alternatePath=false;
		for(int i=0; i<len; i++) {
			if(px[i]==x1 && py[i]==y1) {
				if(calculateProject){
					path.add(new Point2I(px[i], py[i]));
				}
				los=true;
				break;
			}
			if(alternatePath && !b.isObstacle(px1[len-i-1], py1[len-i-1])) {
				if(calculateProject)
					path.add(new Point2I(px1[len-i-1], py1[len-i-1]));
				continue;
			} else
				alternatePath=false;
			
			if(!b.isObstacle(px[i], py[i])) {
				if(calculateProject) {
					path.add(new Point2I(px[i], py[i]));
				}
				continue;
			}
			if(SYMMETRIC_ENABLED && !b.isObstacle(px1[len-i-1], py1[len-i-1])) {
				if(calculateProject)
					path.add(new Point2I(px1[len-i-1], py1[len-i-1]));
				alternatePath=true;
				continue;
			}
			break;
		}
		// TODO Auto-generated method stub
		return los;
	}

	public List<Point2I> getProjectPath()
	{
		return path;
	}

}
