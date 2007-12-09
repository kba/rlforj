package rlforj.los;

public interface IConeFovAlgorithm extends IFovAlgorithm
{

	public void visitConeFieldOfView(ILosBoard b, int x, int y, int distance, int startAngle, int finishAngle);
}
