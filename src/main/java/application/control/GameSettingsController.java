package application.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import application.model.engine.TDA.GameSettings;
import application.model.engine.subrutines.LoadTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

public class GameSettingsController implements Initializable {

	@FXML
	private CheckBox secca;
	@FXML
	private CheckBox reBello;
	@FXML
	private CheckBox napoli;
	@FXML
	private CheckBox asso;
	@FXML
	private Slider punti;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		secca.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				punti.setVisible(!newValue);
			}
		});

	}

	@FXML
	public void startMatch() {
		int p;
		if (secca.isSelected()) {
			p = 0;
		} else {
			p = (int) punti.getValue();
		}

		GameSettings cfg = new GameSettings(p, asso.isSelected(), reBello.isSelected(), napoli.isSelected());
		new LoadTable(null, cfg).run();
	}

	@FXML
	public void returnButton() {
		try {
			Main.changeStage("MainMenu.fxml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
