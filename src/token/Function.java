package token;

import java.util.LinkedList;

/**
 * Function class
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
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
	/**
	 * Function returns arity of the function
	 * @return the arity of the function
	 */
	public int getArity() {
		return arity;
	}
	
	@Override
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Function returns the arguments of the function
	 * @return the arguments of the function
	 */
	public LinkedList<Term> getArguments() {
		return this.arguments;
	}
	
	@Override
	public boolean equals(Object term) {
		return (term instanceof Function) && (((Function) term).getSymbol().equals(this.getSymbol())) && (((Function) term).getArguments().equals(this.getArguments()));
	}
	
	@Override
	public Function clone() {
		LinkedList<Term> copiedArguments = new LinkedList<Term>();
		copiedArguments.addAll(this.getArguments());
		
		return new Function(this.getSymbol(), copiedArguments);
	}
	
	@Override
	public void replaceWith(Variable toReplace, Term substitution) {
		for (Term term: this.getArguments())
			if (term.contains(toReplace))
				term.replaceWith(toReplace, substitution);
	}
}
