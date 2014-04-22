package tuxkids.tuxblocks.core.tutorial;

import tuxkids.tuxblocks.core.solve.blocks.Equation;
import tuxkids.tuxblocks.core.tutorial.gen.StarredTutorial1_Base;

public class StarredTutorial1 extends StarredTutorial implements StarredTutorial1_Base {

	@Override
	public String filename() {
		return filename;
	}

	@Override
	protected void setUpStates() {
		addStartState(new FSMState());
	}

	@Override
	public Equation createEquation() {
		return new Equation.Builder()
		.addLeft("x")
		.addRight(6)
		.createEquation();
	}

}
