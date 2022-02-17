package application.model.networking.subrutines;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import application.model.networking.UDPSocket;
import application.model.networking.TDA.Lobby;
import application.model.networking.TDA.Message;
import application.model.networking.TDA.Packet;
import javafx.application.Platform;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

/**
 * 
 * Questa classe ha il compito di ottenere tutte le lobby presenti in una rete
 * locale e di far partire il thread di aggiornamento grafico
 * 
 * @author Moscatelli
 * 
 * @see RefreshLobby
 * @see Runnable
 *
 */
public class GetLobby implements Runnable {

	/**
	 * Tempo massimo di attesa
	 */
	private static final int waitingTime = 5000;

	private int type;
	private Pane view;
	private ToggleGroup tg;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param type Tipo della lobby
	 * @param view Pane che deve essere aggiornato
	 * @param tg   ToggleGroup dei RadioButton
	 */
	public GetLobby(int type, Pane view, ToggleGroup tg) {
		super();
		this.type = type;
		this.view = view;
		this.tg = tg;
	}

	/**
	 * Questo metodo crea un socket adibito all'ascolto dei pacchetti in arrivo alla
	 * porta desiderata; dopo un determinato numero di secondo dove non riceve alcun
	 * pacchetto, richiama il metodo per aggiornare il pannello delle lobby attive.
	 */
	@Override
	public void run() {
		UDPSocket us = null;
		try {
			us = new UDPSocket();
			us.broadcast(ResponseServer.responsePort, new Message(Message.IS_SOMEONE_THERE, Integer.valueOf(type)));
			us.setSoTimeout(waitingTime);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Packet p = null;
		boolean killed = false;
		Lobby l;
		Message m;
		ArrayList<Lobby> servers = new ArrayList<Lobby>(10);
		while (!killed) {
			try {
				p = us.receive();
				if (p != null) {
					m = p.getMsg();
					if (m.getType() == Message.IM_THERE) {
						l = (Lobby) m.getMsg().getObj();
						if (l.getLobbyType() == type) {
							l.setIp(p.getIp());
							l.setPort(p.getPort());
							servers.add(l);
						}
					}
				}
				p = null;
			} catch (SocketTimeoutException e) {
				killed = true;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		us.close();

		Platform.runLater(new RefreshLobby(view, tg, servers));
	}

}
