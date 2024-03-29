package application.model.engine;

import java.util.concurrent.Semaphore;

import application.control.ControllerCardView;
import application.model.engine.TDA.Carta;
import application.model.engine.TDA.Initialization;
import application.model.engine.TDA.Player;
import application.model.engine.TDA.Points;
import application.model.engine.TDA.Table;
import application.model.engine.subrutines.LoadMatchEnd;
import javafx.application.Platform;

/**
 * La classe rappresenta un Player che necessita di un interazione con l'utente
 * per effttuare alcune operazione di scelta della carta o della combos, inoltre
 * questo giocatore risiede nella stessa memoria in cui � presente il table
 * 
 * @author Brognera
 * @author Moscatelli
 */
public class HumanPlayer extends Player {

	private Table table;
	private ControllerCardView ccv;
	private Semaphore input;

	private int whoAmI;
	private int turn;

	private Carta lastPlayed;
	private int scopeS1;
	private int scopeS2;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param table tavolo a cui il giocatore � collegato
	 * @param ccv   il controller per l'interzione con l'interfaccia grafica
	 */
	public HumanPlayer(Table table, ControllerCardView ccv) {
		this.table = table;
		this.ccv = ccv;
		this.input = ccv.getInput();
		table.addPlayer(this);
	}

	@Override
	public int init(Initialization in) {
		turn=in.getTurn();
		whoAmI=in.getWhoAmI();
		//System.out.println("turn : "+turn+" whoamI : "+whoAmI);
		turn = ((((4 - whoAmI) % 4) + in.getTurn()) % 4);
		//System.out.println("Turno enrico :"+turn);
		ccv.refreshTurn(turn);

		ccv.refreshHand(in.getHand(), 10);

		scopeS1 = 0;
		scopeS2 = 0;

		return 1;
	}

	@Override
	public int setPlayerTurn(Carta[] hand, int dimHand, Carta[] tableCards, int dimTable) {
		// Aggiorno il view
		ccv.refreshHand(hand, dimHand);
		ccv.refreshTable(tableCards, dimTable);

		ccv.refreshTurn(0);

		// input dell'utente
		ccv.setClickEvent(true);
		try {
			input.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int card = ccv.getGiocata().intValue();

		Carta[] newHand = scarta(hand, dimHand, card);
		ccv.refreshHand(newHand, (dimHand - 1));

		return table.playCard(hand[card]);
	}

	@Override
	public int setPlayedCard(Carta c) {
		lastPlayed = c;

		ccv.refreshPlayed(lastPlayed);

		return 1;
	}

	@Override
	public int pickChoice(Carta[][] combos) {
		// Viasulizzo a schermo il vettore di scelte come mi pare
		ccv.refreshCombos(combos);

		// input dell'utente
		ccv.setChoiceEvent(true);
		try {
			input.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int index = ccv.getGiocata().intValue();

		return table.choiceCapture(combos[index]);
	}

	@Override
	public int notifyTableState(Carta[] tableCards, int dimTable) {
		//System.out.println("Turno enrico 2:"+turn);
		if (lastPlayed.getNum() != 1 && dimTable == 0) {
			if (turn == 0 || turn == 2) {
				scopeS1++;
				ccv.refreshScope(scopeS1, 1);
			} else if (turn == 1 || turn == 3) {
				scopeS2++;
				ccv.refreshScope(scopeS2, 2);
			}
		}

		ccv.refreshPlayed(null);
		//System.out.println("Turno enrico 3 :"+turn);
		ccv.refreshTable(tableCards, dimTable);
		if (turn != 0) {
			ccv.removeCard(turn);
		}

		turn = (turn + 1) % 4;
		ccv.refreshTurn(turn);

		return 1;
	}

	@Override
	public int showResult(Points[] squadra) {

		Platform.runLater(new LoadMatchEnd(squadra));

		return 1;
	}

	/**
	 * Il metodo effettua una eliminazione logica da un vettore dato l'indice
	 * 
	 * @param arr   vettore su cui effettuare l'eliminazione
	 * @param dim   dimensione logica del vettore
	 * @param index indice da eliminare
	 * @return il vettore modificato
	 */
	private Carta[] scarta(Carta[] arr, int dim, int index) {
		Carta[] app = new Carta[dim];
		for (int i = 0; i < dim; i++) {
			app[i] = arr[i];
		}

		for (int i = index; i < dim - 1; i++) {
			app[i] = app[i + 1];
		}

		return app;
	}
}
