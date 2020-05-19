package application.model.networking.subrutines;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import application.model.networking.UDPSocket;
import application.model.networking.TDA.Lobby;
import application.model.networking.TDA.Message;
import application.model.networking.TDA.Packet;

/**
 * 
 * La classe ha il compito di rispondere con a tutte le richieste arrivate dalla
 * classe GetLobby con un oggetto Lobby che rappresenta la lobby localizzate
 * nella macchina
 * 
 * @author Moscatelli
 * 
 * @see application.model.networking.subrutines.ConnectionServer
 * @see application.model.networking.subrutines.GetLobby
 * @see java.lang.Runnable
 *
 */
public class ResponseServer implements Runnable {

	/**
	 * Porta di default per la comunicazione
	 */
	public final static int responsePort = 10000;

	private UDPSocket us;
	private Lobby l;
	private boolean killed;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param l Lobby che deve essere comunicata
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public ResponseServer(Lobby l) throws SocketException, UnknownHostException {
		super();
		this.us = new UDPSocket(responsePort);
		this.l = l;
		killed = false;
	}

	/**
	 * All'avvio questa classe rimane in ascolto sulla porta specificata in attesa
	 * di messaggio di interrogazione. Se ne riceve uno questa risponde con le
	 * coordinate della Lobby.
	 */
	@Override
	public void run() {
		Packet p = null;
		Message m = null;

		Message important = new Message(Message.IM_THERE, l);

		while (!killed) {
			try {
				p = us.receive();
			} catch (ClassNotFoundException | IOException e) {
				// e.printStackTrace();
			}

			if (p != null) {
				m = p.getMsg();
				if (m.getType() == Message.IS_SOMEONE_THERE) {
					Integer type = (Integer) m.getMsg().getObj();
					if (type.intValue() == l.getLobbyType()) {
						try {
							us.sendTo(p.getIp(), p.getPort(), important);
						} catch (IOException e) {
							// e.printStackTrace();
						}
					}

				}
			}

		}

	}

	/**
	 * Chiude il thread e il socket UDP
	 * 
	 */
	public void close() {
		killed = true;
		us.close();
	}

	/**
	 * Ritorna se il socket è chiuso
	 * 
	 * @return true se il socket è chiuso
	 */
	public boolean isClosed() {
		return us.isClosed();
	}

}
