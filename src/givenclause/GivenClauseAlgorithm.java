package givenclause;

import java.util.Set;
import java.util.Vector;
import index. *;
import token.Equation;
import token.Function;
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
	 * Index for the ratio
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
	 * The implementation of the given clause algorithm
	 * @return returns true if the goal is satisfiable and
	 * 			returns false if the goal is unsatisfiable
	 */
	public boolean givenClauseAlgorithm() {

		//Equation newEquationTmp;
		Vector<Equation> toDeleteFromSelectFinal = new Vector<Equation>();

		int i = 0;

		// Repeat the loop while the list to_select is empty
		while(!to_select.isEmpty()) {
			
			
			i++;
			Equation givenClause;
			System.gc();

			System.out.println(i + "# iterazione dell'algoritmo della clausola data: " + this.to_select.size() + " " + this.selected.size());

			System.out.println("To select: ");
			printToSelect();

			System.out.println("Selected: ");
			printSelected();
			
			givenClause = selectGivenClause();

			to_select.remove(givenClause); // Selected: removed from the equations to select

			System.out.println("				Ho selezionato la seguente clausola data: " + givenClause.toString());

			// Apply tautology elimination
			if (tautologyElimination(givenClause)) {
				System.out.println("	Applying tautology");

				// and I choose another one
				continue;
			}

			// Trying to subsume the given clause with the clauses in selected
			System.out.println("	Applying functional subsumption");

			boolean flag = false;
			
			for (Equation e: this.selected) {
				if (sussunzioneFunzionale(e, givenClause)) {

					// Given clause is subsumed by "e"
					flag = true;
					break;
				}
			}
			
			if (flag)
				continue;

			Vector<Equation> toRemove = new Vector<Equation>();

			// Trying to simply the given clause with the clauses in selected
			System.out.println("	Applying equational simplification");

			for(Equation equation: toRemove)
				selected.remove(equation);

			
			// Apply reflection
			if (reflection(givenClause)) {
				System.out.println("	Applying reflection");
				System.err.println("Finito.");
				System.exit(0);
			}

			// Add the given clause to the selected equation list
			selected.add(givenClause);

			/**
			 * Second phase
			 */

			// Vector of new equations created
			Vector<Equation> newEquations = new Vector<Equation>();

			// Vector of equations to remove from selected
			Vector<Equation> toDelete = new Vector<Equation>();

			// Reduce w.r.t. the clause in selected
			for (Equation e: this.selected) {
				Equation newEquationsT = sovrapposizione(givenClause, e);

				// Sovrapposizione
				if (newEquationsT != null) {
					System.out.println("	Applying overlap");
					newEquations.add(newEquationsT);
				}
			}
			
			
			// Test created clauses
			System.out.println("Ho generato " + newEquations.size() + " equazioni");
			
			for (Equation eq: newEquations) 
				System.out.println("				Generata: " +eq);
			
			/**
			 * Third phase
			 */

			toDelete = new Vector<Equation>();

			for (Equation target : newEquations) {

				// Apply tautology elimination
				if (tautologyElimination(target)) {
					// The given clause is a tautology: I remove it from selected
					toDelete.add(target);
					// e I choose another one
					continue;
				}
			}
			
			for (Equation eq: toDelete)
				newEquations.remove(eq);
			
			toDelete = new Vector<Equation>();

			for (Equation target: newEquations) {

				// Sussunzione funzionale
				for (Equation eq: this.selected) {
					if (sussunzioneFunzionale(eq, target))
						toDelete.add(target);
				}
			}

			for (Equation eq: toDelete)
				newEquations.remove(eq);
			 
			toDelete = new Vector<Equation>();
			Vector<Equation> toDeleteFromSelected = new Vector<Equation>();

			/*
			for (Equation target: newEquations) {
				for(Equation eq: this.selected) {

					newEquationTmp = semplificazione_equazionale(eq, target);

					if (newEquationTmp != null) {
						toDelete.add(target);
						toDeleteFromSelected.add(eq);
						to_select.add(newEquationTmp);
					}
				}
			}*/

			for (Equation eq: toDeleteFromSelected)
				selected.remove(eq);

			toDelete = new Vector<Equation>();
			toDeleteFromSelected = new Vector<Equation>();

			for (Equation target: newEquations) {
				// Apply reflection
				if (reflection(target)) {
					System.err.println("Finito.");
					System.exit(0);
				}
			}

			for (Equation alpha: newEquations)
				to_select.add(alpha);

		//	for (Equation old: toDeleteFromSelected)
			//	selected.remove(old);
			
			orderingEquations();

			// Repeate
			if (i == 100)
				break;
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
			System.out.println("Ho scelto l'equazione in base al suo ratio");
			for (Equation e: to_select)
				if (to_select.get(indexOfGivenClause).weight() > e.weight())
					indexOfGivenClause = to_select.indexOf(e);

			indexForRatio++;
			return to_select.get(indexOfGivenClause);
		} else {
			System.out.println("Ho scelto l'ultima equazione entrata nella lista");
			indexForRatio = 0;
			return to_select.get(0);
		}
	}

	public Equation semplificazione_equazionale(Equation first, Equation second) {

		//Equation firstCopy = first.clone();
		//Equation secondCopy = second.clone();

		for (Term subterm: second.getFirstTerm().getSubTerms()) {

			Equation firstCopy = first.clone();

			RobinsonAlgorithm ra = new RobinsonAlgorithm(subterm, firstCopy.getFirstTerm());
			Substitution sub = ra.getSubstitution();

			//System.out.println( ((Function) (second.getFirstTerm())).getSubTerms());

			if(!sub.isEmpty())
			{

				firstCopy.getFirstTerm().applySubstitution(sub);

				//System.out.println(subterm + "First copy:" + firstCopy);

				if (firstCopy.getFirstTerm().equals(subterm))
				{
					firstCopy.getSecondTerm().applySubstitution(sub);
					//System.out.println("First copy" + firstCopy);

					if (firstCopy.getFirstTerm().isRPOGreater(firstCopy.getSecondTerm()) == 1)
					{
						//System.out.println("Maggiore");

						Equation result = second.clone();
						//System.out.println(firstCopy.getSecondTerm()+ " " + subterm);

						Term temp;

						if ((temp = result.getFirstTerm().substituteSubterm(firstCopy.getSecondTerm(), subterm)) != null) {
							result.setFirstTerm(temp);
						}

						return result;
					}
				}
			}
			else if (sub.isEmpty() && subterm.equals(firstCopy.getFirstTerm()))
			{
				if(firstCopy.getFirstTerm().isRPOGreater(firstCopy.getSecondTerm()) == 1)
				{
					Equation result=second.clone();
					result.getFirstTerm().substituteSubterm(firstCopy.getSecondTerm(), subterm);
					return result;
				}
			}
		}
		return null;
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
		
	public boolean sussunzioneFunzionale(Equation first, Equation second){

		int sentinella = 0;

		if (!(second.getFirstTerm() instanceof Function) || !(second.getSecondTerm() instanceof Function)) {
			//System.out.println("non funzioni");
			return false;
		}

		//se la funzione non sono uguali (gia' controllato di avere funzioni)
		if( !(second.getFirstTerm().getSymbol().equals(second.getSecondTerm().getSymbol()))){
			//System.out.println("Nomi diversi");
			return false;
		}

		//se il numero di parametri non coincide
		//if(!((2 == second.getFirstTerm().getSubTerms().size()) && (2 == second.getSecondTerm().getSubTerms().size())) )
		if(!(2 == second.getFirstTerm().getSubTerms().size()))   //2 perche' nei sottotermini inseriamo la funzione seguita dagli argomenti.
		{
			//System.out.println("Parametri errati");
			return false;
		}

		for (int i = 0; i < second.getFirstTerm().getSubTerms().size(); i++) {

			RobinsonAlgorithm ra = new RobinsonAlgorithm(first.getFirstTerm(), second.getFirstTerm().getSubTerms().elementAt(i));
			Substitution sub1 = ra.getSubstitution(); // sostituzione con tutta la prima parte della seconda equazione.

			RobinsonAlgorithm ra1 = new RobinsonAlgorithm(first.getSecondTerm(), second.getSecondTerm().getSubTerms().elementAt(i));
			Substitution sub2 = ra1.getSubstitution();

			Equation firstCopy = first.clone();
			Equation secondCopy = second.clone();

			Set<Term> s1 = sub1.keySet();
			Set<Term> s2 = sub2.keySet();

			Substitution union = (Substitution) sub2.clone();

			boolean finish = false;

			for (Term var: s1) {  //scorro la prima sostituzione

				for(Term var2: s2)   //scorro la seconda sostituzione
				{
					//System.out.println("var2: "+var2.toString());
					if(var2.equals(var)) //chiavi uguali
					{
						sentinella = 1;
						
						if (!(sub2.get(var2)).equals(sub1.get(var))) {   //mappano in elementi diversi
							finish = true;
							continue;
						}
					}
				}

				if (finish)
					continue;

				if(sentinella == 0) {  //var non e' mappato in sost2
					union.put(var, sub1.get(var));
				}

				sentinella = 0;
			}

			if (finish)
				continue;
				
			//System.out.println("Union: "+union.toString());
			firstCopy.getFirstTerm().applySubstitution(union);
			firstCopy.getSecondTerm().applySubstitution(union);
			
			//NON FUNZIONA perchè non riesce a comparare perchè il getValue() e' presente solo nelle variabili e non in funzione e costante quindi non posso fare il cast altrimenti
			//nel caso di funzione castata a variabile da errore.

			if(!((firstCopy.getFirstTerm().equals(secondCopy.getFirstTerm().getSubTerms().elementAt(i))) &&
					(firstCopy.getSecondTerm().equals(secondCopy.getSecondTerm().getSubTerms().elementAt(i)))) )
				continue;

			return true;
		}

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
		Equation firstCopy = first.clone(); 			// l  = r
		Equation secondCopy = second.clone();			// P[s] = q

		//if (firstCopy.equals(secondCopy)) {
			for (Term subterm: secondCopy.getFirstTerm().getSubTerms())
				subterm.changeVariable();

			for (Term subterm: secondCopy.getSecondTerm().getSubTerms())
				subterm.changeVariable();
		
		
		System.out.println("--_>" + firstCopy + "  " + secondCopy); 
		
		// Copies for the final equation
		Equation firstCopyForFinal = first.clone();
		Equation secondCopyForFinal = second.clone();

		Equation secondCopyForCycle = secondCopy.clone();

		// Search for the subterm 
		for (Term subterm : secondCopyForCycle.getFirstTerm().getSubTerms()) {
			
			// Working with the original second equation
			secondCopy = secondCopyForCycle.clone();

			// Subterm isn't a Variable
			if (!(subterm instanceof Variable)) {

				// Create a copy of subterm
				Term subtermCopy = subterm.clone();

				// Search for 
				RobinsonAlgorithm ra = new RobinsonAlgorithm(subtermCopy, firstCopy.getFirstTerm());
				Substitution sub = ra.getSubstitution(); // Getting the sigma 
				
				if (!sub.isEmpty()) {		// Case 1 : there's a substitution
					
					firstCopy.applySubstitution(sub); 	// Now I have l sigma and r sigma
					secondCopy.getFirstTerm().applySubstitution(sub); 	// Now I have p sigma and q sigma
					
					if (firstCopy.getSecondTerm().isRPOGreater(firstCopy.getFirstTerm()) == -1) 
						if (secondCopy.getSecondTerm().isRPOGreater(secondCopy.getFirstTerm()) == -1) {

							Term all = secondCopy.getFirstTerm().substituteSubterm(firstCopyForFinal.getSecondTerm(), firstCopyForFinal.getFirstTerm());
														
							if (all == null) {
								
								secondCopy.applySubstitution(sub);
								
								System.out.println(secondCopy);
								
								if (secondCopy.isNegative()) {
									secondCopy.setGoal();
								}
								
								return (secondCopy);
							} else { // Se arrivo qua devo rimpiazzare tutto 
								secondCopy.applySubstitution(sub);

								Equation newEquation = new Equation(all, secondCopy.getSecondTerm(), secondCopy.isNegative());
								
//								newEquation.applySubstitution(sub);
								
								if (newEquation.isNegative()) {
									newEquation.setGoal();
									newEquation.applySubstitution(sub);
								}
								return newEquation;
							}

						}
				} else if ((sub.isEmpty()) && (firstCopy.getFirstTerm().equals(subterm))) { // Case 2: there isn't a substitution but the terms are equals

					if (firstCopy.getSecondTerm().isRPOGreater(firstCopy.getFirstTerm()) == -1) 
						if (secondCopy.getSecondTerm().isRPOGreater(secondCopy.getFirstTerm()) == -1) {
							
							Term all = secondCopy.getFirstTerm().substituteSubterm(firstCopy.getSecondTerm(), firstCopy.getFirstTerm());

							if (all == null) {
								Equation newEquation = new Equation(secondCopy.getFirstTerm(), secondCopy.getSecondTerm(), secondCopyForFinal.isNegative());
								if (newEquation.isNegative()) 
									newEquation.setGoal();
									return newEquation;
							} else {
								Equation newEquationT = new Equation(all, secondCopyForFinal.getSecondTerm(), secondCopyForFinal.isNegative());
								if (newEquationT.isNegative()) 
									newEquationT.setGoal();
								return (newEquationT);
							}
						}
				}
			} else if ((subterm instanceof Variable) && ((Variable) subterm).isInizialized()) {

				// Create a copy of subterm
				Term subtermCopy = ((Variable) subterm).getValue().clone();

				// Search for 
				RobinsonAlgorithm ra = new RobinsonAlgorithm(subtermCopy, firstCopy.getFirstTerm());
				Substitution sub = ra.getSubstitution(); // Getting the sigma 
				
				if (!sub.isEmpty()) {		// Case 1 : there's a substitution
					
					firstCopy.applySubstitution(sub); 	// Now I have l sigma and r sigma
					secondCopy.getFirstTerm().applySubstitution(sub); 	// Now I have p sigma and q sigma
					
					if (firstCopy.getSecondTerm().isRPOGreater(firstCopy.getFirstTerm()) == -1) 
						if (secondCopy.getSecondTerm().isRPOGreater(secondCopy.getFirstTerm()) == -1) {

							Term all = secondCopy.getFirstTerm().substituteSubterm(firstCopyForFinal.getSecondTerm(), firstCopyForFinal.getFirstTerm());

							if (all == null) {
								System.out.println("Primo caso");
								secondCopy.applySubstitution(sub);
								
								if (secondCopy.isNegative())
									secondCopy.setGoal();
									
								return (secondCopy);
							} else { // Se arrivo qua devo rimpiazzare tutto 
								System.out.println("Secondo caso");
								Equation newEquation = new Equation(all, secondCopyForFinal.getSecondTerm(), secondCopyForFinal.isNegative());
								newEquation.applySubstitution(sub);
								if (newEquation.isNegative()) 
									newEquation.setGoal();
								return (newEquation);
							}

						}
				} else if ((sub.isEmpty()) && (firstCopy.getFirstTerm().equals(subterm))) { // Case 2: there isn't a substitution but the terms are equals

					if (firstCopy.getSecondTerm().isRPOGreater(firstCopy.getFirstTerm()) == -1) 
						if (secondCopy.getSecondTerm().isRPOGreater(secondCopy.getFirstTerm()) == -1) {
							System.out.println("Terzo caso");

							Term all = secondCopy.getFirstTerm().substituteSubterm(firstCopy.getSecondTerm(), firstCopy.getFirstTerm());

							if (all == null) {
								Equation newEquation = (new Equation(secondCopy.getFirstTerm(), secondCopy.getSecondTerm(), secondCopyForFinal.isNegative()));
								if (newEquation.isNegative()) 
									newEquation.setGoal();
								return newEquation;
							} else {
								Equation newEquation = new Equation(all, secondCopyForFinal.getSecondTerm(), secondCopyForFinal.isNegative());
								if (newEquation.isNegative()) 
									newEquation.setGoal();
								return (newEquation);
							}
						}
				}
			}
		}
		return null;
	}
	
	public void printSelected() {
		for(Equation e: selected)
			System.out.println("		" + e.toString());
	}

	public void printToSelect() {
		for(Equation e: to_select) 
			System.out.println("		" + e.toString());

	}
	
	public void orderingEquations() {
		
		Vector<Equation> toAdd = new Vector<Equation>();
		Vector<Equation> toDelete = new Vector<Equation>();
		
		for (Equation equation: to_select) {
			if (equation.getSecondTerm().isRPOGreater(equation.getFirstTerm()) == 1) {
				Equation newEquation = new Equation(equation.getSecondTerm(), equation.getFirstTerm(), equation.isNegative());
				toAdd.add(newEquation);
				toDelete.add(equation);
			}
		}
		
		for (Equation e: toDelete)
			removeFromEquations(e, to_select);
		
		for (Equation e: toAdd)
			to_select.add(e);
		
		toAdd = new Vector<Equation>();
		toDelete = new Vector<Equation>();
		
		for (Equation equation: selected) {
			if (equation.getSecondTerm().isRPOGreater(equation.getFirstTerm()) == 1) {
				Equation newEquation = new Equation(equation.getSecondTerm(), equation.getFirstTerm(), false);
				toAdd.add(newEquation);
				toDelete.add(equation);
			}
		}
		
		for (Equation e: toDelete)
			removeFromEquations(e, selected);
		
		for (Equation e: toAdd)
			to_select.add(e);
	}
	
	public void removeFromEquations(Equation equationToRemove, Vector<Equation> equations) {
		int flag = 0;

		for (int i = 0; i < equations.size(); i++) {
			if (equations.get(i).equals(equationToRemove)) {
				flag = i;
				break;
			}
		}

		equations.remove(flag);
	}

	/*
	public Vector<Equation> combining(Equation e1, Equation e2){
		Vector<Equation> result=new Vector<Equation>();
		result.add(e1);
		result.add(e2);
		result.add(e1);
		result.add(new Equation(e2.getSecondTerm(), e2.getFirstTerm(), e2.isNegative()));
		result.add(new Equation(e1.getSecondTerm(), e1.getFirstTerm(), e1.isNegative()));
		result.add(e2);
		result.add(new Equation(e1.getSecondTerm(), e1.getFirstTerm(), e1.isNegative()));
		result.add(new Equation(e2.getSecondTerm(), e2.getFirstTerm(), e2.isNegative()));
		return result;
	}*/
}