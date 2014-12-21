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
	LinkedList<Term> arguments;
	
	/**
	 * Function construction
	 * @param symbol: identify the function symbol
	 * @param terms: parameters of the function
	 */
	public Function(String symbol, LinkedList<Term> arguments) {
		super(symbol);
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		String result = symbol + "(";
		for(Term t : arguments) {
			result += t.toString() + ",";
		}
		
		if (arguments.size() == 1)
			return result +")";
		else
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
}
