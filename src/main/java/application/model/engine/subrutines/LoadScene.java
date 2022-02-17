package application.model.engine.subrutines;

import java.io.IOException;

import application.Main;

/**
 * 
 * Questa classe ha il compito di caricare nella scena principale un file FXML.
 * 
 * @author Moscatelli
 *
 */
public class LoadScene implements Runnable {

	private String fxml;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param fxml Nome del file FXML
	 */
	public LoadScene(String fxml) {
		super();
		this.fxml = fxml;
	}

	/**
	 * Carica il file FXML specificato
	 */
	@Override
	public void run() {
		try {
			Main.changeStage(fxml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
