package token;

/**
 * Class that identify a variable 
 * In this project all the variables are universally quantified!
 * @author Vincenzo Arceri
 */
public class Variable extends Term {
	
	/**
	 * Variable constructor
	 * @param symbol: identify the variable
	 */
	public Variable(String symbol) {
		super(symbol);
	}

	@Override
	public String toString() {
		return symbol;
	}

	@Override
	public boolean contains(Term t) {
		return t.toString().equals(this.symbol) ? true : false;
	}
	
	public boolean equals(Variable var) {
		return this.getSymbol().equals(var.getSymbol());
	}

	@Override
	public Variable clone() {
		return new Variable(this.getSymbol());
	}
	
	public void replaceWith(Variable toReplace, Term substitution) {
		if (this.equals(toReplace))
			this.symbol = substitution.toString();
	}
}
