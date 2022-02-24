package application.model.engine.types;

import application.model.engine.types.cards.Card;

import java.util.ArrayList;

/**
 * La classe rappresenta le funzione obbligatorie che ogni Player deve
 * provvedere ad implementare
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */
public abstract class Player {

	/**
	 * Il metodo identifica la tua posizione e il tuo turno in tavola
	 * 
	 * @param in Parametri per l'inizializzazione del view
	 * @return 1 per verificare la giusta esecuzione del metodo
	 */
	public abstract int init(Initialization in);

	/**
	 * Il metodo seleziona tra le carte in mano quale giocare e ne effettua la
	 * giocata
	 * 
	 * @param handCards     il vettore di carte che il giocatore ha in mano
	 * @param tableCards    il vettore contente le carte presenti in tavola
	 * @return il risultato della giocata della carta selezionata
	 */
	public abstract int setPlayerTurn(ArrayList<Card> handCards, ArrayList<Card> tableCards);

	/**
	 * Il metodo aggiorna le carte che il giocatore possiede
	 * 
	 * @param c la carta giocata
	 * @return 1 per verificare la giusta esecuzione del metodo
	 */
	public abstract int setPlayedCard(Card c);

	/**
	 * Il metodo seleziona una combos da giocare e ne effettua la presa
	 * 
	 * @param combos la matrice contenente le combos di carte da prendere in tavola
	 * @return il risultato della scelta della combos
	 */
	public abstract int pickChoice(Card[][] combos);

	/**
	 * Il metodo aggiorna il tavolo e il turno di gioco
	 * 
	 * @param tableCards il vettore contenete le carte in tavola
	 * @return 1 per verificare la giusta esecuzione del metodo
	 */
	public abstract int notifyTableState(ArrayList<Card> tableCards);

	/**
	 * Il metodo aggiorna e mostra i punti totalizzati
	 * 
	 * @param squadra l'oggetto contenete i punti totalizzati dalla squadra
	 * @return 1 per verificare la giusta esecuzione del metodo
	 */
	public abstract int showResult(Points[] squadra);
}
