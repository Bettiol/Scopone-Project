package application.model.networking.TDA;

import java.io.Serial;
import java.io.Serializable;

import application.model.engine.types.cards.Card;

/**
 * 
 * La classe serve per trasmettere un array di carte
 * 
 * @author Moscatelli
 *
 */
public class State implements Serializable {

	@Serial
	private static final long serialVersionUID = -2757221931344674554L;

	private Card[] arr;
	private int dim;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param arr Array
	 * @param dim Dimensione
	 */
	public State(Card[] arr, int dim) {
		super();
		this.arr = arr;
		this.dim = dim;
	}

	public Card[] getArr() {
		return arr;
	}

	public int getDim() {
		return dim;
	}
}
