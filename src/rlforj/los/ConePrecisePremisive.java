package rlforj.los;

import java.util.LinkedList;

public class ConePrecisePremisive extends PrecisePermissive implements
		IConeFovAlgorithm
{

	public void visitConeFieldOfView(ILosBoard b, int x, int y, int distance, int startAngle, int finishAngle)
	{
		if(startAngle%90==0 && startAngle%360!=0) startAngle--;//we dont like to start at 90, 180, 270
				// because it is screwed up by the "dont visit an axis twice" logic
		
		if(startAngle<0) {startAngle%=360; startAngle+=360; }
		if(finishAngle<0) {finishAngle%=360; finishAngle+=360; }
		
		if(startAngle>360) startAngle%=360;
		if(finishAngle>360) finishAngle%=360;
		
		permissiveMaskT mask = new permissiveMaskT();
		mask.east = mask.north = mask.south = mask.west = distance;
		mask.mask = null;
		mask.fovType = FovType.CIRCLE;
		mask.distSq = distance * distance;
		mask.board = b;
		permissiveConeFov(x, y, mask, startAngle, finishAngle);
	}

	void calculateConeFovQuadrant(final coneFovState state, int startAngle, int finishAngle)
	{
//		 System.out.println("calcfovq called " + state.quadrantIndex + " "
//				+ startAngle + " " + finishAngle);
		LinkedList<bumpT> steepBumps = new LinkedList<bumpT>();
		LinkedList<bumpT> shallowBumps = new LinkedList<bumpT>();
		// activeFields is sorted from shallow-to-steep.
		LinkedList<fieldT> activeFields = new LinkedList<fieldT>();
		activeFields.addLast(new fieldT());
		if(startAngle==0) {
			activeFields.getLast().shallow.near = new offsetT(0, 1);
			activeFields.getLast().shallow.far = new offsetT(state.extent.x, 0);
		} else {
			activeFields.getLast().shallow.near = new offsetT(0, 1);
			activeFields.getLast().shallow.far = new offsetT(
					(int) Math.ceil(Math.cos(Math.toRadians(startAngle))
							* state.extent.x), 
					(int) Math.floor(Math.sin(Math.toRadians(startAngle))
					* state.extent.y));
//			System.out.println(activeFields.getLast().shallow.isAboveOrContains(new offsetT(0, 10)));
		}
		if(finishAngle==90) {
			activeFields.getLast().steep.near = new offsetT(1, 0);
			activeFields.getLast().steep.far = new offsetT(0, state.extent.y);
		} else {
			activeFields.getLast().steep.near = new offsetT(1, 0);
			activeFields.getLast().steep.far = new offsetT(
					(int) Math.floor(Math.cos(Math.toRadians(finishAngle))
							* state.extent.x), 
					(int) Math.ceil(Math.sin(Math.toRadians(finishAngle))
					* state.extent.y));
		}
		offsetT dest = new offsetT(0, 0);

		// Visit the source square exactly once (in quadrant 1).
		if (state.quadrant.x == 1 && state.quadrant.y == 1)
		{
			actIsBlockedCone(state, dest);
		}

		CLikeIterator<fieldT> currentField = new CLikeIterator<fieldT>(
				activeFields.listIterator());
		int i = 0;
		int j = 0;
		int maxI = state.extent.x + state.extent.y;
		// For each square outline
		for (i = 1; i <= maxI && !activeFields.isEmpty(); ++i)
		{
			int startJ = max(0, i - state.extent.x);
			int maxJ = min(i, state.extent.y);
			// System.out.println("Startj "+startJ+" maxj "+maxJ);
			// Visit the nodes in the outline
			for (j = startJ; j <= maxJ && !currentField.isAtEnd(); ++j)
			{
				// System.out.println("i j "+i+" "+j);
				dest.x = i - j;
				dest.y = j;
				visitConeSquare(state, dest, currentField, steepBumps,
						shallowBumps, activeFields);
			}
			// System.out.println("Activefields size "+activeFields.size());
			currentField = new CLikeIterator<fieldT>(activeFields
					.listIterator());
		}
	}
	
	private final int max(int i, int j)
	{
		return i > j ? i : j;
	}

	private final int min(int i, int j)
	{
		return i < j ? i : j;
	}
	public class coneFovState extends fovStateT {
		public boolean axisDone[]={false, false, false, false};
	}
	void permissiveConeFov(int sourceX, int sourceY, permissiveMaskT mask,
			int startAngle, int finishAngle)
	{
		coneFovState state = new coneFovState();
		state.source = new offsetT(sourceX, sourceY);
		state.mask = mask;
		state.board = mask.board;
		// state.isBlocked = isBlocked;
		// state.visit = visit;
		// state.context = context;

		final int quadrantCount = 4;
		final offsetT quadrants[] = { new offsetT(1, 1), new offsetT(-1, 1),
				new offsetT(-1, -1), new offsetT(1, -1) };

		offsetT extents[] = { new offsetT(mask.east, mask.north),
				new offsetT(mask.west, mask.north),
				new offsetT(mask.west, mask.south),
				new offsetT(mask.east, mask.south) };
		
		int[] angles=new int[12];
		angles[0]=0; angles[1]=90; angles[2]=180; angles[3]=270;
		for(int i=4; i<12; i++) angles[i]=720;//to keep them at the end
		int i=0;
		for(i=0; i<4; i++) {
			if(startAngle<angles[i])
			{
				for(int j=3; j>=i; j--)
					angles[j+1]=angles[j];
				break;
			}
		}
		angles[i]=startAngle;
		for(i=0; i<5; i++) {
			if(finishAngle<angles[i])
			{
				for(int j=4; j>=i; j--)
					angles[j+1]=angles[j];
				break;
			}
		}
		angles[i]=finishAngle;
		int startIndex=0;		
		for (i = 0; i < 6; i++)
		{
//			System.out.println("sorted "+angles[i]);
			angles[i + 6] = angles[i];
			if (angles[i] == startAngle)
				startIndex = i;
		}
		
		int stA=0, endA=0;
		for(i=startIndex; i<12; i++)
		{
			if(angles[i]==finishAngle)
				break;
			int quadrantIndex=angles[i]/90;
			switch(quadrantIndex) {
			case 0:
				stA=angles[i];
				endA=angles[i+1];
				break;
			case 1:
				stA=180-angles[i+1];
				endA=180-angles[i];
				break;
			case 2:
				stA=angles[i]-180;
				endA=angles[i+1]-180;
				break;
			case 3:
				stA=360-angles[i+1];
				endA=360-angles[i];
				break;
			}
			state.quadrant = quadrants[quadrantIndex];
			state.extent = extents[quadrantIndex];
			state.quadrantIndex = quadrantIndex;
			
			calculateConeFovQuadrant(state, stA, endA);
//			System.out.println(quadrantIndex+" "+stA+" "+endA);
			
			if(stA==0) state.axisDone[quadrantIndex]=true;
			if(endA==90) state.axisDone[(quadrantIndex+1)%4]=true;
//			System.out.println(Arrays.toString(state.axisDone));
		}
	}
	
	void visitConeSquare(final coneFovState state, final offsetT dest,
			CLikeIterator<fieldT> currentField, LinkedList<bumpT> steepBumps,
			LinkedList<bumpT> shallowBumps, LinkedList<fieldT> activeFields)
	{
		// System.out.println("visitsq called "+dest);
		// The top-left and bottom-right corners of the destination square.
		offsetT topLeft = new offsetT(dest.x, dest.y + 1);
		offsetT bottomRight = new offsetT(dest.x + 1, dest.y);
//		System.out.println(dest);
		// fieldT currFld=null;

		boolean specialCase=false;
		
		while (!currentField.isAtEnd()
				&& currentField.getCurrent().steep
						.isBelowOrContains(bottomRight))
		{
//			System.out.println("currFld.steep.isBelowOrContains(bottomRight) "
//					+ currentField.getCurrent().steep
//							.isBelowOrContains(bottomRight));
			// case ABOVE
			// The square is in case 'above'. This means that it is ignored
			// for the currentField. But the steeper fields might need it.
			// ++currentField;
//			System.out.println("currFld.steep.isBelowOrContains(bottomRight) and shallow "+
//					currentField.getCurrent().shallow.isAboveOrContains(bottomRight)+" "+
//					currentField.getCurrent().steep.isBelowOrContains(topLeft));
			
			if(currentField.getCurrent().shallow.isAboveOrContains(bottomRight)&&
					currentField.getCurrent().steep.isBelowOrContains(topLeft)) {
				specialCase=true;
				break;
			}
			
			currentField.gotoNext();
		}
		if (currentField.isAtEnd())
		{
//			System.out.println("currentField.isAtEnd()");
			// The square was in case 'above' for all fields. This means that
			// we no longer care about it or any squares in its diagonal rank.
			return;
		}

		// Now we check for other cases.
		if (currentField.getCurrent().shallow.isAboveOrContains(topLeft))
		{
			// case BELOW
			// The shallow line is above the extremity of the square, so that
			// square is ignored.
			
//			System.out.println("currFld.shallow.isAboveOrContains(topLeft) "
//					+ currentField.getCurrent() +
//					currentField.getCurrent().shallow.isAboveOrContains(topLeft)+" "+
//					currentField.getCurrent().shallow.isAboveOrContains(bottomRight)+" "+
//					currentField.getCurrent().steep.isAboveOrContains(topLeft)+" "+
//					currentField.getCurrent().steep.isAboveOrContains(bottomRight));
			
				return;
		}
		// The square is between the lines in some way. This means that we
		// need to visit it and determine whether it is blocked.

		boolean isBlocked = actIsBlockedCone(state, dest);
		if (!isBlocked)
		{
			// We don't care what case might be left, because this square does
			// not obstruct.
			return;
		}

		if (currentField.getCurrent().shallow.isAbove(bottomRight)
				&& currentField.getCurrent().steep.isBelow(topLeft))
		{
			// case BLOCKING
			// Both lines intersect the square. This current field has ended.
			currentField.removeCurrent();
		} else if (currentField.getCurrent().shallow.isAbove(bottomRight))
		{
			// case SHALLOW BUMP
			// The square intersects only the shallow line.
			addShallowBump(topLeft, currentField.getCurrent(), steepBumps,
					shallowBumps);
			checkField(currentField);
		} else if (currentField.getCurrent().steep.isBelow(topLeft))
		{
			// case STEEP BUMP
			// The square intersects only the steep line.
			addSteepBump(bottomRight, currentField.getCurrent(), steepBumps,
					shallowBumps);
			checkField(currentField);
		} else
		{
			// case BETWEEN
			// The square intersects neither line. We need to split into two
			// fields.
			fieldT steeperField = new fieldT(currentField.getCurrent());
			fieldT shallowerField = currentField.getCurrent();
			currentField.insertBeforeCurrent(steeperField);
			// System.out.println("activeFields "+activeFields);
			addSteepBump(bottomRight, shallowerField, steepBumps, shallowBumps);
			currentField.gotoPrevious();
			if (!checkField(currentField)) // did not remove
				currentField.gotoNext();// point to the original element
//			System.out.println("B4 addShallowBumps "
//					+ currentField.getCurrent());
			addShallowBump(topLeft, steeperField, steepBumps, shallowBumps);
			checkField(currentField);
		}
	}
	
	boolean actIsBlockedCone(final coneFovState state, final offsetT pos)
	{
		final offsetT stateQuadrant = state.quadrant;
		offsetT adjustedPos = new offsetT(pos.x * stateQuadrant.x
				+ state.source.x, pos.y * stateQuadrant.y + state.source.y);

//		System.out.println(adjustedPos);
//		System.out.println((pos.x==0 && state.quadrant.y>0 && !state.axisDone[1])
//				+" "+ (pos.x==0 && state.quadrant.y<0 && !state.axisDone[3])
//				+" "+ (pos.y==0 && state.quadrant.x>0 && !state.axisDone[0])
//				+" "+ (pos.y==0 && state.quadrant.x<0 && !state.axisDone[2])
//				+" "+ (pos.x!=0 && pos.y!=0) );
		if (
				   (pos.x==0 && stateQuadrant.y>0 && !state.axisDone[1])
				|| (pos.x==0 && stateQuadrant.y<0 && !state.axisDone[3])
				|| (pos.y==0 && stateQuadrant.x>0 && !state.axisDone[0])
				|| (pos.y==0 && stateQuadrant.x<0 && !state.axisDone[2])
				|| (pos.x!=0 && pos.y!=0)
				)
			if (doesPermissiveVisit(state.mask, pos.x * stateQuadrant.x, pos.y
					* stateQuadrant.y) == 1)
			{
				state.board.visit(adjustedPos.x, adjustedPos.y);
			}
		return state.board.isObstacle(adjustedPos.x, adjustedPos.y);
	}
}
