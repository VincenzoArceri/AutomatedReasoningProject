package token;

import index.Substitution;

import java.util.LinkedList;
import java.util.Vector;

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
		
		// For each term in arguments, create a copy to add
		for (int i = 0 ; i < this.getArguments().size(); i++)
			copiedArguments.add(this.getArgumentOf(i).clone());
				
	//	copiedArguments.addAll(this.getArguments().clone());
		
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
			
			System.out.println("First");
			
			for(Term arg: this.getArguments()) {
				if (arg.contains(second))
					return 1;
			}

			// Second case
			if (this.getSymbol().compareTo(second.getSymbol()) < 0) {
				System.out.println("Second");
				for(Term arg: ((Function) second).getArguments())
					if (this.isRPOGreater(arg) != 1)
						return -1;
				return 1;
			}
			// Third case
			
			if (this.getSymbol().equals(second.getSymbol())) {
				System.out.println("Third");
				if (this.getState().equals("lex")) {
					return this.isLEXGreaterThen(((Function) second).argumentsToVector());
				} else if (this.getState().equals("mul")) {
					// Mul case
					
				}
			}
		} 

		// Second case - if second is a Constant or Variable
		else 
			if (this.contains(second))
				return 1;

		return -1;
	}
	
	/**
	 * Returns a vector of the arguments of the function
	 * @return a vector of the arguments of the function
	 */
	private Vector<Term> argumentsToVector() {
		Vector<Term> result = new Vector<Term>();
		
		Term[] arguments = this.getArguments().toArray(new Term[this.getArguments().size()]);

		for (int i = 0; i < getArguments().size(); i++)
			result.add(arguments[i]);
		
		return result;
	}
	
	// TODO
	private int isMULGreaterThen() {
		return 0;
	}
	
	private int isLEXGreaterThen(Vector<Term> second) {
		Vector<Term> first = this.argumentsToVector();
		System.out.println("LEX first ->" + this.arguments.toString());
		System.out.println("LEX second ->" + second.toString());
		
		for (int i = 0; i < first.size(); ++i) {
			
			if (first.get(i).equals(second.get(i))) 
				continue;
			else if (first.get(i).isRPOGreater(second.get(i)) == 1) {

				// Subterm property
				for (int j = i + 1; j < first.size(); j++) {
					if (this.isRPOGreater(second.get(j)) == 1)
						continue;
					else
						return -1;
				}
				return 1;
			} else if (first.get(i).isRPOGreater(second.get(i)) == -1)
				return -1;
		}
		
		// ARRIVO QUI E I TERMINI SONO UGUALI, COSA RITORNO?
		return 1;
	}
	
	public Vector<Term> getSubTerms() {
		return this.argumentsToVector();
	}

	@Override
	public void applySubstitution(Substitution sub) {
		for (Term term : this.getArguments())
			term.applySubstitution(sub);
	}

	@Override
	public void substituteSubterm(Term subterm, Term to_substitute) {
		if (this.equals(to_substitute)) {
			this.setSymbol(subterm.getSymbol());
			this.arguments = (LinkedList<Term>) ((Function) subterm).arguments.clone();
		} else {
			for (Term arg: this.getArguments())
				arg.substituteSubterm(subterm, to_substitute);
		}
	}

	@Override
	public int weight() {
		int result = 0;

		for (Term arg : this.getArguments()) 
			result += arg.weight();
		
		return result;
	}

	@Override
	public boolean isGround() {
		for (Term subterm : this.getArguments()) 
			if (subterm.isGround())
				continue;
			else
				return false;
		return true;
	}
}
