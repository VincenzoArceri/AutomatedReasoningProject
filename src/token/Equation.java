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
	
	/**
	 * Equation constructor
	 * @param first: first term of the equation
	 * @param second: second term of the equation
	 */
	public Equation(Term first, Term second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public String toString() { 
		return first.toString() + " = " + second.toString();
	}
	
	/**
	 * Returns the first term of the equation
	 * @return the first term of the equation
	 */
	public Term getFirstTerm() {
		return first;
	}
	
	/**
	 * Returns the second term of the equation
	 * @return the second term of the equation
	 */
	public Term getSecondTerm() {
		return second;
	}
}
