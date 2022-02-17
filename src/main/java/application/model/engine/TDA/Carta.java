package application.model.engine.TDA;

import java.io.Serial;
import java.io.Serializable;

/**
 * La classe specifica gli attributi della carta del mazzo quindi possiede una
 * stringa contente il seme della carta, un intero num il valore della carta. La
 * classe implementa Serializable
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */

public class Carta implements Serializable, Comparable<Carta> {

	@Serial
	private static final long serialVersionUID = 8223232637933818932L;

	private String seme;
	private int num;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param seme il seme a cui appartiene la carta
	 * @param num  il valore della carta
	 */
	public Carta(String seme, int num) {
		super();
		this.seme = seme;
		this.num = num;
	}

	/**
	 * Il metodo ritorna il seme della carta
	 * 
	 * @return la stringa contenente il seme della carta
	 */
	public String getSeme() {
		return seme;
	}

	/**
	 * Il metodo ritorna il valore della carta
	 * 
	 * @return l'intero rappresentante il valore della carta
	 */
	public int getNum() {
		return num;
	}

	@Override
	public String toString() {
		return "seme=" + seme + " num=" + num;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Carta other = (Carta) obj;
		if (num != other.num) {
			return false;
		}
		if (seme == null) {
			if (other.seme != null) {
				return false;
			}
		} else if (!seme.equals(other.seme)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Carta o) {

		if (this.getNum() != o.getNum()) {
			return this.getNum() - o.getNum();
		} else {
			return this.getSeme().compareTo(o.getSeme());
		}

	}

}
