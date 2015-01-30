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
	
	private boolean isGoal;
	
	/**
	 * Equation constructor
	 * @param first: first term of the equation
	 * @param second: second term of the equation
	 */
	public Equation(Term first, Term second, boolean isNegative) {
		this.first = first;
		this.second = second;
		this.isGoal = false;
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
	
	public int weight() {
		return this.getFirstTerm().weight() + this.getSecondTerm().weight();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Equation)
			return  ((((Equation) obj).getFirstTerm().equals(this.getFirstTerm()) && 
					((Equation) obj).getSecondTerm().equals(this.getSecondTerm())) ||
					((((Equation) obj).getFirstTerm().equals(this.getSecondTerm()) && 
					((Equation) obj).getSecondTerm().equals(this.getFirstTerm()))));
		else return false;
	}
	
	public void setFirstTerm(Term term) {
		this.first = term;
	}
	
	public void setSecondTerm(Term term) {
		this.second = term;
	}
	
	public void setGoal() {
		this.isGoal = true;
	}
	
	public boolean isTheGoal() {
		return this.isGoal;
	}
	

}
