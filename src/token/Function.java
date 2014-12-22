package token;

import java.util.LinkedList;

/**
 * Class used to identify a function
 * @author Vincenzo Arceri
 */
public class Function extends Term {
	/**
	 * Linked list of the parameters of the function
	 */
	public LinkedList<Term> arguments;
	
	/**
	 * Arity of the function 
	 */
	private int arity;

	/**
	 * Function construction
	 * @param symbol: identify the function symbol
	 * @param terms: parameters of the function
	 */
	public Function(String symbol, LinkedList<Term> arguments) {
		super(symbol);
		this.arguments = arguments;
		this.arity = arguments.size();
	}

	@Override
	public String toString() {
		String result = symbol + "(";
		for(Term t : arguments) {
			result += t.toString() + ",";
		}
		
		
		return result.substring(0, result.length() - 1) + ")";
	}
	
	@Override
	public boolean contains(Term t) {
		if (t.toString().equals(this.toString()))
			return true;
		else
			for(Term term: this.arguments)
				if (term.contains(t))
					return true;
			
		return false;
	}
	
	public int getArity() {
		return arity;
	}
	
	@Override
	public String getSymbol() {
		return symbol;
	}
	
	public LinkedList<Term> getArguments() {
		return this.arguments;
	}
	
	public boolean equals(Function fun) {
		return (fun.getSymbol().equals(this.getSymbol())) && (fun.getArguments().equals(this.getArguments()));
	}
	
	public Function clone() {
		LinkedList<Term> copiedArguments = new LinkedList<Term>();
		copiedArguments.addAll(this.getArguments());
		
		return new Function(this.getSymbol(), copiedArguments);
	}
	
	public void replaceWith(Variable toReplace, Term substitution) {
		for (Term term: this.getArguments())
			if (term.contains(toReplace))
				term.replaceWith(toReplace, substitution);
	}
}
