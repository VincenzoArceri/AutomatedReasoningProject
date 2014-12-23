package index;

import java.util.HashMap;
import java.util.Vector;
import token.*;

public class RobinsonAlgorithm {
	
	private Term first;
	private Term second;
	private HashMap<Term, Term> equations;
	private Substitution sub = new Substitution();
	private int index = 0;
	
	
	public RobinsonAlgorithm(Term first, Term second) {
		this.first = first;
		this.second = second;
		this.equations = new HashMap<Term, Term>();
		this.equations.put(first, second);
	}
	
	public HashMap<Variable, Term> getSubstitution() {
		chooseRule(first, second);
		return sub;
	}

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
		
		chooseEquation(index=0);
	}

	private void elimination(Variable first, Term second, Term key) {
		System.out.println("Elimination: " + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());

		HashMap<Term, Term> copy = (HashMap<Term, Term>) equations.clone();
		
		Term t = equations.remove(first);
		
		Object[] keyTermsTmp = ((equations.keySet()).toArray());
		Term[] keyTerms = new Term[keyTermsTmp.length];
		
		for (int i = 0; i < keyTermsTmp.length; i++)
			keyTerms[i] = (Term) keyTermsTmp[i];
			
		
		Vector<Term> mapTerms = new Vector<Term>();
		
		for(int i = 0; i < keyTerms.length; i++)
			mapTerms.add(this.equations.get(keyTerms[i]));
		
		equations.clear();
		
		for(int i = 0; i < keyTerms.length; i++) {
			mapTerms.get(i).replaceWith(first, second);
			equations.put(keyTerms[i], mapTerms.get(i));
		}
		
		// ----------------------------------
		
		keyTermsTmp = (( equations.keySet()).toArray());
		
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

	private void removal(Term first) {
		System.out.println("Removal" + first.toString());
		System.out.println("With the set: " + equations.toString());
		
		this.equations.remove(first);
		chooseEquation(index = 0);
	}

	private void controlOfOccurrence(Variable first, Term second) {
		System.out.println("Control of occurence:" + first.toString() + " " + second.toString());
		System.out.println("With the set: " + equations.toString());
		
		equations.clear();
		chooseEquation(index = 0);
	}

	private void chooseRule(Term first, Term second) {
		
		if ((first instanceof Function) && (second instanceof Function) && (first.getSymbol().equals(second.getSymbol())))
			decomposition((Function) first, (Function) second, first);
		else if ((first instanceof Variable) && (second instanceof Variable) && (((Variable) first).getSymbol()).equals(((Variable) second).getSymbol())) 
			removal(first);
		else if ((first instanceof Constant) && (second instanceof Constant) && (((Constant) first).getSymbol()).equals(((Constant) second).getSymbol())) 
			removal(first);
		else if ((first instanceof Variable) && !(second.contains(first))) 
			elimination((Variable) first, second, first);
		else if ((second instanceof Variable) && !(first.contains(second))) 
			elimination((Variable) first, second, second);
		else if ((first instanceof Variable) && (second.contains(first)))
			controlOfOccurrence((Variable) first, second);
		else if ((second instanceof Variable) && (first.contains(second)))
			controlOfOccurrence((Variable) second, first);
	}
	
	private void chooseEquation(int index) {
		if (equations.isEmpty())
			return;
		else {
			Term selected = ((Term) ((equations.keySet()).toArray())[index]);
			chooseRule(selected, equations.get(selected));
		}
	}
}
