package index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import parser.*;
import token.*;

/**
 * 
 * Init class
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 * 
 */
public class Index {
	
	public static Vector<Equation> to_select =  new Vector<Equation>();
	public static Vector<Equation> selected =  new Vector<Equation>();
	public static Vector<Function> functionSet = new Vector<Function>();
	

	public static void main(String[] args) throws ParseException, TokenMgrError, NumberFormatException {
		
		// Simply menu
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
    	
    	// Start the parser for the set of axioms (must be all positive?)
    	Grammar parser = new Grammar(input, to_select, selected, functionSet); 
    
    	System.out.println("Enter your goal:");  
    	
    	input = "";
    	try {
    		input = myInput.readLine();
    	} catch (IOException e) {
    		System.out.println ("Error!" + e);
    		System.exit(-1);
    	}

    	// Start the parser for the goal (must be negative!)
    	parser = new Grammar(input, to_select, selected, functionSet);


    	
    	
    	//Substitution sub = (new RobinsonAlgorithm(to_select.get(0).getFirstTerm(), to_select.get(0).getSecondTerm())).getSubstitution();
    	
    	
    	System.out.println(to_select.get(0).getFirstTerm().isRPOGreater(to_select.get(0).getSecondTerm()));
    	
    	//System.out.println(sub);
    	
    	System.out.println("Assign a state to all the function symbol you used (\"lex\" or \"mul\")");

    	// Set the state for all the function symbol
    	for (int i = 0; i < parser.functionSet.size(); ++i) {
    		if (parser.functionSet.get(i).getState().equals("")) {
    			do {
    				System.out.println("State for " + parser.functionSet.get(i).getSymbol());

    				input = "";
    				try {
    					input = myInput.readLine();
    				} catch (IOException e) {
    					System.out.println ("Error!" + e);
    					System.exit(-1);
    				}
    			} while (!parser.functionSet.get(i).setState(input));

    			for (Function function: parser.functionSet)
    				if (((Function) function).getSymbol().equals(parser.functionSet.get(i).getSymbol()))
    					function.setState(input);
    		} 
    	}

    	// Print to_select and selected
    	printSelected();
    	printToSelect();
	}

	private static void printSelected() {
		System.out.println("Selected equations:");

		for(Equation e: selected)
			System.out.println(e.toString());
	}

	private static void printToSelect() {
		System.out.println("To select equations:");

		for(Equation e: to_select) {
			System.out.println(e.toString());

			if  (e.getFirstTerm() instanceof Function) {
				System.out.println("" + e.getFirstTerm() + ((Function) e.getFirstTerm()).getState());

				for (Term term: ((Function) e.getFirstTerm()).arguments)
					if (term instanceof Function)
						System.out.println("" + term + ((Function) term).getState());

			}
			
			if  (e.getSecondTerm() instanceof Function) {
				System.out.println( "" + e.getSecondTerm() + ((Function) e.getSecondTerm()).getState());

				for (Term term: ((Function) e.getSecondTerm()).arguments)
					if (term instanceof Function)
						System.out.println("" + term +  ((Function) term).getState());

			}
		}
	}
}
