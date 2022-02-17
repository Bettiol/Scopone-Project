package application.model.networking.subrutines;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import application.model.engine.subrutines.LoadScene;
import application.model.engine.subrutines.LoadTable;
import application.model.networking.TCPConnection;
import application.model.networking.TDA.Lobby;
import application.model.networking.TDA.Message;
import javafx.application.Platform;

/**
 * La classe si occupa del collegamento tra una lobby ed un giocatore che ha
 * scelto di giocare in essa Una volta connesso, si occupa anche di rilevare se
 * l'host della partita si � sconnesso prima dell'inizio di questa e di
 * comunicare al view di aggirnarsi.
 * 
 * @author Moscatelli
 *
 */
public class JoinLobby implements Runnable {

	private Lobby target;
	private TCPConnection tc;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param target Lobby alla qual voglio collegarmi
	 */
	public JoinLobby(Lobby target) {
		super();
		this.target = target;
	}

	/**
	 * All'avvio del metodo questo cerca di instaurare una connessione TCP con l'ip
	 * che fa da host alla lobby. In caso di insuccesso ritorna al men�; in caso di
	 * successo rimane in attesa del messaggio di inizio della partita. Se l'host si
	 * disconnette durante l'attesa il metodo ricarica la pagina del men�.
	 */
	@Override
	public void run() {
		Socket host = null;

		try {
			host = new Socket(target.getIp(), target.getPort());
		} catch (ConnectException e) {
			Platform.runLater(new LoadScene("MultiPlayerHub.fxml"));
			System.out.println("Errore di connessione all'host desiderato");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (host != null) {
			tc = new TCPConnection(host);
			boolean init = false;
			boolean crashed = false;
			Message m;

			while (!init && !crashed) {
				try {
					m = tc.receive();
					if (m.getType() == Message.START_MATCH) {
						init = true;
					}
				} catch (IOException e) {
					crashed = true;
					System.out.println("Host disconnected");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			if (!crashed) {
				//TODO Platform.runLater(new LoadTable(tc, true));
			} else {
				Platform.runLater(new LoadScene("MultiPlayerHub.fxml"));

				try {
					tc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Disconnette e chiude il collegamento TCP
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		tc.close();
	}

	/**
	 * Ritorna se il socket � stato disconnesso
	 * 
	 * @return true se il socket � disconnesso
	 */
	public boolean isDisconnected() {
		return tc.isClosed();
	}
}
