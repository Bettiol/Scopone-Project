package application.model.engine.TDA;

/**
 * La classe rappresenta le funzione obbligatorie che ogni tavolo deve
 * provvedere ad implementare
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */
public abstract class Table {
	/**
	 * Il metodo aggiunge un giocatore al tavolo
	 * 
	 * @param p Player da aggiungere al tavolo
	 */
	public abstract void addPlayer(Player p);

	/**
	 * Il metodo assegna le carte ai giocatori, fa eseguire i 40 turni e infine
	 * calcola i punti
	 */
	public abstract void run();

	/**
	 * Il metodo controllo e assegna le carte prese alla squadra del turno corrente
	 * se non possibile aggiunge la carta in tavolo
	 * 
	 * @param giocata la carta scartata dal giocatore
	 * @return 1 per verificare la giusta esecuzione del metodo
	 */
	public abstract int playCard(Carta giocata);

	/**
	 * Il metodo effettua la presa se in presenza di più combos di carte
	 * 
	 * @param presa vettore di carte da prendere dal tavolo
	 * @return 0 per verificare la giusta esecuzione del metodo
	 */
	public abstract int choiceCapture(Carta[] presa);

}
