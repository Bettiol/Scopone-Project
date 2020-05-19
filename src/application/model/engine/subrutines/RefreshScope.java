package application.model.engine.subrutines;

import javafx.scene.control.Label;

/**
 * Questa classe si occupa di aggiornare le label del view che rappresentano le
 * scope della sqaudra
 * 
 * @author Bettiol
 * @author Silvello
 *
 */
public class RefreshScope implements Runnable {

	private Label l1;
	private Label l2;
	private int num;
	private int squad;

	/**
	 * 
	 * Metodo costruttore della classe
	 * 
	 * @param l1    Label G1
	 * @param l2    Label G2
	 * @param num   Numero di scope
	 * @param squad Identificativo della squadra
	 */
	public RefreshScope(Label l1, Label l2, int num, int squad) {
		super();
		this.l1 = l1;
		this.l2 = l2;
		this.num = num;
		this.squad = squad;
	}

	/**
	 * Aggiorna le due label con il numero delle scope
	 */
	@Override
	public void run() {
		if (squad == 1) {
			String text = "Scope S1: " + num;
			l1.setText(text);
			l2.setText(text);
		} else if (squad == 2) {
			String text = "Scope S2: " + num;
			l1.setText(text);
			l2.setText(text);
		}
	}

}
