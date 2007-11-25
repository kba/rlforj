package rlforj.examples;

import java.util.Random;

import com.sun.xml.internal.ws.client.InternalBindingProvider;

import rlforj.los.IFovAlgorithm;
import rlforj.los.ILosBoard;
import rlforj.los.PrecisePermissive;
import rlforj.los.ShadowCasting;
import rlforj.los.test.TestBoard;

public class FovExample
{

	/**
	 * Each time creates a 21x21 area with random obstacles and
	 * runs ShadowCasting and Precise Permissive algoritms 
	 * on it, printing out the reslts in stdout.
	 * @param args
	 */
	public static void main(String[] args)
	{
		ExampleBoard b = new ExampleBoard(21, 21);
		Random rand=new Random();
		for(int i=0; i<30; i++) {
			b.setObstacle(rand.nextInt(21), rand.nextInt(21));
		}
		
		System.out.println("ShadowCasting");
		IFovAlgorithm a=new ShadowCasting();
		a.visitFieldOfView(b, 10, 10, 9);
		b.print(10, 10);
		
		b.resetVisited();
		System.out.println("Precise Permissive");
		a=new PrecisePermissive();
		a.visitFieldOfView(b, 10, 10, 10);
		b.print(10, 10);
	}
	
	static class ExampleBoard implements ILosBoard  {

		int w, h;
		
		boolean[][] obstacles;
		boolean[][] visited;
		
		public ExampleBoard(int w, int h) {
			this.w=w;
			this.h=h;
			
			obstacles = new boolean[w][h];
			visited = new boolean[w][h];
		}
		
		public void resetVisited()
		{
			visited = new boolean[w][h];
		}

		public void setObstacle(int x, int y) {
			obstacles[x][y]=true;
		}
		
		public boolean contains(int x, int y)
		{
			return x>=0 && y>=0 && x<w && y<h;
		}

		public boolean isObstacle(int x, int y)
		{
			return obstacles[x][y];
		}

		public void visit(int x, int y)
		{
			visited[x][y]=true;
		}
		
		public void print(int ox, int oy) {
			for(int j=0; j<h; j++) {
				for(int i=0; i<w; i++)
					if(i==ox && j==oy)
						System.out.print('@');
					else
						System.out.print(visited[i][j]?(obstacles[i][j]?'#':'.'):' ');
				System.out.println();
			}
		}
		
	}
}
