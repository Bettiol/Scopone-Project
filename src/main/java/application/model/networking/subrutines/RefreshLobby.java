package application.model.networking.subrutines;

import java.util.ArrayList;

import application.model.networking.TDA.Lobby;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

/**
 * Questa classe si occupa di aggiornare la lista delle lobby attive
 * 
 * @author Moscatelli
 * 
 * @see Lobby
 *
 */
public class RefreshLobby implements Runnable {

	private Pane view;
	private ToggleGroup target;
	private ArrayList<Lobby> arr;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param view   Pannello che deve essere aggionato
	 * @param target ToggleGroup dei RadioButton
	 * @param arr    Lista delle lobby
	 */
	public RefreshLobby(Pane view, ToggleGroup target, ArrayList<Lobby> arr) {
		super();
		this.view = view;
		this.target = target;
		this.arr = arr;
	}

	/**
	 * Questo medoto aggiorna la lista delle lobby attive, creando dei RadioButton
	 * con associati un oggetto Lobby
	 */
	@Override
	public void run() {
		RadioButton rb;

		ObservableList<Node> ol = view.getChildren();
		ObservableList<Toggle> toggles = target.getToggles();
		// Hanno la stessa lunghezza
		for (int i = 0; i < ol.size(); i++) {
			ol.remove(i);
			toggles.remove(i);
		}
		// target = new ToggleGroup();
		for (int i = 0; i < arr.size(); i++) {
			rb = new RadioButton(arr.get(i).getName());
			rb.setUserData(arr.get(i));
			target.getToggles().add(rb);
			view.getChildren().add(rb);
		}
	}

}
