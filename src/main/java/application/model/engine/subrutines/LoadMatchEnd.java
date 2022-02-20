package application.model.engine.subrutines;

import java.io.IOException;

import application.Root;
import application.control.MatchEndController;
import application.model.engine.types.Points;

/**
 * 
 * La classe gestisce la fine della partita tra i giocatori, cambiando lo stage
 * e richiamnado il metodo per terminare la partita.
 * 
 * @author Bettiol
 * @author Silvello
 *
 */
public class LoadMatchEnd implements Runnable {

	private Points[] squadre;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param squadre Punti rappresentativi delle due squadre
	 */
	public LoadMatchEnd(Points[] squadre) {
		super();
		this.squadre = squadre;
	}

	/**
	 * Calcola i punti e cambia lo stage della partita nel recap dei punti.
	 */
	@Override
	public void run() {
		try {
			MatchEndController mec = (MatchEndController) Root.changeStage("MatchEnd.fxml");
			mec.endMatch(squadre);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
