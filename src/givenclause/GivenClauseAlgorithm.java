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

		// Copies for the final equation
		Equation firstCopyForFinal = first.clone();
		Equation secondCopyForFinal = second.clone();

		// Copy of the first equations
		Equation firstCopy = first.clone(); 			// l  = r
		Equation secondCopy = second.clone();			// P[s] = q

		Equation secondCopyForCycle = second.clone();

		// Search for the subterm s
		for (Term subterm : secondCopyForCycle.getFirstTerm().getSubTerms()) {
			
			// Working with the original second equation
			secondCopy = secondCopyForCycle.clone();

			// Subterm isn't a Variable
			if (!(subterm instanceof Variable)) {
				
				// Create a copy of subterm
				Term subtermCopy = subterm.clone();
				
				// Search for 
				RobinsonAlgorithm ra = new RobinsonAlgorithm(firstCopy.getFirstTerm(), subtermCopy);
				Substitution sub = ra.getSubstitution(); // Getting the sigma 

				System.out.println("Trovata la sostituzione fra " + firstCopy.getFirstTerm() + " e " +  subtermCopy);
				System.out.println("Sostituzione" + sub);

				firstCopy.applySubstitution(sub); 	// Now I have l sigma and r sigma
				secondCopy.applySubstitution(sub); 	// Now I have p sigma and q sigma

				if (!sub.isEmpty()) {		// Case 1 : there's a substitution

					if (firstCopy.getSecondTerm().isRPOGreater(firstCopy.getFirstTerm()) == -1) 
						if (secondCopy.getSecondTerm().isRPOGreater(secondCopy.getFirstTerm()) == -1) {
							System.out.println("Dentro");

							//subtermCopy.applySubstitution(sub);

							System.out.println("Cerco" + subterm + " e sostituisco con " + firstCopyForFinal.getSecondTerm());
							System.out.println("In" + secondCopyForFinal);

							Term all;

							if ((all = secondCopyForFinal.getFirstTerm().substituteSubterm(firstCopyForFinal.getSecondTerm(), subterm)) == null) {
								secondCopyForFinal.applySubstitution(sub);
								return secondCopyForFinal;
							} else { // Se arrivo qua devo rimpiazzare tutto 
								Equation newEquation = new Equation(all, secondCopyForFinal.getSecondTerm(), false);
								newEquation.applySubstitution(sub);
								return newEquation;
							}

						}
				} else if ((sub.isEmpty()) && (firstCopy.getFirstTerm().equals(subterm))) { // Case 2: there isn't a substitution but the terms are equals

					if (firstCopy.getSecondTerm().isRPOGreater(firstCopy.getFirstTerm()) == -1) 
						if (secondCopy.getSecondTerm().isRPOGreater(secondCopy.getFirstTerm()) == -1) {
							secondCopy.getFirstTerm().substituteSubterm(firstCopy.getSecondTerm(), subtermCopy);
							return new Equation(secondCopy.getFirstTerm(), secondCopy.getSecondTerm(), false);
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

			System.out.println((i + 1)+ "# iterazione dell'algoritmo della clausola data: " + to_select.size() + " " + selected.size());

			// Select the given clause
			Equation givenClause = selectGivenClause();
			to_select.remove(givenClause);

			System.out.println("Ho selezionato la seguente clausola data: " + givenClause.toString());

			// Apply tautology elimination
			if (tautologyElimination(givenClause)) {
				System.out.println("	Applying tautology");

				// The given clause is a tautology: I remove it from selected
				this.to_select.remove(givenClause);

				// and I choose another one
				i++;
				continue;
			}

			// Trying to subsume the given clause with the clauses in selected

			boolean flag = false;
			Vector<Equation> combinazioni=new Vector<Equation>();
			System.out.println("	Applying functional subsumption");
			for (Equation e: this.selected) {
				combinazioni=combining(e, givenClause);
				for(int a = 0; a < 4; a++)
				{
					if (sussunzioneFunzionale(combinazioni.elementAt(a*2), combinazioni.elementAt(a*2+1))) {
						System.out.println("						Applicata sussunzione");
						// Given clause is subsumed by "e"
						selected.remove(givenClause);
						flag = true;
						break;
					}
				}
				if (flag)
					continue;
			}

			if (flag)
				continue;
			flag = false;

			combinazioni = new Vector<Equation>();

			Vector<Equation> ToRemove=new Vector<Equation>();

			// Trying to simply the given clause with the clauses in selected
			System.out.println("	Applying equational simplification");
			for (Equation e: this.selected) {
				combinazioni=combining(e, givenClause);
				for(int a=0; a<4;a++)
				{
					// The generated equation
					newEquationTmp = semplificazione_equazionale(combinazioni.elementAt(2*a), combinazioni.elementAt(2*a+1));

					// If the rules generated something, it must be added to selected
					if (newEquationTmp != null) {
						givenClause = newEquationTmp;
						//to_select.add(newEquationTmp);
						ToRemove.add(e);
						break;
					}
				}
			}

			for(Equation temp: ToRemove)
				selected.remove(temp);

			combinazioni=new Vector<Equation>();
			// Apply reflection
			if (reflection(givenClause)) {
				System.out.println("	Applying reflection");

				if (givenClause.isTheGoal()) {
					System.err.println("Finito.");
					System.exit(0);
				}
			}


			// Add the given clause to the selected equation list
			selected.add(givenClause);

			/**
			 * Second phase
			 */

			// Vector of new equations created
			Vector<Equation> newEquations = new Vector<Equation>();

			// Reduce w.r.t. the clause in selected
			for (Equation e: this.selected) {
				combinazioni=combining(e, givenClause);
				for(int a=0;a<4;a++)
				{
					Equation newEquation;

					// Sovrapposizione
					if ((newEquation = sovrapposizione(combinazioni.elementAt(2*a+1), combinazioni.elementAt(2*a))) != null) {
						System.out.println("	Applying overlap");
						newEquations.add(newEquation);
					}
				}
			}
			combinazioni = new Vector<Equation>();

			// Test created clauses
			System.out.println("Ho generato " + newEquations.size() + " equazioni");

			/**
			 * Third phase
			 */

			Vector<Equation> toDelete = new Vector<Equation>();

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
					combinazioni=combining(eq, target);
					for(int a=0;a<4;a++){
						if (sussunzioneFunzionale(combinazioni.elementAt(2*a), combinazioni.elementAt(2*a+1))) {
							System.out.println("						Applicata sussunzione");
							toDelete.add(target);
							flag=true;
							continue;
						}
					}
					if(flag){
						flag=false;
						continue;
					}
				}

			}
			flag=false;
			for (Equation eq: toDelete)
				newEquations.remove(eq);

			toDelete = new Vector<Equation>();
			combinazioni=new Vector<Equation>();
			Vector<Equation> toDeleteFromSelected = new Vector<Equation>();

			for (Equation target: newEquations) {
				for(Equation eq: this.selected) {
					combinazioni=combining(eq, target);
					for(int a=0;a<4;a++){
						newEquationTmp = semplificazione_equazionale(combinazioni.elementAt(2*a), combinazioni.elementAt(2*a+1));

						if (newEquationTmp != null) {
							toDelete.add(target);
							toDeleteFromSelected.add(eq);
							to_select.add(newEquationTmp);
						}
					}
				}
			}

			combinazioni=new Vector<Equation>();
			for (Equation eq: toDelete)
				newEquations.remove(eq);

			for (Equation eq: toDeleteFromSelected)
				selected.remove(eq);

			toDelete = new Vector<Equation>();
			toDeleteFromSelected = new Vector<Equation>();

			for (Equation target: newEquations) {
				// Apply reflection
				if (reflection(target)) {
					if (target.isTheGoal()) {
						System.err.println("Finito.");
						System.exit(0);
					}
				}
			}

			for (Equation alpha: newEquations)
				to_select.add(alpha);

			// Add to selected
			if (i >=  100)
				break;
			i++;
			// Repeate
		}

		System.out.println((i + 1)+ "# iterazione dell'algoritmo della clausola data: " + to_select.size() + " " + selected.size());


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
				if (to_select.get(indexOfGivenClause).weight() < e.weight())
					indexOfGivenClause = to_select.indexOf(e);

			indexForRatio++;
			return to_select.get(indexOfGivenClause);
		} else {
			System.out.println("Ho scelto l'ultima equazione entrata nella lista");
			indexForRatio = 0;
			return to_select.get(to_select.size() -1);
		}
	}


	//se ritorna un'equazione != null, rimuovo le equazioni passate nel metodo
	public Equation semplificazione_equazionale(Equation first, Equation second) {

		//Equation firstCopy = first.clone();
		//Equation secondCopy = second.clone();

		for (Term subterm: second.getFirstTerm().getSubTerms()) {

			Equation firstCopy = first.clone();

			RobinsonAlgorithm ra = new RobinsonAlgorithm(subterm, firstCopy.getFirstTerm());
			Substitution sub = ra.getSubstitution();
			Term tempSub = subterm.clone();

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

	public boolean sussunzioneFunzionale(Equation first, Equation second){

		int sentinella = 0;
		
		//se la funzione non sono uguali
		if( !(second.getFirstTerm().getSymbol().equals(second.getSecondTerm().getSymbol())))
			return false;

		//se il numero di parametri non coincide
		if(!((2 == second.getFirstTerm().getSubTerms().size()) && (2 == second.getSecondTerm().getSubTerms().size())) )
			return false;

		for (int i = 0; i < second.getFirstTerm().getSubTerms().size(); i++) {
			System.out.println(i +"# iterazione");
			System.out.println("prima_sost: "+first);
			RobinsonAlgorithm ra = new RobinsonAlgorithm(first.getFirstTerm(), second.getFirstTerm().getSubTerms().elementAt(i));
			Substitution sub1 = ra.getSubstitution();
			System.out.println("dopo_prima_sost: "+first); //quando cerca la sostituzione la applica anche!!!! GIUSTO????
			System.out.println("PRIMA SOST.");

			RobinsonAlgorithm ra1 = new RobinsonAlgorithm(first.getSecondTerm(), second.getSecondTerm().getSubTerms().elementAt(i));
			Substitution sub2 = ra1.getSubstitution();
			System.out.println("SECONDA SOST.");

			Equation firstCopy = first.clone();
			Equation secondCopy = second.clone();

			System.out.println("originale: "+first);
			System.out.println("prima: "+firstCopy);
			firstCopy.applySubstitution(sub1);
			System.out.println("dopo: "+firstCopy);

			Set<Term> s1 = sub1.keySet();
			Set<Term> s2 = sub2.keySet();

			Substitution union = (Substitution) sub2.clone();

			System.out.println("s2.tostring: "+s2.toString());


			boolean finish = false;

			for (Term var: s1) {
				//STAMPE DI TEST
				System.out.println("var iniziale: "+var.toString());
				for(Term var2: s2)
				{
					System.out.println("var2: "+var2.toString());
					if(var2.equals(var))
					{
						sentinella=1;
						System.out.println("UGUALI!!!!!");
						System.out.println("sub2.get: "+sub2.get(var2));
						System.out.println("sub1.get: "+sub1.get(var));
						System.out.println("uguali?" +(sub2.get(var2)).equals(sub1.get(var)));

						if(!(sub2.get(var2)).equals(sub1.get(var))) {
							System.out.println("Stesso simbolo ma inizializzazione diversa");
							finish = true;
							continue;
						}
					}
				}

				if (finish)
					continue;

				if(sentinella == 0){
					System.out.println("Il simbolo cercato non e' presente. Lo inserisco!");
					//System.out.println("union:"+union.toString());
					union.put(var, sub1.get(var));
				}

				sentinella=0;
			}

			if (finish)
				continue;

			System.out.println("Union: "+union.toString());
			firstCopy.getFirstTerm().applySubstitution(union);
			firstCopy.getSecondTerm().applySubstitution(union);
			System.out.println("first_unificata "+firstCopy);
			System.out.println("firstCopy.getFirstTerm(): "+firstCopy.getFirstTerm().toString());
			System.out.println("secondCopy.getFirstTerm().getSubTerms(): "+secondCopy.getFirstTerm().getSubTerms().toString());
			System.out.println("prima_parte_comparata: "+(firstCopy.getFirstTerm().toString()).equals(secondCopy.getFirstTerm().getSubTerms().toString()));

			//NON FUNZIONA perchè non riesce a comparare perchè il getValue() e' presente solo nelle variabili e non in funzione e costante quindi non posso fare il cast altrimenti
			//nel caso di funzione castata a variabile da errore.

			if(!((firstCopy.getFirstTerm().equals(secondCopy.getFirstTerm().getSubTerms().elementAt(i))) &&
					(firstCopy.getSecondTerm().equals(secondCopy.getSecondTerm().getSubTerms().elementAt(i)))) )
				continue;

			//secondCopy.getFirstTerm().substituteSubterm(firstCopy.getFirstTerm(), secondCopy.getFirstTerm().getSubTerms().elementAt(0));
			//secondCopy.getSecondTerm().substituteSubterm(firstCopy.getSecondTerm(), secondCopy.getSecondTerm().getSubTerms().elementAt(0));

			return true;
		}

		return false;
	}

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
	}
}