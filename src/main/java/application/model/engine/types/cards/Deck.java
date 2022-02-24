package application.model.engine.types.cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * La classe rappresenta un mazzo di 40 carte
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */

public class Deck {

	private ArrayList<Card> deck;
	/**
	 * Metodo costruttore della classe
	 */
	public Deck() {
		super();
		this.deck = new ArrayList<>(40);
		this.generaMazzo();
		Collections.shuffle(deck);
	}

	/**
	 * Il metodo genera il mazzo da gioco classico formato da 10 carte per ogniuno
	 * dei 4 semi
	 */
	private void generaMazzo() {
		for(Suit seme : Suit.values()) {
			for (int i = 1; i <= 10; ++i) {
				deck.add(new Card(seme, i));
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("mazzo:\n");
		for (Card carta : deck) {
			s.append(carta).append("\n");
		}
		return s.toString();
	}

	/**
	 * Il metodo elimina logicamente l'ultima carta dal mazzo
	 * 
	 * @return la carta eliminata dal mazzo
	 */
	public Card pescaCarta() {
		return deck.remove(deck.size() - 1);
	}
}
