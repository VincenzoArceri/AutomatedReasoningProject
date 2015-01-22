package index;

import java.util.Vector;
import com.rits.cloning.Cloner;
import token.*;

/**
 * Implementation of the Robinson algorihtm
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 *
 */
public class RobinsonAlgorithm {

	/**
	 * Cloner
	 */
	private Cloner cloner;

	/**
	 * First term 
	 */
	private Term first;

	/**
	 * Second term
	 */
	private Term second;

	/**
	 * Couple of terms to get equals
	 */
	private Vector<Equation> equations;

	/**
	 * Substitutions
	 */
	private Substitution sub = new Substitution();

	private int index = 0;

	/**
	 * Constructor of RobinsonAlgorithm
	 */
	public RobinsonAlgorithm(Term first, Term second) {
		this.first = first;
		this.second = second;
		this.cloner = new Cloner();
		this.equations = new Vector<Equation>();
		this.equations.add(new Equation(first, second, false));
	}

	/**
	 * Returns the most general substitution to get equals the first and the second term 
	 * @return most general substitution
	 */
	public Substitution getSubstitution() {

		// Choose the first rule to apply
		chooseRule(first, second);
		
		// Ordino il senso delle equazioni
		makeItRight();
		
		// Controllo se c'è altro da fare
		if (!equations.isEmpty()){
			boolean flag = false;

			for (Equation e: equations)
				if (!(e.getFirstTerm() instanceof Variable)) {
					flag = true;
					break;
				}

			if (flag)
				chooseRule(equations.get(0).getFirstTerm(), equations.get(0).getSecondTerm());

			for (Equation e: equations) 
				sub.put(e.getFirstTerm(), e.getSecondTerm());
		}

		return sub;
	}

	/**
	 * Implementation of the decomposition rule
	 */
	private void decomposition(Function first, Function second, Term key) {
		System.out.println("Decomposition:" + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());

		for (int i = 0; i < first.getArity(); i++) {
			if ((first.getArguments().get(i) instanceof Variable) && !(second.getArguments().get(i) instanceof Variable))
				equations.add(new Equation(first.arguments.get(i), second.arguments.get(i), false));
			else if (!(first.getArguments().get(i) instanceof Variable) && (second.getArguments().get(i) instanceof Variable))
				equations.add(new Equation(second.getArguments().get(i), first.getArguments().get(i), false));
			else
				equations.add(new Equation(first.getArguments().get(i), second.getArguments().get(i), false));
		}

		removeFromEquations(new Equation(first, second, false), equations);

		chooseEquation(index = 0);
	}

	/**
	 * Implementation of the elimination rule
	 */
	private void elimination(Variable first, Term second) {
		System.out.println("Elimination: " + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());

		// Copy of the equations to make equals
		Vector<Equation> equationsCopy = (Vector<Equation>) cloner.deepClone(equations);

		// I'm not checking the equation "first <-- second"
		Equation termToApply = new Equation(first, second, false);
		removeFromEquations(termToApply, equationsCopy);

		Substitution simpleSubstitution = new Substitution();
		simpleSubstitution.put(termToApply.getFirstTerm(), termToApply.getSecondTerm());

		for (Equation eq : equationsCopy)
			eq.applySubstitution(simpleSubstitution);

		equationsCopy.add(termToApply); // Quello rimosso

		System.out.println(equations);
		System.out.println(equationsCopy);

		// Check if I have to choose another rule of if I have to terminate

		boolean found = true;
		boolean find = false;

		for (int i = 0; i < equations.size(); i++) {
			for (int j = 0; j < equations.size(); j++) {
				if (equations.get(i).equals(equationsCopy.get(j))) {
					find = true;
					break;
				}
			}

			if (find)
				find = false;
			else {
				found = false;	
				break;
			}
		}



		if (++index < equations.size()) {
			//sub.put(first, second);
			equations = cloner.deepClone(equationsCopy);
			chooseEquation(index);
		} else if (found && (index >= equations.size() - 1)) {
			//sub.put(first, second);
			return;
		} else {
			equations = cloner.deepClone(equationsCopy);
			chooseEquation(index = 0);
		}
	}

