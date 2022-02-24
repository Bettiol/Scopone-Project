package application.model.engine.types.cards;

import java.util.HashMap;

import javafx.scene.image.Image;

/**
 * Questa classe gestisce l'associazione univoca tra il nome di una carta e la
 * sua corrispettiva immagine. Inoltre questa classe gestisce alcune carte non
 * giocabili, come il retro e quelle trasparenti.
 * 
 * @author Silvello
 *
 */
public class CardDictionary {

	private HashMap<String, Image> cards;
	private Image transparent;
	private Image retro;
	private Image retro90;

	/**
	 * Metodo costruttore. Inizializza le carte di default
	 */
	public CardDictionary() {
		super();
		this.cards = new HashMap<>(40);
		this.transparent = new Image("file:assets/cards/trevigiane/transparent.png");
		this.retro = new Image("file:assets/cards/trevigiane/retro.png");
		this.retro90 = new Image("file:assets/cards/trevigiane/retro90.png");

		this.initialize();
	}

	/**
	 * Associa tutte le carte con le loro rispettive immagini
	 */
	private void initialize() {
		String key;
		for (Suit seme : Suit.values()) {

			for (int j = 1; j <= 10; ++j) {
				key = j + "_" + seme;
				//TODO Settings
				cards.put(key, new Image("file:assets/cards/trevigiane/" + key + ".png"));
			}
		}
	}

	/**
	 * Questo metodo, data una carta, si occupa di ritornare l'immagine
	 * rappresentativa della carta
	 * 
	 * @param c Carta
	 * @return Immagine rappresentativa della carta
	 */
	public Image cardToImage(Carta c) {
		Image i;
		if (c != null) {
			String key = c.getRank() + "_" + c.getSuit();
			i = cards.get(key);
		} else {
			i = transparent;
		}

		return i;
	}

	public Image getTransparent() {
		return transparent;
	}

	public Image getRetro() {
		return retro;
	}

	public Image getRetro90() {
		return retro90;
	}
}
