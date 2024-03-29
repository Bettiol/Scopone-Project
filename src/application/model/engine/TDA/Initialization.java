package application.model.engine.TDA;

import java.io.Serializable;

/**
 * 
 * Questa classe serve per passare i dati di inizializzazione ai singoli
 * giocatori
 * 
 * @author Moscatelli
 *
 */
public class Initialization implements Serializable {

	private static final long serialVersionUID = 4493340833434233188L;
	private int whoAmI;
	private int turn;
	private Carta[] hand;
	private String[] playerNames;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param whoAmI      Chi � il giocatore rispetto al tavolo
	 * @param turn        Chi � il primo di mano
	 * @param hand        Mano iniziale del giocatore
	 * @param playerNames Nomi degli altri giocatori
	 */
	public Initialization(int whoAmI, int turn, Carta[] hand, String[] playerNames) {
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

	public Carta[] getHand() {
		return hand;
	}

	public String[] getPlayerNames() {
		return playerNames;
	}

}
