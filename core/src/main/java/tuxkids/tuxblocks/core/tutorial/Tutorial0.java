package tuxkids.tuxblocks.core.tutorial;


import static tuxkids.tuxblocks.core.tutorial.Tutorial.Trigger.*;
import pythagoras.i.Point;
import tuxkids.tuxblocks.core.story.StoryGameState;
import tuxkids.tuxblocks.core.tutorial.Tutorial.Tag;
import tuxkids.tuxblocks.core.tutorial.gen.Tutorial0_Base;
import tuxkids.tuxblocks.core.utils.Debug;

public class Tutorial0 extends FSMTutorial implements Tutorial0_Base {



	public Tutorial0(StoryGameState storyGameState) {
		super(storyGameState);
	}

	@Override
	protected void setUpStates() {
		FSMState one = addStartState(id_nextWaveSoon);
		FSMState two = addState(id_shoreUpDefenses);
		FSMState three = addState(id_dragFirstTower).addHighlightable(Tag.Defense_PeaShooter);
		final FSMState four = addState(id_goodFirstPlacement);
		final FSMState five = addState(id_okayFirstPlacement);
		FSMState six = addState(id_secondTowerPlacement);
		FSMState seven = new FSMState(){
			@Override
			public FSMState notifyMessageShown() {
				gameState.level().startNextRound();
				return super.notifyMessageShown();
			}
		};
		
		one.addTransition(two, TextBoxHidden);
		two.addTransition(three, TextBoxHidden);
		
		three.addTransition(new StateChooser() {
			
			@Override
			public FSMState chooseState(Object extraInformation) {
				if (extraInformation instanceof Point) {
					Point point = (Point) extraInformation;
					if (point.x == 5 && point.y == 2) {
						return four;
					}
				}
				return five;
			}
		}, Defense_TowerDropped);
		
		four.addTransition(six, Defense_TowerDropped);
		five.addTransition(six, Defense_TowerDropped);
		six.addTransition(seven, TextBoxHidden);
		seven.addTransition(endState, Defense_RoundOver);
		anyState.addTransition(endState, Defense_RoundOver);
		
		handleBadTowerTransition(one);
		handleBadTowerTransition(two);
		handleBadTowerTransition(three);
		handleBadTowerTransition(four);
		handleBadTowerTransition(five);
		
		
	}

	private void handleBadTowerTransition(final FSMState originalState) {
		FSMState error = addState(id_badTowerPlacement);
		originalState.addTransition(error, Defense_BadTowerPlacement);
		error.registerEpsilonTransition(originalState);
		
	}

	
	@Override
	protected void endOfTutorial() {
		Debug.write("End");
		super.endOfTutorial();
		gameState.finishedLesson();
	}

}
