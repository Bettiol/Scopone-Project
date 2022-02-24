package application.model.engine.types.cards;

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

public class Card implements Serializable, Comparable<Card> {

	@Serial
	private static final long serialVersionUID = 8223232637933818932L;

	private Suit suit;
	private int rank;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param seme il seme a cui appartiene la carta
	 * @param num  il valore della carta
	 */
	public Card(Suit seme, int num) {
		super();
		this.suit = seme;
		this.rank = num;
	}

	/**
	 * Il metodo ritorna il seme della carta
	 * 
	 * @return la stringa contenente il seme della carta
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Il metodo ritorna il valore della carta
	 * 
	 * @return l'intero rappresentante il valore della carta
	 */
	public int getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return "seme=" + suit + " num=" + rank;
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
		Card other = (Card) obj;
		if (rank != other.rank) {
			return false;
		}
		if (suit == null) {
			if (other.suit != null) {
				return false;
			}
		} else if (!suit.equals(other.suit)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Card o) {

		if (this.getRank() != o.getRank()) {
			return this.getRank() - o.getRank();
		} else {
			return this.getSuit().compareTo(o.getSuit());
		}

	}

}
