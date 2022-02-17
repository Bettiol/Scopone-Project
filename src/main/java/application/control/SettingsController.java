package application.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import application.model.engine.TDA.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
	private CheckBox fullScreen;

	@FXML
	private HBox tables;
	@FXML
	private HBox decks;

	private ToggleGroup tgTables;
	private ToggleGroup tgDecks;

	private Settings oldSettings;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		oldSettings = Main.settings.copy();

		// Tavoli
		ImageView iv;
		RadioButton rb;
		tgTables = new ToggleGroup();
		File[] tavoli = new File(Main.class.getResource("assets/tables/").getPath()).listFiles();
		System.out.println(Main.class.getResource("assets/tables/").getPath());
		for (File file : tavoli) {
			iv = new ImageView(new Image("file:" + file.getPath()));
			iv.setFitWidth(150);
			iv.setFitHeight(150);
			// iv.setPreserveRatio(true);

			rb = new RadioButton();
			rb.setGraphic(iv);
			rb.setUserData(file);

			// System.out.println(tavoli[i]);
			if (file.equals(Main.settings.getTable())) {
				rb.setSelected(true);
			}
			// rb.getStyleClass().add("radio-button");
			tgTables.getToggles().add(rb);

			tables.getChildren().add(rb);
		}
		tgTables.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println(newValue.getUserData());
			Main.settings.setTable((URL) newValue.getUserData());
		});

		// Immagini
		File[] carte = new File(Main.class.getResource("assets/cards/").getPath()).listFiles();
		tgDecks = new ToggleGroup();
		for (File file : carte) {
			if (file.isDirectory()) {
				iv = new ImageView(new Image("file:" + file.getPath() + File.separator + "1_danaro.png"));
				iv.setFitWidth(150);
				iv.setFitHeight(150);
				iv.setPreserveRatio(true);

				rb = new RadioButton();
				rb.setGraphic(iv);
				rb.setUserData(file);
				if (file.equals(Main.settings.getDeck())) {
					rb.setSelected(true);
				}
				tgDecks.getToggles().add(rb);

				decks.getChildren().add(rb);
			}
		}
		// tgDecks.se
		tgDecks.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			Main.settings.setDeck((URL) newValue.getUserData());
		});

		// Volume
		mainVolume.setValue(Main.settings.getVolume() * 100);
		mainVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
			Main.settings.setVolume(newValue.doubleValue() / 100);
			Main.mainTheme.volumeProperty().set(Main.settings.getVolume());
		});

		// Blasfemia
		blasphemy.setSelected(Main.settings.isBlasphemy());
		blasphemy.selectedProperty().addListener((observable, oldValue, newValue) -> {
			Main.settings.setBlasphemy(newValue);
		});

		fullScreen.setSelected(Main.settings.isFullScreen());
		fullScreen.selectedProperty().addListener((observable, oldValue, newValue) -> {
			Main.settings.setFullScreen(newValue);
		});
	}

	@FXML
	public void applyChanges() {
		oldSettings = Main.settings.copy();
		Main.settings.saveSettings();
	}

	@FXML
	public void restoreDefaults() {
		Main.settings.defaultSettings();
		oldSettings = Main.settings.copy();

		ObservableList<Toggle> ol;
		RadioButton rb;
		ol = tgTables.getToggles();
		for (int i = 0; i < ol.size(); i++) {
			rb = (RadioButton) ol.get(i);
			if (rb.getUserData().equals(Main.settings.getTable())) {
				rb.setSelected(true);
				break;
			}
		}
		ol = tgDecks.getToggles();
		for (int i = 0; i < ol.size(); i++) {
			rb = (RadioButton) ol.get(i);
			if (rb.getUserData().equals(Main.settings.getDeck())) {
				rb.setSelected(true);
				break;
			}
		}

		mainVolume.setValue(Main.settings.getVolume() * 100);
		blasphemy.setSelected(Main.settings.isBlasphemy());
	}

	/**
	 * Torna al menù principale
	 * 
	 * @throws IOException
	 */
	@FXML
	public void returnButton() throws IOException {

		if (!Main.settings.equals(oldSettings)) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Ci sono delle modifiche non salvate. Vuoi salvarle?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				applyChanges();
			} else {
				Main.settings = oldSettings;
				Main.mainTheme.volumeProperty().set(Main.settings.getVolume());
			}

		}

		Main.changeStage("MainMenu.fxml");

	}

}
