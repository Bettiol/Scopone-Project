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

	private ArrayList<Carta> deck;
	/**
	 * Metodo costruttore della classe
	 */
	public Deck() {
		super();
		this.deck = new ArrayList<>(40);
		this.generaMazzo();
		this.mischiaMazzo();
	}

	/**
	 * Il metodo genera il mazzo da gioco classico formato da 10 carte per ogniuno
	 * dei 4 semi
	 */
	private void generaMazzo() {
		for(Semi seme : Semi.values()) {
			for (int i = 1; i <= 10; ++i) {
				deck.add(new Carta(seme, i));
			}
		}
	}

	/**
	 * Il metodo mette in disordine il mazzo
	 */
	private void mischiaMazzo() {
		Collections.shuffle(deck);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("mazzo:\n");
		for (Carta carta : deck) {
			s.append(carta).append("\n");
		}
		return s.toString();
	}

	/**
	 * Il metodo elimina logicamente l'ultima carta dal mazzo
	 * 
	 * @return la carta eliminata dal mazzo
	 */
	public Carta pescaCarta() {
		return deck.remove(deck.size() - 1);
	}
}
