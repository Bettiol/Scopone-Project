package application.model.networking.subrutines;

import java.io.IOException;
import java.net.ServerSocket;

import application.model.engine.subrutines.LoadScene;
import application.model.engine.subrutines.LoadTable;
import application.model.networking.TCPConnection;
import application.model.networking.TDA.Message;
import javafx.application.Platform;

/**
 * 
 * Classe che gestisce la creazione di una lobby e la connessione tra gli host.
 * La classe gestisce, inoltre, i cambi di scena dell'interfaccia
 * 
 * @author Moscatelli
 * 
 * @see java.lang.Runnable
 *
 */

public class ConnectionServer implements Runnable {

	public static final int listeningPort = 10000;

	private ServerSocket ss;
	private ResponseServer rs;
	private TCPConnection[] connections;

	private boolean killed;

	/**
	 * Metodo costruttore della classe.
	 * 
	 * @param rs    Server per la risposta ai broadcast
	 * @param conns Numero di connessioni richieste
	 * @throws IOException
	 */
	public ConnectionServer(ResponseServer rs, int conns) throws IOException {
		this.ss = new ServerSocket(listeningPort);
		// this.ss = new ServerSocket(listeningPort, 20,
		// InetAddress.getByName("25.148.235.191"));
		this.rs = rs;
		this.connections = new TCPConnection[conns];
		this.killed = false;
	}

	/**
	 * All'avvio l'oggetto rimane in ascolto per ottenerre connessioni TCP fino a
	 * riempire l'array delle connessioni. Una volta pieno, comunica a tutti i
	 * giocatori che la partita sta per iniziare e lancia il metodo per caricare il
	 * tavolo
	 */
	@Override
	public void run() {
		int num = 0;

		while (num < connections.length && !killed) {
			try {
				connections[num] = new TCPConnection(ss.accept());
				num++;
			} catch (IOException e) {
				//
			}
		}

		if (!killed) {
			Message m = new Message(Message.START_MATCH, null);
			for (int i = 0; i < connections.length; i++) {
				try {
					connections[i].send(m);
				} catch (IOException e) {
					connections[i] = null;
					System.out.println("Qualcuno si è disconnesso");
					// e.printStackTrace();
				}
			}

			Platform.runLater(new LoadTable(connections));

			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			rs.close();
		} else {
			Platform.runLater(new LoadScene("MultiPlayerHub.fxml"));

			for (int i = 0; i < num; i++) {
				try {
					connections[i].close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			rs.close();
		}
	}

	/**
	 * Il metodo termina il thread e chiude i socket
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		killed = true;
		ss.close();
		rs.close();
	}

	/**
	 * Specifica se il ServerSocket è chiuso o no
	 * 
	 * @return true se il ServerSocket è chiuso
	 */
	public boolean isClosed() {
		return ss.isClosed();
	}

}
