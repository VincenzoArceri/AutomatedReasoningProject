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