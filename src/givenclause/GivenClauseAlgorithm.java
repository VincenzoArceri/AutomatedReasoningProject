package givenclause;

import java.util.Set;
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
	 * Ratio for the given clause choice
	 */
	private int ratio = 4;
	
	/**
	 * Index for the ration
	 */
	private int indexForRatio = 0;
	
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
				} else if ((subterm instanceof Variable) && (((Variable) subterm).isInizialized())) {
					// Apply the substitution to the subterm
					
					((Variable) subterm).getValue().applySubstitution(sub);

					if (((Variable) subterm).getValue().equals(firstCopy.getFirstTerm())) {

						// First ordering check
						if (firstCopy.getFirstTerm().isRPOGreater(firstCopy.getSecondTerm()) == -1) {
							Equation secondCopy = second.clone();

							secondCopy.getFirstTerm().applySubstitution(sub);
							secondCopy.getSecondTerm().applySubstitution(sub);

							// Second ordering check
							if (secondCopy.getFirstTerm().isRPOGreater(secondCopy.getSecondTerm()) == -1) {

								// Creating the new equations
								Equation result = second.clone();
								result.getFirstTerm().substituteSubterm(first.getSecondTerm(), ((Variable) subterm).getValue());
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

		Equation newEquationTmp;
		
		int i = 0;
		
		// Repeat the loop while the list to_select is empty
		while(!to_select.isEmpty()) {
			
			System.out.println(i + "# iterazione " + to_select.size() + " " +  selected.size());
			// Select the given clause
			Equation givenClause = selectGivenClause();

			// Apply tautology elimination
			if (tautologyElimination(givenClause)) {
				System.out.println("TAUTOLOGIA");
				// The given clause is a tautology: I remove it from selected
				this.to_select.remove(givenClause);
				// and I choose another one
				continue;
			}
			
			// Sussunzione funzionale e semplificazione equazionale
			for (Equation e: this.selected) {
				if (sussunzioneFunzionale(e, givenClause)) {
					System.out.println("SUSSUNZIONE");
					continue;
				}
				
				newEquationTmp = semplificazione_equazionale(e, givenClause);
				
				if (newEquationTmp != null) {
					to_select.add(newEquationTmp);
					selected.remove(e);
				}
			}
			
			// Apply reflection
			if (reflection(givenClause)) {
				System.out.println("RIFLESSIONE");
				System.err.println("Finito.");
			}

			// Vector of new equations created
			Vector<Equation> newEquations = new Vector<Equation>();
			
			// Reduce w.r.t. the clause in selected
			for (Equation e: this.selected) {

				// Sovrapposizione
				if (sovrapposizione(e, givenClause) != null) {
					System.out.println("SOVRAPPOSIZIONE");
					newEquations.add(sovrapposizione(e, givenClause));
				}
			}

			// Test created clauses
			System.out.println("Ho generato " + newEquations.size() + " equazioni");
			
			for (Equation e : newEquations) {
				// Apply tautology elimination
				if (tautologyElimination(e)) {
					// The given clause is a tautology: I remove it from selected
					this.selected.remove(e);
					// e I choose another one
					continue;
				}

				// Sussunzione funzionale e semplificazione equazionale
				for (Equation eq: this.selected) {
					if (sussunzioneFunzionale(eq, e))
						continue;

					newEquationTmp = semplificazione_equazionale(eq, e);

					if (newEquationTmp != null) {
						to_select.add(newEquationTmp);
						selected.remove(eq);
					}
				}

				// Apply reflection
				if (reflection(e)) {
					System.err.println("Finito.");
				}
			}
			// Add to selected
			if (i >=  100)
				break;
			i++;
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

		if (indexForRatio < ratio) {
			for (Equation e: to_select) 
				if (to_select.get(indexOfGivenClause).weight() < e.weight()) 
					indexOfGivenClause = to_select.indexOf(e);
			
			indexForRatio++;
			return to_select.get(indexOfGivenClause); 
		} else {
			indexForRatio = 0;
			return to_select.get(to_select.size() -1);
		}
	}


	//se ritorna un'equazione != null, rimuovo le equazioni passate nel metodo
	public Equation semplificazione_equazionale(Equation first, Equation second) {

		Equation firstCopy = first.clone();
		Equation secondCopy = second.clone();

		for (Term subterm: second.getFirstTerm().getSubTerms()) {

			RobinsonAlgorithm ra = new RobinsonAlgorithm(subterm, firstCopy.getFirstTerm());
			Substitution sub = ra.getSubstitution();
			Term tempSub = subterm.clone();

			if(!sub.isEmpty())
			{
				tempSub.applySubstitution(sub);
				if(tempSub.equals(subterm))
				{
					firstCopy.getFirstTerm().applySubstitution(sub);
					firstCopy.getSecondTerm().applySubstitution(sub);
					if(firstCopy.getFirstTerm().isRPOGreater(firstCopy.getSecondTerm()) == -1)
					{
						Equation result=second.clone();
						result.getFirstTerm().substituteSubterm(firstCopy.getSecondTerm(), subterm);
						return result;
					}
				}
			}
			else if (sub.isEmpty() && subterm.equals(firstCopy.getFirstTerm()))
			{
				if(firstCopy.getFirstTerm().isRPOGreater(firstCopy.getSecondTerm()) == -1)
				{
					Equation result=second.clone();
					result.getFirstTerm().substituteSubterm(firstCopy.getSecondTerm(), subterm);
					return result;
				}
			}
		}
		return null;
	}

	//true se il metodo è stato applicato, false altrimenti. Con true devo eliminare il secondo parametro passato
	public boolean sussunzioneFunzionale(Equation first, Equation second){

		//se la funzione non sono uguali
		if( !(second.getFirstTerm().getSymbol().equals(second.getSecondTerm().getSymbol())))
			return false;

		//se il numero di parametri non coincide
		if(!((first.getFirstTerm().getSubTerms().size() == second.getFirstTerm().getSubTerms().size()) &&
				(first.getSecondTerm().getSubTerms().size() == second.getSecondTerm().getSubTerms().size())) )
			return false;
		
		RobinsonAlgorithm ra = new RobinsonAlgorithm(first.getFirstTerm(), second.getFirstTerm().getSubTerms().firstElement());
		Substitution sub1 = ra.getSubstitution();

		RobinsonAlgorithm ra1 = new RobinsonAlgorithm(first.getSecondTerm(), second.getSecondTerm().getSubTerms().firstElement());
		Substitution sub2 = ra1.getSubstitution();

		Equation firstCopy = first.clone();
		Equation secondCopy = second.clone();

		Set<Term> s1 = sub1.keySet();
		//Set <Variable> s2 = sub2.keySet();

		Substitution union = (Substitution) sub2.clone();

		for (Term var: s1) {
			if (sub2.containsKey(var)) {
				if(!(sub2.get(var).equals(sub1.get(var))))      //se è settata un'inizializzazione diversa per la stessa variabile nelle due sostituzioni.
					return false;
			}
			else {
				union.put(var, sub1.get(var));
			}
		}

		firstCopy.applySubstitution(union);
		if(!((firstCopy.getFirstTerm().equals(secondCopy.getFirstTerm().getSubTerms())) && (firstCopy.getSecondTerm().equals(secondCopy.getSecondTerm().getSubTerms()))) )
			return false;

		//secondCopy.getFirstTerm().substituteSubterm(firstCopy.getFirstTerm(), secondCopy.getFirstTerm().getSubTerms().elementAt(0));
		//secondCopy.getSecondTerm().substituteSubterm(firstCopy.getSecondTerm(), secondCopy.getSecondTerm().getSubTerms().elementAt(0));

		return true;               
	}

}