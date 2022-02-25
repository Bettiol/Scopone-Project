package application.model.engine.types;

import application.model.engine.types.cards.Card;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * Questa classe serve per passare i dati di inizializzazione ai singoli
 * giocatori
 * 
 * @author Moscatelli
 *
 */
public class Initialization implements Serializable {

	@Serial
	private static final long serialVersionUID = 4493340833434233188L;

	private int whoAmI;
	private int turn;
	private ArrayList<Card> hand;
	private ArrayList<String> playerNames;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param whoAmI      Chi è il giocatore rispetto al tavolo
	 * @param turn        Chi è il primo di mano
	 * @param hand        Mano iniziale del giocatore
	 * @param playerNames Nomi degli altri giocatori
	 */
	public Initialization(int whoAmI, int turn, ArrayList<Card> hand, ArrayList<String> playerNames) {
		super();
		this.whoAmI = whoAmI;
		this.turn = turn;
		this.hand = hand;
		this.playerNames = playerNames;
	}

	public int getWhoAmI() {
		return whoAmI;
	}

	public int getTurn() {
		return turn;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public ArrayList<String> getPlayerNames() {
		return playerNames;
	}

}
