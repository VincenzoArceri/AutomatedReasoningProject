package token;

/**
 * Variable class
 * In this project all the variables are universally quantified!
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 */
public class Variable extends Term {
	
	/**
	 * Value of the variable
	 */
	private Term value;
	
	/**
	 * Variable constructor
	 * @param symbol: identify the variable
	 */
	public Variable(String symbol) {
		super(symbol);
		
		// The value of the variable isn't assign
		this.value = null;
	}

	@Override
	public String toString() {
		return symbol;
	}

	@Override
	public boolean contains(Term term) {
		if (this.isInizialized() && !(term instanceof Variable)) 
			return this.value.contains(term);
		else if ((term instanceof Variable) && (this.isInizialized()) && ((Variable) term).isInizialized()) 
			return this.value.contains(((Variable) term).value);
		else if ((term instanceof Variable) && (this.isInizialized()) && !(((Variable) term).isInizialized()))
			return this.value.contains(term);
		else if ((term instanceof Variable) && !(this.isInizialized()) && (((Variable) term).isInizialized()))
			return false;
		else if ((term instanceof Variable) && !(this.isInizialized()) && !(((Variable) term).isInizialized()))
			return (this.getSymbol().equals(term.getSymbol()));
		
		return false;
	}

	@Override
	public Variable clone() {
		return new Variable(this.getSymbol());
	}
	
	// CAMBIARE SICURO
	public void replaceWith(Variable toReplace, Term substitution) {
		if (this.equals(toReplace))
			this.symbol = substitution.toString();
	}

	@Override
	public boolean equals(Object term) {
		return (term instanceof Variable) && (this.getSymbol().equals(((Variable) term).getSymbol()));
	}
	
	/**
	 * Assign the value of the variable
	 * @param term
	 */
	public void setValue(Term term) {
		this.value = term;
	}
	
	/**
	 * Returns the variable value
	 * @return
	 */
	public Term getValue() {
		return this.value;
	}
	
	/**
	 * Return true if this has a value, false in other cases
	 * @return true if this has a value, false in other cases
	 */
	public boolean isInizialized() {
		return this.value == null ? false : true;
	}

	@Override
	public int isRPOGreater(Term term) {
		if ((term instanceof Variable) && (!((Variable) term).isInizialized()) && !(this.isInizialized()))
			return -1;
		else if ((this.isInizialized()) && (term instanceof Variable) && !(((Variable) term).isInizialized()))
			return this.value.contains(term) ? 1 : -1;
		else if ((this.isInizialized()) && (term instanceof Variable) && (((Variable) term).isInizialized()))
			return this.value.contains(((Variable) term).getValue()) ? 1 : -1;
		else if (!(this.isInizialized()) && (term instanceof Variable) && (((Variable) term).isInizialized()))
			return -1;
		else if ((this.isInizialized()) && !(term instanceof Variable))
			return (this.value.contains(term)) ? 1 : -1;
		return -1;
	}
}
