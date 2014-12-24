package token;

/**
 * Class that identify a equation
 * @author Vincenzo Arceri
 */
public class Equation {
	
	/**
	 * First term of the equation
	 */
	private Term first;
	
	/**
	 * Secondo term of the equation
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
}
