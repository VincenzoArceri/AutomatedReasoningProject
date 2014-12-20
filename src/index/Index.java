package index;

import parser.Grammar;
import parser.ParseException;
import parser.TokenMgrError;

/**
 * 
 * Init class
 * @author Vincenzo Arceri
 * 
 */
public class Index {
	public static void main(String[] args) throws ParseException, TokenMgrError, NumberFormatException {
		System.out.println("Hi! This is a simply theorem prover for equality theories.");
		System.out.println("All the clauses are in this form: (T = T)  or (T != T) where: ");
		System.out.println("\t T is a term: can be a variable, a constant or a function applied to a list of term");
		System.out.println("\t Variables: use upper character;");
		System.out.println("\t Constant: use lower character;");
		System.out.println("\t Function: use at least two lower character applied to a list of term");
		System.out.println("Example: mul(a, get(b)) = product(a, set(b))\n");
    	System.out.println("Enter the set of clauses:\n");
    
    	Grammar parser = new Grammar( System.in ) ;
    	parser.START() ;
    	
    	System.out.println("Done.");
	}
}
