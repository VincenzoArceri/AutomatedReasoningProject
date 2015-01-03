package token;

import index.Substitution;

/**
 * Equation class
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 */
public class Equation extends Token {
	
	/**
	 * First term of the equation
	 */
	private Term first;
	
	/**
	 * Second term of the equation
	 */
	private Term second;
	
	private boolean isNegative;
	
	/**
	 * Equation constructor
	 * @param first: first term of the equation
	 * @param second: second term of the equation
	 */
	public Equation(Term first, Term second, boolean isNegative) {
		this.first = first;
		this.second = second;
		this.isNegative = isNegative;
	}
	
	@Override
	public String toString() { 
		return first.toString() + (isNegative ? " != " : " = ") + second.toString();
	}
	
	/**
	 * Returns the first term of the equation
	 */
	public Term getFirstTerm() {
		return first;
	}
	
	/**
	 * Returns the second term of the equation
	 */
	public Term getSecondTerm() {
		return second;
	}
	
	/**
	 * Returns "true" if this is negative else "false"
	 */
	public boolean isNegative() {
		return isNegative;
	}
	
	public Equation clone() {
		return new Equation(this.getFirstTerm().clone(), this.getSecondTerm().clone(), isNegative);
	}
	
	public void applySubstitution(Substitution sub) {
		this.getFirstTerm().applySubstitution(sub);
		this.getSecondTerm().applySubstitution(sub);
	}
}
