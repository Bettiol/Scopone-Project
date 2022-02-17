package application.model.engine.subrutines;

import application.model.engine.TDA.CardDictionary;
import application.model.engine.TDA.Carta;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * La classe gestisce il pannello contente le scelte possibili di una presa.
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */
public class RefreshCombos implements Runnable {

	private Pane table;
	private Pane scelte;

	private Carta[][] combos;
	private CardDictionary cd;

	/**
	 * 
	 * Metodo costruttore della classe
	 * 
	 * @param table  Pannello con il tavolo.
	 * @param scelte Pannello con le scelte
	 * @param combos Matrice con le varie possibilità
	 * @param cd     Dizionario delle carte
	 */
	public RefreshCombos(Pane table, Pane scelte, Carta[][] combos, CardDictionary cd) {
		super();
		this.table = table;
		this.scelte = scelte;
		this.combos = combos;
		this.cd = cd;
	}

	/**
	 * All'avvio il metodo rende invisibile il riquadro del tavolo e rende visibile
	 * quello delle prese. Rende visibile i pannelli con le prese disponibili e ne
	 * aggiorna le carte
	 */
	@Override
	public void run() {
		table.setVisible(false);
		scelte.setVisible(true);

		ObservableList<Node> ol = scelte.getChildren();
		Pane p;
		for (int i = 0; i < 3; i++) {
			p = (HBox) ol.get(i);
			if (combos[i][0] != null) {
				new RefreshCards(p, combos[i], 3, cd).run();
				p.setVisible(true);
			} else {
				p.setVisible(false);
			}
		}
	}

}
