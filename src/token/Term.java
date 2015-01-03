package token;

import index.Substitution;

import java.util.Vector;

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
	
	/**
	 * Comparison between this this and term with recursive path ordering
	 * @param term
	 * @return Returns -1 if this and term aren't comparable
	 * 					1 if this is greater (according to the recursive path ordering) than second
	 */
	public abstract int isRPOGreater(Term term);
	
	/**
	 * Returns the subterms of the term
	 * @return the sbuterms of the term
	 */
	public abstract Vector<Term> getSubTerms();
	
	/**
	 * Applies a substitution to the term
	 * @param sub substitution to apply
	 */
	public abstract void applySubstitution(Substitution sub);
	
	/**
	 * Replace a subterm with another one.
	 * @param subterm the substitution
	 * @param to_sostitute subterm to substitute
	 */
	public abstract void substituteSubterm(Term subterm, Term to_sostitute);
}
