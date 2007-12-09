package rlforj.examples;

import java.util.Random;

import rlforj.los.ConePrecisePremisive;
import rlforj.los.IConeFovAlgorithm;
import rlforj.los.IFovAlgorithm;
import rlforj.los.PrecisePermissive;
import rlforj.los.ShadowCasting;

public class ConeFovExample
{

	public static void main(String[] args)
	{
		ExampleBoard b = new ExampleBoard(21, 21);
		Random rand=new Random();
		for(int i=0; i<30; i++) {
			b.setObstacle(rand.nextInt(21), rand.nextInt(21));
		}
		int startAngle=rand.nextInt(360), finishAngle=rand.nextInt(360);
		System.out.println(startAngle+" degrees to "+finishAngle+" degrees");
		System.out.println("ShadowCasting");
		IConeFovAlgorithm a=new ShadowCasting();
		a.visitConeFieldOfView(b, 10, 10, 9, startAngle, finishAngle);
		b.print(10, 10);
		
		b.reset();
		System.out.println("Precise Permissive");
		a=new ConePrecisePremisive();
		a.visitConeFieldOfView(b, 10, 10, 10, startAngle, finishAngle);
		b.print(10, 10);
	}
}
