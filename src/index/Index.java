package index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import parser.*;
import token.*;

/**
 * 
 * Init class
 * @author Vincenzo Arceri
 * 
 */
public class Index {
	
	public static HashSet<Equation> to_select =  new HashSet<Equation>();
	public static HashSet<Equation> selected =  new HashSet<Equation>();
	
	public static void main(String[] args) throws ParseException, TokenMgrError, NumberFormatException {
		
		System.out.println("Hi! This is a simply theorem prover for equality theories.");
		System.out.println("All the clauses are in this form: (T = T)  or (T != T) where: ");
		System.out.println("\t T is a term: can be a variable, a constant or a function applied to a list of term");
		System.out.println("\t Variables: use upper character;");
		System.out.println("\t Constant: use lower character;");
		System.out.println("\t Function: use at least two lower character applied to a list of term");
		System.out.println("Example: mul(a, get(b)) = product(a, set(b))\n");
		
    	System.out.println("Enter the set of clauses:\n");
    	
    	InputStreamReader reader = new InputStreamReader(System.in);
    	BufferedReader myInput = new BufferedReader(reader);
    	
    	String input = new String();
    	try {
    		input = myInput.readLine();
    	} catch (IOException e) {
    		System.out.println ("Error!" + e);
    		System.exit(-1);
    	}
    	
    	Grammar parser = new Grammar(input, to_select, selected); 
    	

    	
    	
    	System.out.println("Enter your goal:");  
    	
    	input = "";
    	try {
    		input = myInput.readLine();
    	} catch (IOException e) {
    		System.out.println ("Error!" + e);
    		System.exit(-1);
    	}

    	parser = new Grammar(input, to_select, selected);
    	
    	System.out.println("To select:");
    	for(Equation e: to_select)
    		System.out.println(e.toString());
    	
    	System.out.println("Selected");
    	for(Equation e: selected)
    		System.out.println(e.toString());
    
    	System.out.println("Done.");
	}
}
