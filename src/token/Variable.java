package token;

import index.Substitution;
import java.util.Set;
import java.util.Vector;

/**
 * Variable class
 * In this project all the variables are universally quantified!
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 */
public class Variable extends Term {
	
	/**
	 * Value of the variable
	 */
	private Term value;
	
	/**
	 * Variable constructor
	 * @param symbol: identify the variable
	 */
	public Variable(String symbol) {
		super(symbol);
		
		// The value of the variable isn't assign
		this.value = null;
	}

	@Override
	public String toString() {
		if (this.isInizialized())
			return this.getValue().toString();
		else
			return this.getSymbol();
	}

	@Override
	public boolean contains(Term term) {
		// Se this è inizializzata e term non è una Variable
		if (this.isInizialized() && !(term instanceof Variable)) 
			return this.getValue().contains(term);
		// Da qui in poi rientriamo in casi in cui term è Variable
		
		// Entrambe inizializzate
		else if ((term instanceof Variable) && (this.isInizialized()) && ((Variable) term).isInizialized()) 
			return this.getValue().contains(((Variable) term).getValue());
		
		// La prima è inizializzata e term non è inizializzata
		else if ((term instanceof Variable) && (this.isInizialized()) && !(((Variable) term).isInizialized()))
			return this.getValue().contains(term);
		
		// La prima non è inizializzata e term sì
		else if ((term instanceof Variable) && !(this.isInizialized()) && (((Variable) term).isInizialized()))
			return false;
		
		// Entrambe non inizializzate
		else if ((term instanceof Variable) && !(this.isInizialized()) && !(((Variable) term).isInizialized()))
			return (this.getSymbol().equals(term.getSymbol()));
		
		return false;
	}

	@Override
	public Variable clone() {
		Variable result = new Variable(this.getSymbol());
		if (this.isInizialized())
			result.setValue(this.getValue().clone());
		return result;
	}
	
	// Forse va bene così
	@Override
	public void replaceWith(Variable toReplace, Term substitution) {
		if ((!this.isInizialized()) && (toReplace.equals(this)))
			this.setValue(substitution);
		else if (this.isInizialized())
			this.getValue().replaceWith(toReplace, substitution);
	}

	@Override
	public boolean equals(Object term) {
		if (term instanceof Variable) {
			if ((this.isInizialized()) && ((Variable) term).isInizialized())
				return this.getValue().equals(((Variable) term).getValue());
			else if (!(this.isInizialized()) && !((Variable) term).isInizialized())
				return ((Variable) term).getSymbol().equals(this.getSymbol());
			else if (!(this.isInizialized()) && ((Variable) term).isInizialized())
				return this.equals(((Variable) term).getValue());
			else if ((this.isInizialized()) && !((Variable) term).isInizialized())
				return this.getValue().equals(term);
			else
				return false;
		} else {
			if (this.isInizialized())
				return this.getValue().equals(term);
			else 
				return false;
		}
	}
	
	/**
	 * Assign the value of the variable
	 * @param term
	 */
	public void setValue(Term term) {
		this.value = term;
	}
	
	/**
	 * Returns the variable value
	 * @return
	 */
	public Term getValue() {
		return this.value;
	}
	
	/**
	 * Return true if this has a value, false in other cases
	 * @return true if this has a value, false in other cases
	 */
	public boolean isInizialized() {
		return this.value == null ? false : true;
	}

	@Override
	public int isRPOGreater(Term term) {
		if ((term instanceof Variable) && (!((Variable) term).isInizialized()) && !(this.isInizialized()))
			return -1; // Non confrontabili
		else if ((this.isInizialized()) && (term instanceof Variable) && !(((Variable) term).isInizialized()))
			return this.value.contains(term) ? 1 : -1; // Controllo il contenimento
		else if ((this.isInizialized()) && (term instanceof Variable) && (((Variable) term).isInizialized()))
			return this.value.isRPOGreater(((Variable) term).getValue()); // Mi richiamo su RPO
		else if (!(this.isInizialized()) && (term instanceof Variable) && (((Variable) term).isInizialized()))
			return -1;
		else if ((this.isInizialized()) && (term instanceof Constant))
			return this.getValue().isRPOGreater(term);
		else if ((this.isInizialized()) && !(term instanceof Variable))
			return (this.value.contains(term)) ? 1 : -1;
		
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

		if (!sub.isEmpty()) {
			Set<Term> variables = sub.keySet();

			// this isn't inizialized
			if (!this.isInizialized()) {
				for (Term var: variables) {
					if (((Variable) var).equals(this)) 
						this.setValue(sub.get(var));
				}
				// this is inizialized
			} else {
				if (this.isInizialized()) {
					//for (Term var: variables) {
					//if (((Variable) var).equals(this)) 
					this.getValue().applySubstitution(sub);
				}
			}
		}
	}
	//}

	@Override 
	public Term substituteSubterm(Term subterm, Term to_substitute) {
		if (this.isInizialized()) 
			this.getValue().substituteSubterm(subterm, to_substitute);
		else {
			// Aggiunto questo
			this.setValue(to_substitute);
		}
		return null;
	}

	@Override
	public int weight() {
		if (this.isInizialized()) 
			return this.getValue().weight();
		else
			return 2;
	}

	@Override
	public boolean isGround() {
		return this.isInizialized() ? this.getValue().isGround() : false;
	}

	@Override
	public void changeVariable() {
		if (!(this.isInizialized()))
			this.setSymbol(this.getSymbol() + "1");
		else
			this.getValue().changeVariable();
	}
}
