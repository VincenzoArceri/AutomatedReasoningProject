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
	 * State of the function can be true (means "lexicographical order")
	 * or false (means "multiset order")
	 */
	private String state = "";

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
	public boolean contains(Term term) {
		boolean flag = true;
		
		if ((term instanceof Function) && (term.getSymbol().equals(this.getSymbol()))) {
			for (int i = 0; i < ((Function) this).getArity(); ++i)
				if (this.getArgumentOf(i).equals(((Function) term).getArgumentOf(i)))
					continue;
				else {
					flag = false;
					break;	
				}
			
		if (flag)
			return true;
		} else {
			for(Term t: this.getArguments())
				if (t.contains(term))
					return true;
		}
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
	
	/**
	 * Return true if the function has a lexicographical order
	 * @return true - if the function has a lexicographical order
	 */
	public boolean isLexicolGraphical() {
		return this.state.equals("lex");
	}
	
	/**
	 * Return true if the function has a multiset order
	 * @return true - if the function has a lexicographical
	 */
	public boolean isMultiSet() {
		return this.state.equals("mul");
	}
	
	/**
	 * Set the state of the function symbols ("lex" or "mul")
	 * @param state State to set
	 * @return true if the state is "lex" or "mul"
	 * 		   false in the other cases
	 */
	public boolean setState(String state) {
		if (state.equals("lex")) {
			this.state = "lex";
			return true;
		} else if (state.equals("mul")) {
			this.state = "mul";
			return true;
		} else
			return false;
	}
	
	/**
	 * Returns the state of the function
	 * @return The state of the function
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Return the term in arguments in position index
	 * @param index position of the term in arguments
	 * @return the term in position index
	 */
	public Term getArgumentOf(int index) {
		return this.getArguments().get(index);
	}

	@Override
	public int isRPOGreater(Term second) {
		// First case
		if (second instanceof Function) {
			for(Term arg: this.getArguments()) {
				if (arg.contains(second))
					return 1;
			}

			// Second case
			if (this.getSymbol().compareTo(second.getSymbol()) >= 0) 
				for(Term arg: ((Function) second).getArguments())
					if (this.isRPOGreater(arg) != 1)
						return -1;

			// Third case
		} 

		// Second case - if second is a Constant or Variable
		else 
			if (this.contains(second))
				return 1;
		
		// return -1;
		return 5;
	}
}
