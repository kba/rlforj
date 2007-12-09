package rlforj.los.test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import rlforj.los.ILosAlgorithm;
import rlforj.los.ShadowCasting;
import rlforj.math.Point2I;

public class ProjectionTest
{

	public static void main(String[] args)
	{
		Random rand = new Random();
		TestBoard tb = new TestBoard(false);
		
		for(int i=0; i<50; i++) {
			tb.exception.add(new Point2I(rand.nextInt(21), rand.nextInt(21)));
		}
		
		int x1=rand.nextInt(21), y1=rand.nextInt(21);
//		int x1=45, y1=10;
//		tb.exception.add(new Point2I(7, 11));
//		tb.exception.add(new Point2I(13, 12));
		
//		ILosAlgorithm alg = new PrecisePermissive();
		ILosAlgorithm alg = new ShadowCasting();
		
		boolean losExists = alg.existsLineOfSight(tb, 10, 10, x1, y1, true);
		List<Integer> x=alg.getProjectPathX();
		List<Integer> y = alg.getProjectPathY();
		Iterator<Integer> xit = x.iterator(); Iterator<Integer> yit = y.iterator();
		
		while(xit.hasNext() && yit.hasNext()) {
			int xx=xit.next(), yy=yit.next();
			tb.mark(xx, yy, '-');
		}
		
		tb.mark(10, 10, '@');
		tb.mark(x1, y1, '*');
		
		tb.print(-1, 46, -1, 22);
		System.out.println("LosExists "+losExists);
	}
}
