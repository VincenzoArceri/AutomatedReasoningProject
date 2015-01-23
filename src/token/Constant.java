package token;

import index.Substitution;

import java.util.Vector;

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
	
	@Override
	public boolean equals(Object term) {
		return (term instanceof Constant) && ((Constant) term).getSymbol().equals(this.getSymbol());
	}

	@Override
	public void replaceWith(Variable toReplace, Term substitution) {
		// this is a constant, there's nothing to substitute
	}
	
	@Override
	public int isRPOGreater(Term term) {
		if (term instanceof Constant) {
			if (this.getSymbol().compareTo(term.getSymbol()) < 0)
				return 1;
			else
				return 0;
		} else if ((term instanceof Variable) && (((Variable) term).isInizialized()))
			return this.isRPOGreater(((Variable) term).getValue());
		else
			return -1;
	}

	@Override
	public Vector<Term> getSubTerms() {
		Vector<Term> result = new Vector<Term>();
		result.add(this.clone());
		return result;
	}

	@Override
	public void applySubstitution(Substitution sub) {
		
	}

	@Override
	public void substituteSubterm(Term subterm, Term to_substitute) { 
		
	}

	@Override
	public int weight() {
		return 1;
	}

	@Override
	public boolean isGround() {
		return true;
	}
	
}
