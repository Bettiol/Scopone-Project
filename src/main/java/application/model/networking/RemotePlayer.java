package application.model.networking;

import java.io.IOException;
import java.util.ArrayList;

import application.model.engine.LocalTable;
import application.model.engine.types.cards.Card;
import application.model.engine.types.Initialization;
import application.model.engine.types.Player;
import application.model.engine.types.Points;
import application.model.networking.TDA.Message;
import application.model.networking.TDA.State;

/**
 * 
 * La classe rappresenta un'entità player collegata in remoto al tavolo tramite
 * una connessione TCP
 * 
 * @author Moscatelli
 *
 */
public class RemotePlayer extends Player {

	private TCPConnection host;
	private LocalTable table;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param host  Connessione TCP all'host
	 * @param table tavolo a cui il giocatore è collegato
	 */
	public RemotePlayer(TCPConnection host, LocalTable table) {
		this.host = host;
		this.table = table;
		table.addPlayer(this);
	}

	@Override
	public int init(Initialization in) {
		Message send = new Message(Message.HELLO_THERE, in);
		try {
			host.send(send);
		} catch (IOException | NullPointerException e) {
			return -1;
		}

		return 1;
	}

	@Override
	public int setPlayerTurn(ArrayList<Card> handCards, ArrayList<Card> tableCards) {
		Message send;
		State[] state = new State[2];
		state[0] = new State(handCards.toArray(new Card[0]), handCards.size());
		state[1] = new State(tableCards.toArray(new Card[0]), tableCards.size());

		send = new Message(Message.SET_TURN, state);
		try {
			host.send(send);
		} catch (IOException e) {
			return -1;
		}

		Card giocata = null;
		Message m = null;
		try {
			m = host.receive();
		} catch (ClassNotFoundException | IOException e) {
			return -1;
		}

		if (m == null) {
			return -1;
		}

		if (m.getType() == Message.CHOICE) {
			giocata = (Card) m.getMsg().getObj();
		}

		return table.playCard(giocata);
	}

	@Override
	public int pickChoice(Card[][] combos) {
		Message send = new Message(Message.CHOICE, combos);
		try {
			host.send(send);
		} catch (IOException e) {
			return -1;
		}

		Message m = null;
		Card[] risp = null;
		try {
			m = host.receive();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		if (m == null) {
			return -1;
		}

		if (m.getType() == Message.CHOICE) {
			risp = (Card[]) m.getMsg().getObj();
		}

		return table.choiceCapture(risp);
	}

	@Override
	public int notifyTableState(Card[] tableCards, int dimTable) {
		Message send = new Message(Message.STATE, new State(tableCards, dimTable));
		try {
			host.send(send);
		} catch (IOException e) {
			return -1;
		}

		return 1;
	}

	@Override
	public int setPlayedCard(Card c) {
		Message send = new Message(Message.CARD_PLAYED, c);
		try {
			host.send(send);
		} catch (IOException e) {
			return -1;
		}

		return 1;
	}

	@Override
	public int showResult(Points[] squadra) {
		Message send = new Message(Message.END_MATCH, squadra);
		try {
			host.send(send);
		} catch (IOException e) {
			return -1;
		}

		try {
			// Chiudo la connessione
			host.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 1;
	}

}
