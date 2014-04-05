package tuxkids.tuxblocks.core.student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tuxkids.tuxblocks.core.solve.blocks.Equation;
import tuxkids.tuxblocks.core.solve.blocks.EquationGenerator.EGenerator;


public class EquationTree {

	private List<EquationTreeNode> equationNodes = new ArrayList<EquationTreeNode>();
	private EquationTreeNode root;

	public EquationTree() {
		root = new EquationTreeNode(null) {
			@Override
			public Equation equation() {
				throw new RuntimeException("Root node is just a place holder.  It should never actually be called");
			}
		};

	}

	public EquationTreeNode node(int index) {
		return equationNodes.get(index);
	}

	public int size() {
		return equationNodes.size();
	}

	public boolean unlocked(int i) {
		return node(i).isUnlocked();
	}

	public List<Float> confidences() {
		List<Float> retVal = new ArrayList<Float>();
		for(EquationTreeNode node:equationNodes) {
			retVal.add(node.confidence());
		}
		return retVal;
	}

	public Equation equation(int i) {
		return node(i).equation();
	}

	public EquationTreeNode randomWeightedUnlockedNode(Random rand) {
		
		float totalWeights = 0.0f;
		for(EquationTreeNode node: equationNodes) {
			if (node.isUnlocked()) {
				totalWeights += (1 - node.confidence);
			}
		}
		
		float randomWeight = totalWeights *= rand.nextFloat();
		
		EquationTreeNode lastNode = root;
		
		for(EquationTreeNode node: equationNodes) {
			if (node.isUnlocked()) {
				randomWeight -= (1 - node.confidence);
				if (randomWeight <= 0.0f) {
					return node;
				}
				lastNode = node;
			}
		}
		
		return lastNode;	
		
	}

	public EquationTreeNode root() {
		return root;
	}
	
	

	public EquationTreeNode addInitialNode(EGenerator generator) {
		EquationTreeNode newNode = addNode(generator, new BlankCriteria(), root);
			
		return newNode;
	}

	public EquationTreeNode addNode(EGenerator generator, Criteria c, EquationTreeNode... parents) {
		EquationTreeNode newNode = new EquationTreeNode(generator);
		
		newNode.preRequisites.put(c, Arrays.asList(parents));
		
		equationNodes.add(newNode);	//cache the tree into a linear list for easy access
		
		return newNode;
	}

	public static class EquationTreeNode {
		private EGenerator generator;

		private Map<Criteria, List<EquationTreeNode>> preRequisites = new HashMap<Criteria, List<EquationTreeNode>>();

		private float confidence;
		
		private EquationTreeNode(EGenerator generator) {
			this.generator = generator;
			this.confidence = 0;
		}
		

		public boolean isUnlocked() {
			for(Criteria c: preRequisites.keySet()) {
				if (!c.hasBeenSatisfied())		//TODO is it more efficient to pass the list here or to use the final 
												//references in the student model
					return false;
			}
			return true;
		}

		public Equation equation() {
			return generator.generate();
		}

		public float confidence() {
			return confidence;
		}

		private void setConfidence(float newConfidence) {
			this.confidence = newConfidence;
		}


	}
}

class BlankCriteria implements Criteria {
	@Override
	public boolean hasBeenSatisfied() {
		return true;
	}
}

interface Criteria {

	//TODO: this class will represent what knowledge components need to advance to
	//what levels to progress.
	boolean hasBeenSatisfied();
	//or something like that.  Perhaps be an interface with a single method.
}