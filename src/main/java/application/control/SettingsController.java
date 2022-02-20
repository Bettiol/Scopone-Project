package application.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Root;
import application.model.engine.types.Settings;
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
		oldSettings = Root.settings.copy();

		// Tavoli
		ImageView iv;
		RadioButton rb;
		tgTables = new ToggleGroup();

		File[] tavoli = new File[0];
		tavoli = new File("assets/tables/").listFiles();
		if (tavoli != null) {
			for (File file : tavoli) {
				iv = new ImageView(new Image("file:" + file.getPath()));
				iv.setFitWidth(150);
				iv.setFitHeight(150);
				// iv.setPreserveRatio(true);

				rb = new RadioButton();
				rb.setGraphic(iv);
				rb.setUserData(file);

				// System.out.println(tavoli[i]);
				if (file.equals(Root.settings.getTable())) {
					rb.setSelected(true);
				}
				// rb.getStyleClass().add("radio-button");
				tgTables.getToggles().add(rb);

				tables.getChildren().add(rb);
			}
			tgTables.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
				System.out.println(newValue.getUserData());
				Root.settings.setTable((File) newValue.getUserData());
			});
		}

		// Immagini
		File[] carte = new File[0];
		carte = new File("assets/cards/").listFiles();
		tgDecks = new ToggleGroup();
		if (carte != null) {
			for (File file : carte) {
				if (file.isDirectory()) {
					iv = new ImageView(new Image("file:" + file.getPath() + File.separator + "1_danaro.png"));
					iv.setFitWidth(150);
					iv.setFitHeight(150);
					iv.setPreserveRatio(true);

					rb = new RadioButton();
					rb.setGraphic(iv);
					rb.setUserData(file);
					if (file.equals(Root.settings.getDeck())) {
						rb.setSelected(true);
					}
					tgDecks.getToggles().add(rb);

					decks.getChildren().add(rb);
				}
			}
			// tgDecks.se
			tgDecks.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
				Root.settings.setDeck((File) newValue.getUserData());
			});
		}

		// Volume
		mainVolume.setValue(Root.settings.getVolume() * 100);
		mainVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
			Root.settings.setVolume(newValue.doubleValue() / 100);
			Root.mainTheme.volumeProperty().set(Root.settings.getVolume());
		});

		// Blasfemia
		blasphemy.setSelected(Root.settings.isBlasphemy());
		blasphemy.selectedProperty().addListener((observable, oldValue, newValue) -> {
			Root.settings.setBlasphemy(newValue);
		});

		fullScreen.setSelected(Root.settings.isFullScreen());
		fullScreen.selectedProperty().addListener((observable, oldValue, newValue) -> {
			Root.settings.setFullScreen(newValue);
		});
	}

	@FXML
	public void applyChanges() {
		oldSettings = Root.settings.copy();
		Root.settings.save(Root.settingsPath);
	}

	@FXML
	public void restoreDefaults() {
		Root.settings.defaultSettings();
		oldSettings = Root.settings.copy();

		ObservableList<Toggle> ol;
		RadioButton rb;
		ol = tgTables.getToggles();
		for (Toggle toggle : ol) {
			rb = (RadioButton) toggle;
			if (rb.getUserData().equals(Root.settings.getTable())) {
				rb.setSelected(true);
				break;
			}
		}
		ol = tgDecks.getToggles();
		for (Toggle toggle : ol) {
			rb = (RadioButton) toggle;
			if (rb.getUserData().equals(Root.settings.getDeck())) {
				rb.setSelected(true);
				break;
			}
		}

		mainVolume.setValue(Root.settings.getVolume() * 100);
		blasphemy.setSelected(Root.settings.isBlasphemy());
	}

	/**
	 * Torna al menù principale
	 * 
	 * @throws IOException
	 */
	@FXML
	public void returnButton() throws IOException {

		if (!Root.settings.equals(oldSettings)) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Ci sono delle modifiche non salvate. Vuoi salvarle?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				applyChanges();
			} else {
				Root.settings = oldSettings;
				Root.mainTheme.volumeProperty().set(Root.settings.getVolume());
			}

		}

		Root.changeStage("MainMenu.fxml");

	}

}
