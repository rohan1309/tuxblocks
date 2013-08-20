package tuxkids.tuxblocks.core.solve.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tuxkids.tuxblocks.core.PlayNObject;
import tuxkids.tuxblocks.core.defense.round.Level.Level1;
import tuxkids.tuxblocks.core.solve.blocks.Equation.Builder;
import tuxkids.tuxblocks.core.title.Difficulty;

public class EquationGenerator extends PlayNObject {

	private static interface Generator {
		public Equation generate();
	}

	private static class StandardGenerator implements Generator {

		private final int operations;

		public StandardGenerator(int operations) {
			this.operations = operations;
		}

		@Override
		public Equation generate() {
			return generateStandard(operations);
		}
	}

	private static class CompositeGenerator implements Generator {
		public final int minSteps, maxSteps, expressions;

		public CompositeGenerator(int minSteps, int maxSteps, int expressions) {
			this.minSteps = maxSteps;
			this.maxSteps = maxSteps;
			this.expressions = expressions;
		}

		@Override
		public Equation generate() {
			return generateComposite(minSteps, maxSteps, expressions);
		}
	}

	private static Generator gA1 = new Generator() {
		@Override
		public Equation generate() {
			return generateFormA1();
		}
	};

	private static Generator gA2 = new Generator() {
		@Override
		public Equation generate() {
			return generateFormA2();
		}
	};

	private static Generator gA3 = new Generator() {
		@Override
		public Equation generate() {
			return generateFormA3();
		}
	};

	private static Generator gB1 = new Generator() {
		@Override
		public Equation generate() {
			return generateFormB1();
		}
	};

	private static Generator gB2 = new Generator() {
		@Override
		public Equation generate() {
			return generateFormB2();
		}
	};
	
	public static Generator[][] generators = new Generator[][] {
		new Generator[] {
				new StandardGenerator(1),
		},

		new Generator[] {
				new StandardGenerator(2),
				new StandardGenerator(2),
				new CompositeGenerator(1, 1, 2),
				gA1,
		},

		new Generator[] {
				new StandardGenerator(3),
				new StandardGenerator(3),
				new CompositeGenerator(1, 2, 2),
				gA2, gA3,
		},

		new Generator[] {
				new StandardGenerator(4),
				new StandardGenerator(4),
				new CompositeGenerator(2, 3, 2),
				gA2, gA3, gB1,
		},

		new Generator[] {
				new StandardGenerator(5),
				new CompositeGenerator(3, 3, 2),
				new CompositeGenerator(1, 2, 3),
				gB1, gB2,
		},
	};
	private final static int MIN_FACTOR = 2;

	private static Random rand = new Random();

	// I really should find a better way to pass this around
	// but since everything's synchronous, it should work fine
	private static int difficulty;
	private static float percFinished;

	private static int maxFactor() {
		return (int) (10 + 5 * percFinished + difficulty);
	}

	private static int maxAdden() {
		return (int) (20 + 20 * percFinished + difficulty * 2);
	}

	private static int maxTerm() {
		return (int) (200 + 200 * percFinished + difficulty * 100);
	}

	private static int maxAnswer() {
		return (int) (30 + 15 * percFinished + 5 * difficulty);
	}

	private static int factor() {
		return factor(maxFactor());
	}
	
	private static int factor(int cap) {
		return rand(MIN_FACTOR, Math.min(cap, maxFactor()));
	}

	private static int factorSigned() {
		return rand(MIN_FACTOR, maxFactor()) * randSign();
	}

	private static int factorNot(int not) {
		int f = factor();
		if (f == not) return factorNot(not);
		return f;
	}

	private static int adden() {
		return adden(maxAdden());
	}

	private static int adden(int cap) {
		return randNonZero(Math.min(maxAdden(), cap));
	}

	public static Equation generate(int difficulty, float percFinished) {
		EquationGenerator.percFinished = percFinished;
		EquationGenerator.difficulty = difficulty;
		
		Generator[] gens = generators[difficulty];
		return gens[rand.nextInt(gens.length)].generate();
	}
	