	/**
	 * Implementation of the removal rule
	 */
	private void removal(Term first) {
		System.out.println("Removal" + first.toString());
		System.out.println("With the set: " + equations.toString());

		removeFromEquations(new Equation(first, first, false), equations);
		chooseEquation(index = 0);
	}

	/**
	 * Implementation of the control of occurrence rule
	 */
	private void controlOfOccurrence(Variable first, Term second) {
		System.out.println("Control of occurence:" + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());

		clearAll();
		chooseEquation(index = 0);
	}

	/**
	 * Choose the next rule of the algorithm to apply to first and second
	 * @param first
	 * @param second
	 */
	private void chooseRule(Term first, Term second) {

		if ((first.isGround()) && (second.isGround()) && (!first.equals(second))) {
			clearAll();
			chooseEquation(index);
		}

		// Decomposition: first and second are Function object and they have same symbol
		else if ((first instanceof Function) && (second instanceof Function) && (first.getSymbol().equals(second.getSymbol())))
			decomposition((Function) first, (Function) second, first);

		// If there're two functions with different symbols, the substitution don't exists
		else if ((first instanceof Function) && (second instanceof Function) && (!first.getSymbol().equals(second.getSymbol()))) {
			clearAll();
			return;
		}

		// Removal: first and second are Variable object and they have same symbol
		else if ((first instanceof Variable) && (second instanceof Variable) && (((Variable) first).getSymbol()).equals(((Variable) second).getSymbol())) 
			removal(first);

		else if (first.equals(second)) 
			removal(first);

		// Elimination: first (or second) is a Variable object and isn't contained in second (or first)
		else if ((first instanceof Variable) && !(second.contains(first))) 
			elimination((Variable) first, second);

		else if ((second instanceof Variable) && !(first.contains(second))) {
			// Giro l'equazione
			removeFromEquations(new Equation(first, second, false), equations);
			equations.add(new Equation(second, first, false));

			elimination((Variable) second, first);
		}

		// Control of occurrence: first (or second) is a Variable object and is contained in second (or first)
		else if ((first instanceof Variable) && (second.contains(first)))
			controlOfOccurrence((Variable) first, second);

		else if ((second instanceof Variable) && (first.contains(second)))
			controlOfOccurrence((Variable) second, first);
	}

	/**
	 * Choose the next equations to unify
	 * @param index
	 */
	private void chooseEquation(int index) {
		// If equations is empty means that the two terms can't be unified
		if (equations.isEmpty())
			return;
		else {
			// Get an element from equations
			Equation selected = ((Equation) equations.get(index));

			// Choose the rule to apply
			chooseRule(selected.getFirstTerm(), selected.getSecondTerm());
		}
	}

	private void removeFromEquations(Equation equationToRemove, Vector<Equation> equations) {
		int flag = 0;

		for (int i = 0; i < equations.size(); i++) {
			if (equations.get(i).equals(equationToRemove)) {
				flag = i;
				break;
			}
		}

		equations.remove(flag);
	}
	
	/**
	 * Set to empty sub and equations
	 */
	private void clearAll() {
		equations.clear();
		sub.clear();
	}
	
	/**
	 * Functions that make the sense of the substitution right
	 */
	private void makeItRight() {
		
		Vector<Equation> toAdd = new Vector<Equation>();
		
		for (int i = 0; i < equations.size(); i++)
			for(int j = 0; j < equations.size(); j++) {
				if (equations.get(i).getSecondTerm().equals(equations.get(j).getFirstTerm())) {
					Term first = equations.get(j).getFirstTerm();
					Term second = equations.get(j).getSecondTerm();
					removeFromEquations(new Equation(first, second, false), equations);
					toAdd.add(new Equation(second, first, false));
				}
			}
		
		for (Equation e: toAdd)
			equations.add(e);
	}
}
