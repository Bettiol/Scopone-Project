package application.model.engine.subrutines;

import java.io.IOException;

import application.Main;
import application.control.ControllerCardView;
import application.model.engine.AI;
import application.model.engine.HumanPlayer;
import application.model.engine.LocalTable;
import application.model.networking.RemotePlayer;
import application.model.networking.RemoteTable;
import application.model.networking.TCPConnection;

/**
 * La classe gestisce l'avvio di una partita, sia questa in singlepalyer, con
 * pi� host collegati o in remoto.
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */
public class LoadTable implements Runnable {

	private TCPConnection[] arr;
	private boolean remote;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param arr Connessioni con i player esterni
	 */
	public LoadTable(TCPConnection[] arr) {
		super();
		this.arr = arr;
		this.remote = false;
	}

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param host   Connessione con l'host
	 * @param remote Boolean che indica che sono connesso in remoto ad un host
	 */
	public LoadTable(TCPConnection host, boolean remote) {
		super();
		this.arr = new TCPConnection[1];
		this.arr[0] = host;
		this.remote = true;
	}

	/**
	 * Crea e carica il tavolo, collega i giocatori ad esso e fa partire la partita
	 */
	@Override
	public void run() {
		Main.stopMainTheme();

		ControllerCardView c = null;
		try {
			c = (ControllerCardView) Main.changeStage("Table.fxml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!remote) {
			LocalTable table = new LocalTable();

			new HumanPlayer(table, c);
			if (arr == null) {

				/*
				 * new HumanPlayer(table, c); new HumanPlayer(table, c); new HumanPlayer(table,
				 * c);
				 */

				new AI(table);
				new AI(table);
				new AI(table);
			} else if (arr.length == 1) {
				new AI(table);
				new RemotePlayer(arr[0], table);
				new AI(table);
			} else if (arr.length == 3) {
				new RemotePlayer(arr[0], table);
				new RemotePlayer(arr[1], table);
				new RemotePlayer(arr[2], table);
			}

			Thread t = new Thread(table, "Tavolo");
			t.setDaemon(true);
			t.start();
		} else {
			RemoteTable table = null;
			try {
				table = new RemoteTable(arr[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}

			new HumanPlayer(table, c);

			Thread t = new Thread(table, "Tavolo");
			t.setDaemon(true);
			t.start();
		}
	}

}
