package application.model.engine.TDA;

import java.io.File;
import java.util.HashMap;

import application.Main;
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
		this.transparent = new Image(Main.class.getResourceAsStream("assets/cards/trevigiane/transparent.png"));
		this.retro = new Image(Main.class.getResourceAsStream("assets/cards/trevigiane/retro.png"));
		this.retro90 = new Image(Main.class.getResourceAsStream("assets/cards/trevigiane/retro90.png"));

		this.initialize();
	}

	/**
	 * Associa tutte le carte con le loro rispettive immagini
	 */
	private void initialize() {
		String seme = null;

		String key;
		for (int i = 0; i < 4; i++) {
			if (i == 0) {
				seme = "danaro";
			} else if (i == 1) {
				seme = "bastone";
			} else if (i == 2) {
				seme = "spada";
			} else {
				seme = "coppa";
			}

			for (int j = 0; j < 10; j++) {
				key = (j + 1) + "_" + seme;
				cards.put(key, new Image(Main.class.getResourceAsStream("assets/cards/trevigiane/" + key + ".png")));
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
			String key = c.getNum() + "_" + c.getSeme();
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