	public static Equation generateSample(int difficulty) {
		EquationGenerator.difficulty = difficulty;
		Generator[] gens = generators[difficulty];
		return gens[rand.nextInt(gens.length)].generate();
		
	}

	public static Equation generate(Difficulty difficulty, int level) {
		return generate(difficulty.mathDifficulty, level);
	}

	/** ax + b = cx + d */
	public static Equation generateFormA1() {
		int a = factor();
		int c = factorNot(a);
		int d = adden();
		int bot = a - c;
		int b = d % bot;
		b += randNonZero(4) * bot;
		if (b == d) b += bot;

		return new Equation.Builder()
		.addLeft(new VariableBlock("x").times(a).add(b))
		.addRight(new VariableBlock("x").times(c).add(d))
		.addRight(new BlockHolder())
		.createEquation();
	}

	/** ax + (x + b) / c = d */
	public static Equation generateFormA2() {

		int a = factor();
		int d = adden();
		int c = factor(maxTerm() / Math.abs(d)); // limit c*d
		int top = c * d;
		int bot = a * c + 1;
		int b = top - (Math.round((float)top / bot) * bot);

		return new Equation.Builder()
		.addLeft(new VariableBlock("x").times(a))
		.addLeft(new VariableBlock("x").add(b).over(c))
		.addRight(new NumberBlock(d))
		.createEquation();
	}

	/** x / a + c * (x + b) = d */
	public static Equation generateFormA3() {
		int a = factor(maxTerm() / 4);
		int c = factor(maxTerm() / a / 2);
		int b = adden(maxTerm() / a / c); // limit a*c*b
		int d = randNonZero(2) * (c * a + 1) + c * b;

		return new Equation.Builder()
		.addLeft(new VariableBlock("x").over(a))
		.addLeft(new VariableBlock("x").add(b).times(c))
		.addRight(new NumberBlock(d))
		.addRight(new BlockHolder())
		.createEquation();
	}

	/** ax + x / b + cx / d = e */
	public static Equation generateFormB1() {
		int a = factor();
		int b = factor(maxTerm() / 4);
		int c = factor();
		int d = factor(maxTerm() / b / 2);
		int bot = a * b * d + d + b * c;
		int maxE = maxTerm() / a / d; // limit b*d*e
		int e = bot;
		int times = randNonZero(3);
		while (Math.abs(e) < maxE && times != 0) {
			e += bot * (int) Math.signum(e);
			times -= 1 * (int) Math.signum(e);
		}

		return new Equation.Builder()
		.addLeft(new VariableBlock("x").times(a))
		.addLeft(new VariableBlock("x").over(b))
		.addLeft(new VariableBlock("x").times(c).over(d))
		.addRight(new NumberBlock(e))
		.createEquation();
	}

	/** ax + (x + b) / c + dx / e = f */
	public static Equation generateFormB2() {
		int a = factor(maxTerm() / 4);
		int c = factor(maxTerm() / a / 2);
		int d = factor();
		int e = factor(maxTerm() / a / c); // limit c*e*a
		int f = adden(maxTerm() / c / e); // limit c*e*f

		int bot = a * c * e + e + c * d;
		int b = c * f - randNonZero(2) * bot;

		return new Equation.Builder()
		.addLeft(new VariableBlock("x").times(a))
		.addLeft(new VariableBlock("x").add(b).over(c))
		.addLeft(new VariableBlock("x").times(d).over(e))
		.addRight(new NumberBlock(f))
		.createEquation();
	}


	public static Equation generateComposite(int minSteps, int maxSteps, int expressions) {
		Builder builder = new Builder();
		int rhs = 0;
		int answer = generateAnswer() / 2;
		for (int i = 0; i < expressions; i++) {
			int steps = rand(minSteps, maxSteps);
			Equation eq = generateStandard(answer, steps);
			builder.addLeft(eq.leftSide().get(0));
			rhs += ((NumberBlock) eq.rightSide().get(0)).value();
		}
		builder.addRight(new NumberBlock(rhs));
		if (expressions < 3) {
			builder.addRight(new BlockHolder());
		}
		return builder.createEquation();
	}

