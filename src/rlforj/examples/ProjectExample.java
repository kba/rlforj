package rlforj.examples;

import java.util.List;
import java.util.Random;

import rlforj.los.ILosAlgorithm;
import rlforj.los.PrecisePermissive;
import rlforj.los.ShadowCasting;

public class ProjectExample
{

	public static void main(String[] args)
	{
		ExampleBoard b = new ExampleBoard(21, 21);
		Random rand=new Random();
		for(int i=0; i<30; i++) {
			b.setObstacle(rand.nextInt(21), rand.nextInt(21));
		}
		int x1=rand.nextInt(21), y1=rand.nextInt(21);
		b.invisibleFloor='.';
		b.invisibleWall='#';
		
		System.out.println("ShadowCasting");
		ILosAlgorithm a=new ShadowCasting();
		boolean los=a.existsLineOfSight(b, 10, 10, x1, y1, true);
		
		List<Integer> pathx = a.getProjectPathX(), pathy=a.getProjectPathY();
		markProjectPath(b, pathx, pathy);
		b.mark(x1, y1, '*');
		System.out.println("Los "+(los?"exists":"does not exist"));
		b.print(10, 10);
		
		b.reset();
		System.out.println("Precise Permissive");
		a=new PrecisePermissive();
		los=a.existsLineOfSight(b, 10, 10, x1, y1, true);
		
		pathx = a.getProjectPathX(); pathy=a.getProjectPathY();
		markProjectPath(b, pathx, pathy);
		b.mark(x1, y1, '*');
		System.out.println("Los "+(los?"exists":"does not exist"));
		b.print(10, 10);
	}

	private static void markProjectPath(ExampleBoard b, List<Integer> pathx, List<Integer> pathy)
	{
		int lastx=pathx.get(0), lasty=pathy.get(0);
		for(int i=1; i<pathx.size(); i++) {
			int x=pathx.get(i), y=pathy.get(i);
			if(x!=lastx) {
				if(y!=lasty) {
					b.mark(x, y, ((x-lastx)*(y-lasty)>0)?'\\':'/');
				} else
					b.mark(x, y, '-');
			} else
				b.mark(x, y, '|');
			
			lastx=x; lasty=y;
		}
	}
}
