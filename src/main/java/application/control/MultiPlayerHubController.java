package application.control;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import application.model.networking.TDA.Lobby;
import application.model.networking.subrutines.ConnectionServer;
import application.model.networking.subrutines.GetLobby;
import application.model.networking.subrutines.JoinLobby;
import application.model.networking.subrutines.ResponseServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MultiPlayerHubController implements Initializable {

	private static ConnectionServer cs;
	private static ResponseServer rs;
	private static JoinLobby jl;

	@FXML
	private VBox list2v2;
	@FXML
	private VBox list4;
	private ToggleGroup tg2v2;
	private ToggleGroup tg4;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		tg2v2 = new ToggleGroup();
		tg4 = new ToggleGroup();

		// get2v2Lobby();
		// get4Lobby();

	}

	public String showLobbyDialog(String title, Stage s) {

		TextInputDialog textdialog = new TextInputDialog();
		textdialog.setHeaderText(title);
		textdialog.setTitle("Info");

		Optional<String> result = textdialog.showAndWait();
		if (result.isPresent()) {
			return textdialog.getResult();
		} else {
			return null;
		}

	}

	@FXML
	public void get2v2Lobby() {
		new Thread(new GetLobby(Lobby.people2, list2v2, tg2v2), "GetLobby").start();
	}

	@FXML
	public void get4Lobby() {
		new Thread(new GetLobby(Lobby.people4, list4, tg4), "GetLobby").start();
	}

	@FXML
	public void create2v2lobby() throws IOException {

		String lobbyName = showLobbyDialog("Inserisci nome della lobby", Main.getPrimaryStage());

		if (lobbyName != null) {
			Lobby l = new Lobby(lobbyName, Lobby.people2);

			rs = new ResponseServer(l);
			cs = new ConnectionServer(rs, 1);

			Main.changeStage("LoadingPage.fxml");

			new Thread(rs, "ResponseServer").start();
			new Thread(cs, "ConnectionsServer").start();
		}

	}

	@FXML
	public void create4lobby() throws IOException {

		String lobbyName = showLobbyDialog("Inserisci nome della lobby", Main.getPrimaryStage());

		if (lobbyName != null) {
			Main.changeStage("LoadingPage.fxml");
			Lobby l = new Lobby(lobbyName, Lobby.people4);

			rs = new ResponseServer(l);
			cs = new ConnectionServer(rs, 3);

			new Thread(rs, "ResponseServer").start();
			new Thread(cs, "ConnectionsServer").start();
		}

	}

	@FXML
	public void join2v2Lobby() throws IOException {

		if (tg2v2.getSelectedToggle() != null) {
			Main.changeStage("LoadingPage.fxml");
			Lobby l = (Lobby) tg2v2.getSelectedToggle().getUserData();
			jl = new JoinLobby(l);
			new Thread(jl, "JoinLobby").start();
		}

	}

	@FXML
	public void join4Lobby() throws IOException {

		if (tg4.getSelectedToggle() != null) {
			Main.changeStage("LoadingPage.fxml");
			Lobby l = (Lobby) tg4.getSelectedToggle().getUserData();
			jl = new JoinLobby(l);
			new Thread(jl, "JoinLobby").start();
		}

	}

	@FXML
	public void cancel() throws IOException {

		if (cs != null && !cs.isClosed()) {
			cs.close();
		}
		if (rs != null && !rs.isClosed()) {
			rs.close();
		}
		if (jl != null && !jl.isDisconnected()) {
			jl.disconnect();
		}

		Main.changeStage("MultiPlayerHub.fxml");

	}

	@FXML
	public void returnButton() throws IOException {
		Main.changeStage("MainMenu.fxml");
	}

}