	private static int generateAnswer() {
		return rand.nextInt(maxAnswer() * 2 + 1) - maxAnswer();
	}

	private enum Operation {
		Plus, Minus, Times, Over
	}

	public static Equation generateStandard(int steps) {
		return generateStandard(generateAnswer(), steps);
	}

	private static Equation generateStandard(int answer, int steps) {
		int rhs = answer;
		BaseBlock lhs = new VariableBlock("x");
		Operation lastOperation = null;
		Operation lastOperationInv = null;
		Integer lastTimes = null;
		for (int i = 0; i < steps; i++) {
			List<Integer> factors = getFactors(rhs);
			if (lastTimes != null) factors.remove(lastTimes);

			List<Operation> operations = new ArrayList<Operation>();
			for (Operation operation : Operation.values()) operations.add(operation);
			if (factors.isEmpty()) operations.remove(Operation.Over);
			if (lastOperation != null) operations.remove(lastOperation);

			int maxTimes = maxFactor();
			if (rhs != 0) maxTimes = Math.min(maxTimes, Math.abs(maxTerm() / rhs));
			if (maxTimes <= MIN_FACTOR) operations.remove(Operation.Times);

			if (operations.size() > 1 && lastOperationInv != null)
				operations.remove(lastOperationInv);

			Operation operation = operations.get(rand.nextInt(operations.size()));
			lastOperation = operation;
			lastTimes = null;

			int value;
			if (operation == Operation.Plus) {
				lastOperationInv = Operation.Minus;
				value = adden();
				lhs = lhs.add(value);
				rhs += value;
			} else if (operation == Operation.Minus) {
				lastOperationInv = Operation.Plus;
				value = adden();
				lhs = lhs.add(-value);
				rhs -= value;
			} else if (operation == Operation.Times) {
				lastOperationInv = Operation.Over;
				value = factorSigned();
				lhs = lhs.times(value);
				rhs *= value;
				lastTimes = value;
			} else {
				lastOperationInv = Operation.Times;
				value = factors.get(rand.nextInt(factors.size()));
				lhs = lhs.over(value);
				rhs /= value;
			}
		}
		return new Builder().addLeft(lhs).addRight(new NumberBlock(rhs)).createEquation();
	}


	private static int rand(int min, int max) {
		return rand.nextInt(max - min + 1) + min;
	}

	private static int randNonZero(int maxMag) {
		return rand(1, maxMag) * randSign();
	}

	private static int randSign() {
		return rand(0, 1) * 2 - 1;
	}

	public static List<Integer> getFactors(int n) {
		List<Integer> factors = new ArrayList<Integer>();
		n = Math.abs(n);
		if (n < 3) return factors;
		double max = Math.sqrt(n);
		int pIndex = 0;
		int prime = getPrime(pIndex++);
		while (prime <= max) {
			if (n % prime == 0) {
				if (n / prime == 1) break;
				factors.add(prime);
				if (!factors.contains(n / prime)) factors.add(n / prime);
				List<Integer> subprimes = getFactors(n / prime);
				for (Integer subprime : subprimes) {
					if (!factors.contains(subprime)) factors.add(subprime);
					if (!factors.contains(subprime * prime)) factors.add(subprime * prime);
				}
				break;
			} else {
				prime = getPrime(pIndex++);
			}
		}
		return factors;
	}


	private final static List<Integer> primes = new ArrayList<Integer>();
	static { primes.add(2); }
	private static int getPrime(int index) {
		while (index >= primes.size()) {
			int possible = primes.get(primes.size() - 1) + 1;
			while (!isPrimeSoFar(possible)) possible++;
			primes.add(possible);
		}

		return primes.get(index);
	}

	private static boolean isPrimeSoFar(int n) {
		double max = Math.sqrt(n);
		for (int prime : primes) {
			if (prime > max) break;
			if (n % prime == 0) return false;
		}
		return true;
	}
}
