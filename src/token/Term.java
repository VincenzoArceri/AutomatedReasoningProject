package token;

/**
 * Abstract class to indentify a token
 * @author Vincenzo Areri
 */
public abstract class Term {
	/**
	 * Token symbol
	 */
	String symbol;
	
	/**
	 * Token constructor
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

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public abstract Term clone();
	
	public abstract void replaceWith(Variable toReplace, Term substitution);
}
