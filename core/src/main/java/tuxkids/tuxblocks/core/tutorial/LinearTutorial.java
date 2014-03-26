package tuxkids.tuxblocks.core.tutorial;

import java.util.ArrayList;
import java.util.List;

import playn.core.util.Clock;
import tuxkids.tuxblocks.core.tutorial.Tutorial.Tag;
import tuxkids.tuxblocks.core.tutorial.Tutorial.Trigger;

public abstract class LinearTutorial implements TutorialInstance {
	
	private final List<String> sections = new ArrayList<String>();
	
	protected final List<Action> actions = new ArrayList<Action>();
	protected final int themeColor, secondaryColor;

	protected TutorialLayer tutorialLayer;
	protected int actionIndex = -1;
	protected boolean canReshow;
	
	protected abstract void addActions();
	
	protected boolean hasAction() {
		return actionIndex > 0 && actionIndex < actions.size();
	}
	
	protected Action action() {
		return actions.get(actionIndex);
	}
	
	protected Action nextAction() {
		return actions.get(++actionIndex);
	}
	
	protected Action peek() {
		return actions.get(actionIndex + 1);
	}
	
	protected boolean hasNext() {
		return actionIndex < actions.size() - 1;
	}
	
	public LinearTutorial(int themeColor, int secondaryColor) {
		this.themeColor = themeColor;
		this.secondaryColor = secondaryColor;
	}
	
	public void loadTextFile(String text) {
		// load the text into lines
		for (String line : text.split("\n")) {
			// normalize line-ends
			line = Tutorial.prepareMessage(line);
			if (!line.replace(" ", "").isEmpty()) {
				sections.add(line);
			}
		}
		
		addActions();

		tutorialLayer = new TutorialLayer(themeColor);
		trigger(null);
	}
	
	public void update(int delta) {
		
	}
	
	public void paint(Clock clock) {
		if (tutorialLayer != null) {
			tutorialLayer.paint(clock);
		}
	}

	// called when something happens that might
	// advance the tutorial
	public void trigger(Trigger event) {
		if (hasNext() && peek().trigger == event) {
			// if the tirgger causes the next Action...
			Action action = nextAction();
			if (action instanceof Segue) {
				String path = ((Segue) action).path;
				TutorialInstance tutorial = ((Segue) action).tutorial;
				destroy();
				Tutorial.start(tutorial, path);
				return;
			} else {
				tutorialLayer.showMessage(action.message);
			}
			canReshow = false;
		} else if (canReshow && actionIndex >= 0 && action().trigger == event) {
			// or reshow this action's message if the player has left the screen
			// it was originally shown on and come back
			tutorialLayer.showMessage(action().message);
			canReshow = false;
		} else if (event != null && hasNext() && peek().skipTrigger == event) {
			// sometimes we want to skip an Action if the next Action is ready
			// (when players do things out of order) so we skip an Action
			actions.remove(peek());
			trigger(event);
			return;
		} else if (event == Trigger.TextBoxFullyHidden && !hasNext()) {
			// if this is the last Action and the textbox is hidden, end the tutorial
			destroy();
			return;
		}
		refreshHighlights();
	}
	
	public void destroy() {
		tutorialLayer.destroy();
		Tutorial.clearIndicators();
	}

	// for segueing into a new tutorial 
	protected Segue addSegue(TutorialInstance tutorial, String path, Trigger trigger) {
		Segue segue = new Segue(tutorial, path);
		segue.trigger = trigger;
		actions.add(segue);
		return segue;
	}

	// for skipping part of a tutorial for testing
	// i.e. don't use this in production
	@Deprecated
	protected void addStart() {
		actions.clear();
	}
	
	public void didLeaveScreen() {
		if (actionIndex >= 0 && action().canRepeat) {
			// alow the Action's message to be reshown, if applicable
			canReshow = true;
		}
	}

	protected Action addAction(Trigger trigger) {
		Action action = new Action();
		action.message = sections.remove(0);
		action.trigger = trigger;
		actions.add(action);
		return action;
	}

	@Override
	public void refreshHighlights() {
		if (hasAction()) Tutorial.refreshHighlights(action().highlights);
	}
	
	@Override
	public void wasRepeated() {
		canReshow = false;
	}
	
	protected enum Align {
		Center,
		TopLeft,
		TopRight,
		BottomLeft,
		ButtonRight
	}
	
	/** For segueing between tutorials */
	protected class Segue extends Action {
		public final TutorialInstance tutorial;
		public final String path;
		
		public Segue(TutorialInstance tutorial, String path) {
			this.path = path;
			this.tutorial = tutorial;
		}
	}
	
	/** Represents one segment of the tutorial, it's trigger and what it highlights. */
	protected class Action { 
		public Trigger trigger, skipTrigger;
		public String message;
		public List<Tag> highlights = new ArrayList<Tutorial.Tag>();
		public boolean canRepeat = true;
		
		public Action addHighlight(Tag highlight) {
			highlights.add(highlight);
			return this;
		}

		public Action dontRepeat() {
			canRepeat = false;
			return this;
		}

		public Action setSkip(Trigger skipTrigger) {
			this.skipTrigger = skipTrigger;
			return this;
		}
	}
	
	/** Currently not supported */
	protected class Indicator {
		public String name;
		public float x, y, width, height;
		public Align align = Align.Center;
		public int color;
	}
}