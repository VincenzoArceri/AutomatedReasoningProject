package index;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import com.rits.cloning.Cloner;

import token.*;

/**
 * Class used to represent a substitution
 * @author <a href="mailto:vincenzoarceri.92@gmail.com"> Vincenzo Arceri </a>
 */
public class Substitution extends HashMap<Term, Term> {
	
	/**
	 * Substitution constructor
	 */
	public Substitution() {
		super();
	}
	
	/**
	 * Equals
	 */
	@Override
	public boolean equals(Object obj) {
		System.out.println("Entrato");
		
		Cloner cloner = new Cloner();
		
		HashMap<Term, Term> thisCopy = cloner.deepClone(this);
		HashMap<Term, Term> otherCopy = cloner.deepClone(((HashMap<Term, Term>) obj));
			
		
		Set<Term> keyValueThis = thisCopy.keySet();
		Term[] keyValueOther = (Term[]) otherCopy.keySet().toArray(new Term[otherCopy.keySet().size()]);
		Vector<String> stringKeyValueOther = new Vector<String>();
		
		for (int i = 0; i < keyValueOther.length; i++)
			stringKeyValueOther.add(keyValueOther[i].toString());
		
		HashMap<Term, Term> temp = cloner.deepClone(this);
		
		for(Term thisKey : keyValueThis) {
			System.out.println(((stringKeyValueOther.contains(thisKey.toString()))));
			System.out.println((thisCopy.get(thisKey).equals(otherCopy.get(thisKey))));
			if ((stringKeyValueOther.contains(thisKey.toString())) && (thisCopy.get(thisKey).equals(otherCopy.get(thisKey)))) {
				otherCopy.remove(thisKey);
				temp.remove(thisKey);
			}
		}
		
		if ((temp.isEmpty()) && (otherCopy.isEmpty()))
			return true;
		else
			return false;
	}
}
