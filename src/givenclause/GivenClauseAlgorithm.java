package givenclause;

import java.util.Vector;
import index. *;
import token.Equation;
import token.Term;
import token.Variable;

/**
 * Implementation of the given clause algorithm with the contraction and expansion rules.
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 */
public class GivenClauseAlgorithm {
	
	/**
	 * Vector of equations to select 
	 */
	private Vector<Equation> to_select;
	
	/**
	 * Vector of equations already selected
	 */
	private Vector<Equation> selected;

	/**
	 * GivenClauseAlgorithm constructor
	 * @param to_select to_select vector
	 * @param selected	selected vector
	 */
	public GivenClauseAlgorithm(Vector<Equation> to_select, Vector<Equation> selected) {
		this.selected = selected;
		this.to_select = to_select;
	}
	
	/**
	 * Implementation of the tautology elimination (contraction rule)
	 * @return true if e is a tautology, false otherwise.
	 */
	public boolean tautologyElimination(Equation e) {
		if (e.getFirstTerm().equals(e.getSecondTerm()))
			return true;
		return false;
	}

	/**
	 * Implementation of reflection (contraction rule)
	 * @return true if the equations e must be deleted, false otherwise
	 */
	public boolean reflection(Equation e) {
		
		// If the terms of e are equals, e must be deleted
		if (e.getFirstTerm().equals(e.getSecondTerm()))
			return true;
		
		// I search for a substitution to make equals the terms
		RobinsonAlgorithm ra = new RobinsonAlgorithm(e.getFirstTerm(), e.getSecondTerm());
		Substitution sub = ra.getSubstitution();
		
		// If exists a substitution, e must be deleted
		if (!sub.isEmpty())
			return true;

		return false;
	}
	
	/**
	 * Implementation of overlapping (expansion rule)
	 * @param first: first equations
	 * @param second: equations to "reduce"
	 * @return if overlapping can be used returns the generated equations, null otherwise.
	 */
	public Equation sovrapposizione(Equation first, Equation second) {
		
		// Copy of the first equations
		Equation firstCopy = first.clone();
		Equation copy = second.clone();
		
		// Search for a substitution between the terms of first equations, to make them equals
		RobinsonAlgorithm ra = new RobinsonAlgorithm(firstCopy.getFirstTerm(), firstCopy.getSecondTerm());
		Substitution sub = ra.getSubstitution();
		
		// Apply the substitution to the terms of the first equations
		firstCopy.getFirstTerm().applySubstitution(sub);
		firstCopy.getSecondTerm().applySubstitution(sub);

		if (!sub.isEmpty()) {
			
			for (Term subterm: copy.getFirstTerm().getSubTerms()) {
				
				// Subterm must not be a variable
				if (!(subterm instanceof Variable)) {
					
					// Apply the substitution to the subterm
					subterm.applySubstitution(sub);
					
					if (subterm.equals(firstCopy.getFirstTerm())) {
						
						// First ordering check
						if (firstCopy.getFirstTerm().isRPOGreater(firstCopy.getSecondTerm()) == -1) {
							Equation secondCopy = second.clone();

							secondCopy.getFirstTerm().applySubstitution(sub);
							secondCopy.getSecondTerm().applySubstitution(sub);
							
							// Second ordering check
							if (secondCopy.getFirstTerm().isRPOGreater(secondCopy.getSecondTerm()) == -1) {
								
								// Creating the new equations
								Equation result = second.clone();
								result.getFirstTerm().substituteSubterm(first.getSecondTerm(), subterm);
								result.applySubstitution(sub);
								return result;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * The implementation of the given clause algorithm
	 * @return returns true if the goal is satisfiable and 
	 * 			returns false if the goal is unsatisfiable
	 */
	public boolean givenClauseAlgorithm() {

		// Repeat the loop while the list to_select is empty
		while(!to_select.isEmpty()) {

			// Select the given clause
			Equation givenClause = selectGivenClause();

			// Apply tautology elimination
			if (tautologyElimination(givenClause)) {
				// The given clause is a tautology: I remove it from selected
				this.selected.remove(givenClause);
				// e I choose another one
				continue;
			}

			// Apply reflection
			if (reflection(givenClause)) {
				// I win :)
				// I found a contradiction
			}

			// Vector of new equations created
			Vector<Equation> newEquations = new Vector<Equation>();

			// Reduce w.r.t. the clause in selected
			for (Equation e: this.selected) {
				if (sovrapposizione(e, givenClause) != null)
					newEquations.add(sovrapposizione(e, givenClause));

				// Manca sussunzione funzionale
			}

			// Test created clauses

			// Add to selected
			//to_select.add(toTest);

			// Repeate
		}

		return true;
	}

	/**
	 * Select the best clause to choose w.r.t. the clause weight
	 * @return returns the given clause
	 */
	private Equation selectGivenClause() {
		int indexOfGivenClause = 0;

		for (Equation e: to_select) {
			if (to_select.get(indexOfGivenClause).weight() < e.weight()) 
				indexOfGivenClause = to_select.indexOf(e);
		}
		return to_select.get(indexOfGivenClause);
	}

	public boolean testEquation(Equation toTest) {

		// Reflection

		// Tautology elimination
		if (tautologyElimination(toTest))
			return true;

		// Semplificazione equazionale

		return false;
	}
}