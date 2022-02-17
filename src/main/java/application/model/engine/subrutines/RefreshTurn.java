package application.model.engine.subrutines;

import javafx.scene.control.Label;

/**
 * La classe gestisce l'aggiornamento delle label che mostrano i turni
 * all'utente
 * 
 * @author Bettiol
 * @author Silvello
 *
 */
public class RefreshTurn implements Runnable {

	private Label l_g1;
	private Label l_g2;
	private Label l_g3;
	private Label l_g4;
	private int turn;

	/**
	 * 
	 * Metodo costruttore della classe
	 * 
	 * @param l_g1 Label di G1
	 * @param l_g2 Label di G2
	 * @param l_g3 Label di G3
	 * @param l_g4 Label di G4
	 * @param turn Turno corrente
	 */
	public RefreshTurn(Label l_g1, Label l_g2, Label l_g3, Label l_g4, int turn) {
		this.l_g1 = l_g1;
		this.l_g2 = l_g2;
		this.l_g3 = l_g3;
		this.l_g4 = l_g4;
		this.turn = turn;
	}

	/**
	 * Il metodo cambia lo sfondo della label del giocatore di turno
	 */
	@Override
	public void run() {

		l_g1.getStyleClass().removeAll("turn", "activeTurn");
		l_g2.getStyleClass().removeAll("turn", "activeTurn");
		l_g3.getStyleClass().removeAll("turn", "activeTurn");
		l_g4.getStyleClass().removeAll("turn", "activeTurn");
		if (turn == 0) {
			l_g1.getStyleClass().add("activeTurn");
			l_g2.getStyleClass().add("turn");
			l_g3.getStyleClass().add("turn");
			l_g4.getStyleClass().add("turn");
		} else if (turn == 1) {
			l_g1.getStyleClass().add("turn");
			l_g2.getStyleClass().add("activeTurn");
			l_g3.getStyleClass().add("turn");
			l_g4.getStyleClass().add("turn");
		} else if (turn == 2) {
			l_g1.getStyleClass().add("turn");
			l_g2.getStyleClass().add("turn");
			l_g3.getStyleClass().add("activeTurn");
			l_g4.getStyleClass().add("turn");
		} else if (turn == 3) {
			l_g1.getStyleClass().add("turn");
			l_g2.getStyleClass().add("turn");
			l_g3.getStyleClass().add("turn");
			l_g4.getStyleClass().add("activeTurn");
		}

	}
}