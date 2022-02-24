package application.model.engine.subrutines;

import application.model.engine.types.cards.CardDictionary;
import application.model.engine.types.cards.Card;
import javafx.scene.image.ImageView;

/**
 * La classe gestisce l'aggiornamento di una ImageView
 * 
 * @author Moscatelli
 *
 */
public class RefreshPlayed implements Runnable {

	private ImageView iv;
	private Card c;
	private CardDictionary cd;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param iv ImageView
	 * @param c  Carta
	 * @param cd Dizionario delle carte
	 */
	public RefreshPlayed(ImageView iv, Card c, CardDictionary cd) {
		super();
		this.iv = iv;
		this.c = c;
		this.cd = cd;
	}

	/**
	 * Aggiorna l'imageView
	 */
	@Override
	public void run() {
		iv.setImage(cd.cardToImage(c));
	}

}
