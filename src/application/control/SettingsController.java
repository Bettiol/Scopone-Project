package application.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import application.model.engine.TDA.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SettingsController implements Initializable {

	@FXML
	private Slider mainVolume;
	@FXML
	private CheckBox blasphemy;

	@FXML
	private HBox tables;
	@FXML
	private HBox decks;

	private ToggleGroup tgTables;
	private ToggleGroup tgDecks;

	private Settings oldSettings;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		oldSettings = Main.settings;

		// Tavoli
		ImageView iv;
		RadioButton rb;
		tgTables = new ToggleGroup();
		File[] tavoli = new File("resources/tables/").listFiles();
		for (int i = 0; i < tavoli.length; i++) {
			iv = new ImageView(new Image("file:" + tavoli[i].getPath()));
			iv.setFitWidth(150);
			iv.setFitHeight(150);

			rb = new RadioButton();
			rb.setGraphic(iv);
			rb.setContentDisplay(ContentDisplay.BOTTOM);
			rb.setUserData(tavoli[i]);

			// System.out.println(tavoli[i]);
			if (tavoli[i].equals(Main.settings.getTable())) {
				rb.setSelected(true);
			}
			// rb.getStyleClass().add("radio-button");
			tgTables.getToggles().add(rb);

			tables.getChildren().add(rb);
		}
		tgTables.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				System.out.println(newValue.getUserData());
				Main.settings.setTable((File) newValue.getUserData());

			}
		});

		// Immagini
		File[] carte = new File("resources/cards/").listFiles();
		tgDecks = new ToggleGroup();
		for (int i = 0; i < carte.length; i++) {
			if (carte[i].isDirectory()) {
				iv = new ImageView(new Image("file:" + carte[i].getPath() + File.separator + "1_danaro.png"));
				iv.setFitWidth(150);
				iv.setFitHeight(150);
				iv.setPreserveRatio(true);

				rb = new RadioButton();
				rb.setGraphic(iv);
				rb.setContentDisplay(ContentDisplay.BOTTOM);
				rb.setUserData(carte[i]);
				if (carte[i].equals(Main.settings.getDeck())) {
					rb.setSelected(true);
				}
				tgDecks.getToggles().add(rb);

				decks.getChildren().add(rb);
			}
		}
		// tgDecks.se
		tgDecks.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				Main.settings.setDeck((File) newValue.getUserData());

			}
		});

		// Volume
		mainVolume.setValue(Main.settings.getVolume() * 100);
		mainVolume.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Main.settings.setVolume(newValue.doubleValue() / 100);
				Main.mainTheme.volumeProperty().set(Main.settings.getVolume());
			}
		});

		// Blasfemia
		blasphemy.setSelected(Main.settings.isBlasphemy());
		blasphemy.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				Main.settings.setBlasphemy(newValue);
			}
		});

	}

	@FXML
	public void applyChanges() {
		Main.settings.saveSettings();
	}

	/**
	 * Torna al menù principale
	 * 
	 * @throws IOException
	 */
	@FXML
	public void returnButton() throws IOException {
		Main.settings.saveSettings();
		Main.changeStage("MainMenu.fxml");
	}

}
