package token;

/**
 * Term abstract class
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 */
public abstract class Term extends Token{
	/**
	 * Term symbol
	 */
	String symbol;
	
	/**
	 * Term constructor
	 * @param symbol: symbol that identify the token
	 */
	public Term(String symbol) {
		this.symbol = symbol;		
	}
	
	@Override
	public abstract String toString();
	
	/**
	 * Method to control if the parameter is a subterm of this
	 */
	public abstract boolean contains(Term t);
	
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Method to set the symbol
	 * @param symbol - symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Returns a copy of the term
	 */
	@Override
	public abstract Term clone();
	
	/**
	 * Replace toReplace with substitution in the term
	 * @param toReplace: term to replace
	 * @param substitution: term that substitute toReplace
	 */
	public abstract void replaceWith(Variable toReplace, Term substitution);
	
	@Override
	public abstract boolean equals(Object term);
	
	public abstract int isRPOGreater(Term term);
}
