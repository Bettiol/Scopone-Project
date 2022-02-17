package application.model.engine.subrutines;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * La classe gestisce in tempo reale la mano dei giocatori, sostintuendo la
 * carta calata con la carta passata al metodo
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */
public class RemoveCard implements Runnable {

	private Pane g;
	private Image transparent;

	/**
	 * 
	 * Metodo costruttore della classe
	 * 
	 * @param g           Pannello che rappresenta la mano del giocatore
	 * @param transparent Immagine sostitutiva
	 */
	public RemoveCard(Pane g, Image transparent) {
		super();
		this.g = g;
		this.transparent = transparent;
	}

	/**
	 * Questo metodo rende invisibile la prima ImageView disponibile
	 */
	@Override
	public void run() {
		ObservableList<Node> ol = null;
		ImageView iv;

		ol = g.getChildren();

		// Non da 9 perché è stata aggiunta la label
		int i = 10;
		boolean found = false;
		while (i >= 1 && !found) {
			if (ol.get(i) instanceof ImageView) {
				iv = (ImageView) ol.get(i);
				if (!iv.getImage().equals(transparent)) {
					iv.setImage(transparent);
					found = true;
				} else {
					i--;
				}
			}
		}
	}

}
