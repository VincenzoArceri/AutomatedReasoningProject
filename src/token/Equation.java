package token;

/**
 * Class that identify a equation
 * @author Vincenzo Arceri
 */
public class Equation {
	
	/**
	 * First term of the equation
	 */
	Term first;
	
	/**
	 * Secondo term of the equation
	 */
	Term second;
	
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

}
