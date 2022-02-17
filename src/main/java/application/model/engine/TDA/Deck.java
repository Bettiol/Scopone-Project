package application.model.engine.TDA;

/**
 * La classe rappresenta un mazzo di 40 carte
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */

public class Deck {
	private Carta mazzo[];
	private int dim;

	/**
	 * Metodo costruttore della classe
	 */
	public Deck() {
		super();
		this.mazzo = new Carta[40];
		dim = 40;
		this.generaMazzo();
		this.mischiaMazzo();
	}

	/**
	 * Il metodo genera il mazzo da gioco classico formato da 10 carte per ogniuno
	 * dei 4 semi
	 */
	private void generaMazzo() {
		Carta app;
		int i = 0;
		for (i = 1; i < 41; i++) {
			if (i < 11) {
				app = new Carta("danaro", i);
			} else if (i < 21) {
				app = new Carta("bastone", i - 10);
			} else if (i < 31) {
				app = new Carta("spada", i - 20);
			} else {
				app = new Carta("coppa", i - 30);
			}
			mazzo[i - 1] = app;
		}
	}

	/**
	 * Il metodo mette in disordine il mazzo
	 */
	private void mischiaMazzo() {
		Carta app;
		int rand;
		for (int i = 0; i < 40; i++) {
			rand = (int) (Math.random() * 40);
			app = mazzo[rand];
			mazzo[rand] = mazzo[i];
			mazzo[i] = app;
		}
	}

	@Override
	public String toString() {
		String s = "mazzo:\n";
		for (int i = 0; i < dim; i++) {
			s = s + mazzo[i] + "\n";
		}
		return s;
	}

	/**
	 * Il metodo elimina logicamente l'ultima carta dal mazzo
	 * 
	 * @return la carta eliminata dal mazzo
	 */
	public Carta pescaCarta() {
		dim--;
		return mazzo[dim];
	}
}
