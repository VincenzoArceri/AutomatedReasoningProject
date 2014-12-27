package token;

/**
 * Constant class
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
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
	public boolean contains(Term term) {
		return (term instanceof Constant) && (term.getSymbol().equals(this.getSymbol()));
	}
	
	@Override
	public Constant clone() {
		return new Constant(this.getSymbol());
	}
	
	public boolean equals(Object term) {
		return (term instanceof Constant) && ((Constant) term).getSymbol().equals(this.getSymbol());
	}

	@Override
	public void replaceWith(Variable toReplace, Term substitution) { }

	@Override
	public int isRPOGreater(Term term) {
		if (term instanceof Constant) {
			if (this.getSymbol().compareTo(term.getSymbol()) >= 0)
				return 1;
			else
				return 0;
		} else
			return -1;
	}
	
	
}
