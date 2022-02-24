package application.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Root;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Questa classe controller gestisce i bottoni del MainMenu e il ritorno a
 * questo dal view di Rules e Credits. Si occupa inoltre dell'uscita dal
 * programma
 * 
 * @author Bettiol
 * @author Silvello
 *
 */
public class MenuController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Root.playMainTheme("MainMenuTheme.mp3");
	}

	/**
	 * Lancia la funzione per far partire una partita contro 3 IA
	 * 
	 * @throws IOException
	 */
	@FXML
	public void singlePlayerMode() throws IOException {
		Root.changeStage("GameSettingsPage.fxml");

		// new LoadTable(null).run();
	}

	/**
	 * Cambia lo stage nel HUB del multiplayer
	 * 
	 * @throws IOException
	 */
	@FXML
	public void multiPlayerMode() throws IOException {
		Root.changeStage("MultiPlayerHub.fxml");
	}

	/**
	 * Cambia lo stage nella pagina del regolamento
	 * 
	 * @throws IOException
	 */
	@FXML
	public void rulesPage() throws IOException {
		Root.changeStage("RulesPage.fxml");
	}

	/**
	 * Cambia lo stage nella pagina delle opzioni
	 * 
	 * @throws IOException
	 */
	@FXML
	public void settingsPage() throws IOException {
		Root.changeStage("SettingsPage.fxml");
	}

	/**
	 * Chiude il programma
	 * 
	 * @throws IOException
	 */
	@FXML
	public void exitButton() {
		System.exit(0);
	}

	/**
	 * Torna al men� principale
	 * 
	 * @throws IOException
	 */
	@FXML
	public void returnButton() throws IOException {
		Root.changeStage("MainMenu.fxml");
	}
}