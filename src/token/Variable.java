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
		if (this.isInizialized() && !(term instanceof Variable)) 
			return this.value.contains(term);
		else if ((term instanceof Variable) && (this.isInizialized()) && ((Variable) term).isInizialized()) 
			return this.value.contains(((Variable) term).value);
		else if ((term instanceof Variable) && (this.isInizialized()) && !(((Variable) term).isInizialized()))
			return this.value.contains(term);
		else if ((term instanceof Variable) && !(this.isInizialized()) && (((Variable) term).isInizialized()))
			return false;
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
	
	// CAMBIARE SICURO - da aggiungere il caso in cui la variabile è istanziata
	public void replaceWith(Variable toReplace, Term substitution) {
		if (this.equals(toReplace))
			this.value = substitution;
	}

	@Override
	public boolean equals(Object term) {
		if (!this.isInizialized())
			return (term instanceof Variable) && (this.getSymbol().equals(((Variable) term).getSymbol()));
		else
			return this.getValue().equals(term);
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
					if (var.equals(this)) 
						this.replaceWith(this, sub.get(var));
				}
			// this is inizialized
			} else {
				if (this.isInizialized()) {
					for (Term var: variables) {
						if (var.equals(this)) {
							this.getValue().applySubstitution(sub);
						}
					}
				}
			}
		}
	}
	
	@Override 
	public void substituteSubterm(Term subterm, Term to_substitute) {
		if (this.isInizialized()) 
			this.getValue().substituteSubterm(subterm, to_substitute);
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
}
