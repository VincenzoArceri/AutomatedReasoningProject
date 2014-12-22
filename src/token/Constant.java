package token;

/**
 * Class that identify a constant
 * @author Vincenzo Arceri
 */
public class Constant extends Term {
	
	/**
	 * Constant constructor
	 * @param symbol: identify the variable
	 */
	public Constant(String symbol) {
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
	
	@Override
	public Constant clone() {
		return new Constant(this.getSymbol());
	}
	
	public boolean equals(Constant c) {
		return c.getSymbol().equals(this.getSymbol());
	}

	@Override
	public void replaceWith(Variable toReplace, Term substitution) { }
}
