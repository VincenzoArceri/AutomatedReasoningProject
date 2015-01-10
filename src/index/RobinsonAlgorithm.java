package index;

import java.util.HashMap;
import java.util.Vector;
import token.*;

/**
 * Implementation of the Robinson algorihtm
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 *
 */
public class RobinsonAlgorithm {
	
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
	private HashMap<Term, Term> equations;
	
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
		this.equations = new HashMap<Term, Term>();
		this.equations.put(first, second);
	}
	
	/**
	 * Returns the most general substitution to get equals the first and the second term 
	 * @return most general substitution
	 */
	public Substitution getSubstitution() {
		
		// Choose the first rule to apply
		chooseRule(first, second);
		return sub;
	}
	
	/**
	 * Implementation of the decomposition rule
	 */
	private void decomposition(Function first, Function second, Term key) {
		System.out.println("Decomposition:" + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());
		
		if (!first.getSymbol().equals(second.getSymbol())) 
			equations.clear();
		
		for (int i = 0; i < first.getArity(); i++) {
			if ((first.getArguments().get(i) instanceof Variable) && !(second.getArguments().get(i) instanceof Variable))
				equations.put(first.arguments.get(i), second.arguments.get(i));
			else if (!(first.getArguments().get(i) instanceof Variable) && (second.getArguments().get(i) instanceof Variable))
				equations.put(second.getArguments().get(i), first.getArguments().get(i));
			else
				equations.put(first.getArguments().get(i), second.getArguments().get(i));
		}
		
		equations.remove(key);
		
		chooseEquation(index = 0);
	}
	
	/**
	 * Implementation of the elimination rule
	 */
	private void elimination(Variable first, Term second) {
		System.out.println("Elimination: " + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());
		
		// Copy of the equations to make equals
		HashMap<Term, Term> copy = (HashMap<Term, Term>) equations.clone();
		
		// I'm not checking the equation "first <-- second"
		Term t = equations.remove(first);
		
		
		// Array of keys of equations
		Object[] keyTermsTmp = ((equations.keySet()).toArray());
		Term[] keyTerms = new Term[keyTermsTmp.length];
		
		for (int i = 0; i < keyTermsTmp.length; i++)
			keyTerms[i] = (Term) keyTermsTmp[i];
			
		
		// Vector of value of equations
		Vector<Term> mapTerms = new Vector<Term>();
		
		for(int i = 0; i < keyTerms.length; i++)
			mapTerms.add(this.equations.get(keyTerms[i]));
		
		equations.clear();
		
		// Apply the substitution
		for (int i = 0; i < keyTerms.length; i++) {
			mapTerms.get(i).replaceWith(first, second);
			equations.put(keyTerms[i], mapTerms.get(i));
		}
		
		// ----------------------------------
		
		keyTermsTmp = ((equations.keySet()).toArray());
		
		for (int i = 0; i < keyTermsTmp.length; i++)
			keyTerms[i] = (Term) keyTermsTmp[i];
		
		mapTerms = new Vector<Term>();
		
		for(int i = 0; i < keyTerms.length; i++)
			mapTerms.add(this.equations.get(keyTerms[i]));
		
		equations.clear();
		
		for(int i = 0; i < keyTerms.length; i++) {
			keyTerms[i].replaceWith(first, second);
			equations.put(keyTerms[i], mapTerms.get(i));
		}	
		
		equations.put(first, t);
		
		// Check if I have to choose another rule of if I have to terminate
		if (++index < equations.size()) {
			sub.put(first, second);
			chooseEquation(index);
		}
		else if (copy.equals(equations) && (index >= equations.size() - 1)) {
			sub.put(first, second);
			return;
		}
		else 
			chooseEquation(index = 0);
	}
	
	/**
	 * Implementation of the removal rule
	 */
	private void removal(Term first) {
		System.out.println("Removal" + first.toString());
		System.out.println("With the set: " + equations.toString());
		
		this.equations.remove(first);
		chooseEquation(index = 0);
	}
	
	/**
	 * Implementation of the control of occurrence rule
	 */
	private void controlOfOccurrence(Variable first, Term second) {
		System.out.println("Control of occurence:" + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());
		
		equations.clear();
		chooseEquation(index = 0);
	}
	
	/**
	 * Choose the next rule of the algorithm to apply to first and second
	 * @param first
	 * @param second
	 */
	private void chooseRule(Term first, Term second) {
		// Decomposition: first and second are Function object and they have same symbol
		if ((first instanceof Function) && (second instanceof Function) && (first.getSymbol().equals(second.getSymbol())))
			decomposition((Function) first, (Function) second, first);
		
		// Removal: first and second are Variable object and they have same symbol
		else if ((first instanceof Variable) && (second instanceof Variable) && (((Variable) first).getSymbol()).equals(((Variable) second).getSymbol())) 
			removal(first);
		// Elimination: first (or second) is a Variable object and isn't contained in second (or first)
		else if ((first instanceof Variable) && !(second.contains(first))) 
			elimination((Variable) first, second);
		else if ((second instanceof Variable) && !(first.contains(second))) 
			elimination((Variable) second, first);
		
		// // Control of occurrence: first (or second) is a Variable object and is contained in second (or first)
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
			Term selected = ((Term) ((equations.keySet()).toArray())[index]);
			
			// Choose the rule to apply
			chooseRule(selected, equations.get(selected));
		}
	}
	
	/**
	 * Returns true if exists a substitution for the terms first and second
	 * @return true if exists a substitution for the terms first and second
	 */
	public boolean existsSubstitutionFor() {
		return getSubstitution().isEmpty() ? false : true;
	}
}
