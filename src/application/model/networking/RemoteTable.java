package application.model.networking;

import java.io.IOException;

import application.model.engine.TDA.Carta;
import application.model.engine.TDA.Initialization;
import application.model.engine.TDA.Player;
import application.model.engine.TDA.Points;
import application.model.engine.TDA.Table;
import application.model.engine.subrutines.LoadScene;
import application.model.networking.TDA.Message;
import application.model.networking.TDA.State;
import javafx.application.Platform;

/**
 * 
 * La classe gestisce l'interazione remota tra un giocatore e il tavolo alla
 * quale � collegato lo stesso
 * 
 * @author Moscatelli
 *
 */
public class RemoteTable extends Table implements Runnable {

	private TCPConnection host;
	private Player myPlayer;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param host Terminale alla quale il giocatore � connesso
	 * @throws IOException
	 */
	public RemoteTable(TCPConnection host) throws IOException {
		super();
		this.host = host;
	}

	@Override
	public void addPlayer(Player p) {
		myPlayer = p;
	}

	@Override
	public void run() {
		Message m = null;
		boolean end = false;

		while (!end) {
			m = null;
			try {
				m = host.receive();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Connessione all'host persa");
			}

			if (m == null) {
				Platform.runLater(new LoadScene("MultiPlayerHub.fxml"));
				end = true;
			} else {

				switch (m.getType()) {
				case Message.CARD_PLAYED:
					Carta card = (Carta) m.getMsg().getObj();
					myPlayer.setPlayedCard(card);
					break;
				case Message.SET_TURN:
					State[] states = (State[]) m.getMsg().getObj();
					myPlayer.setPlayerTurn(states[0].getArr(), states[0].getDim(), states[1].getArr(),
							states[1].getDim());
					break;
				case Message.STATE:
					State table = (State) m.getMsg().getObj();
					myPlayer.notifyTableState(table.getArr(), table.getDim());
					break;
				case Message.CHOICE:
					Carta[][] combos = (Carta[][]) m.getMsg().getObj();
					myPlayer.pickChoice(combos);
				case Message.HELLO_THERE:
					Initialization init = (Initialization) m.getMsg().getObj();
					myPlayer.init(init);
					break;
				case Message.END_MATCH:
					Points[] result = (Points[]) m.getMsg().getObj();
					myPlayer.showResult(result);
					end = true;
					break;
				}
			}

		}

		try {
			// Disconnetto dall'host
			host.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int playCard(Carta card) {
		Message send = new Message(Message.CHOICE, card);
		try {
			host.send(send);
		} catch (IOException e) {
			return -1;
		}

		return 1;
	}

	@Override
	public int choiceCapture(Carta[] combos) {
		Message send = new Message(Message.CHOICE, combos);
		try {
			host.send(send);
		} catch (IOException e) {
			return -1;
		}

		return 1;
	}
}
