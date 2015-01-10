package givenclause;

import java.util.Vector;
import index. *;
import token.Equation;
import token.Term;
import token.Variable;

public class GivenClauseAlgorithm {
	private Vector<Equation> to_select;
	private Vector<Equation> selected;
	
	public GivenClauseAlgorithm(Vector<Equation> to_select, Vector<Equation> selected) {
		this.selected = selected;
		this.to_select = to_select;
	}
	
	public boolean tautologyElimination(Equation e) {
		if (e.getFirstTerm().equals(e.getSecondTerm()))
			return true;
		return false;
	}
	
	public boolean reflection(Equation e) {
		
		if (e.getFirstTerm().equals(e.getSecondTerm()))
			return true;
		
		RobinsonAlgorithm ra = new RobinsonAlgorithm(e.getFirstTerm(), e.getSecondTerm());
		Substitution sub = ra.getSubstitution();
		
		if (!sub.isEmpty())
			return true;

		return false;
	}

	public Equation sovrapposizione(Equation first, Equation second) {
		Equation firstCopy = first.clone();


		RobinsonAlgorithm ra = new RobinsonAlgorithm(firstCopy.getFirstTerm(), firstCopy.getSecondTerm());
		Substitution sub = ra.getSubstitution();

		firstCopy.getFirstTerm().applySubstitution(sub);
		firstCopy.getSecondTerm().applySubstitution(sub);

		if (!sub.isEmpty()) {
			for (Term subterm: second.getFirstTerm().getSubTerms()) {
				if (!(subterm instanceof Variable)) {
					subterm.applySubstitution(sub);

					if (subterm.equals(firstCopy.getFirstTerm())) {

						if (firstCopy.getFirstTerm().isRPOGreater(firstCopy.getSecondTerm()) == -1) {
							Equation secondCopy = second.clone();

							secondCopy.getFirstTerm().applySubstitution(sub);
							secondCopy.getSecondTerm().applySubstitution(sub);

							if (secondCopy.getFirstTerm().isRPOGreater(secondCopy.getSecondTerm()) == -1) {
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
		
		while(!to_select.isEmpty()) {
			
			// Select the given clause
			Equation givenClause = selectGivenClause();
			
			// Reduce w.r.t. the clause in selected
			
			// Test created clauses
			
			// Add to selected
			
			// Repeate
		}
		
		return true;
	}
	
	/**
	 * Select the best clause to choose w.r.t. the cluase weight
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
}