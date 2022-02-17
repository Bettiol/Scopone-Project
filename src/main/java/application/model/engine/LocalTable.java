package application.model.engine;

import java.util.ArrayList;
import java.util.Arrays;

import application.model.engine.types.cards.Carta;
import application.model.engine.types.cards.Deck;
import application.model.engine.types.GameSettings;
import application.model.engine.types.Initialization;
import application.model.engine.types.Player;
import application.model.engine.types.Points;
import application.model.engine.types.Table;
import application.model.engine.types.cards.Semi;

/**
 * La classe rappresente il tavolo di gioco e il mazziere e posside come sue
 * caratteristiche, attributi: le carte in tavola le carte di ogni giocatore e
 * la loro associazione le carte prese dalle squadre i punti totalizzati da
 * ciascuna squadra
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */

public class LocalTable extends Table implements Runnable {
	// Carte in tavola
	private Carta[] table; // 40
	private int dimTable;
	private int turn;

	// Giocatori
	private Player[] myPlayers; // 4
	private int players;

	// Carte e punti dei giocatori
	private Carta[][] playersHands; // 4;10
	private int[] handSizes; // 4
	private ArrayList<Carta> teamOnePicks;
	private ArrayList<Carta> teamTwoPicks;
	private int teamOnePoints;
	private int teamTwoPoints;
	private ArrayList<Carta> lastPick; // Puntatore all'array con l'ultima presa
	private int teamOneTotal;
	private int teamTwoTotal;

	private GameSettings cfg;

	/**
	 * Metodo costruttore della classe, nel quale si inizializzano le carte del
	 * tavolo e dei giocatori, i giocatori, i punti e l'ultima presa
	 */
	public LocalTable(GameSettings cfg) {
		table = new Carta[8];
		dimTable = 0;
		turn = 0;

		myPlayers = new Player[4];
		players = 0;

		playersHands = new Carta[4][10];
		handSizes = new int[4];
		teamOnePicks = new ArrayList<Carta>(40);
		teamTwoPicks = new ArrayList<Carta>(40);
		lastPick = null;
		teamOnePoints = 0;
		teamTwoPoints = 0;
		
		teamOneTotal=0;
		teamTwoTotal=0;

		this.cfg = cfg;
	}

	@Override
	public void run() {
		Deck m = new Deck();
		int allWentOk;
		// Genero casualmente il turno
		turn = (int) Math.floor(Math.random() * 3);
		
		do {
			//Genera il mazzo
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 10; j++) {
					playersHands[i][j] = m.pescaCarta();
				}
				Arrays.sort(playersHands[i]);
				handSizes[i] = 10;
			}

			// Dico ai player chi sono
			int app = turn;
			Initialization pippo = null;
			for (int i = 0; i < 4; i++) {
				pippo = new Initialization(i, app, playersHands[i], null);
				allWentOk = myPlayers[i].init(pippo);
				// System.out.println("turn : "+pippo.getTurn()+" whoamI : "+pippo.getWhoAmI());
				// System.out.println("Player(" + i + ") + turno " + app);
				if (allWentOk == -1) {
					myPlayers[i] = new AI(this, true, cfg.isAssoPigliatutto(), cfg.isReBello());
					System.out.println("Non una bella partenza per player(" + i + ")");
				}

				// app = (app + 1) % 4;
			}

			// Inizia a giocare
			int i = 0;
			while (i < 40) {
				turn = turn % 4;
				/*
				 * System.out.print(myPlayers[turn] instanceof RemotePlayer ? "Turno remoto" :
				 * myPlayers[turn] instanceof AI ? "Turno IA" : "Turno locale");
				 * System.out.println(" (" + turn + ")");
				 */
				allWentOk = myPlayers[turn].setPlayerTurn(playersHands[turn], handSizes[turn], table, dimTable);
				if (allWentOk != -1) {
					for (int j = 0; j < 4; j++) {
						// System.out.println("turno 56 :" + turn);
						allWentOk = myPlayers[j].notifyTableState(table, dimTable);
						if (allWentOk == -1) {
							System.out.println("Qualcosa è andato storto nella notifica al player " + j);
							myPlayers[j] = new AI(this, true, cfg.isAssoPigliatutto(), cfg.isReBello());
						}
					}

					turn++;
					i++;
				} else {
					System.out.println("Player " + turn + " non ha fatto nulla");
					myPlayers[turn] = new AI(this, true, cfg.isAssoPigliatutto(), cfg.isReBello());
				}

			}

			assignLastPick();

			Points[] result = stampaPunti();
			teamOnePoints=result[0].calcolaTotale();
			teamTwoPoints=result[1].calcolaTotale();

			// Notifico a tutti la fine del match
			if(cfg.getPointLimit()==0) {
				for (i = 0; i < 4; i++) {
					allWentOk = myPlayers[i].showResult(result);
					if (allWentOk == -1) {
						System.out.println("Qualcosa è andato storto nella notifica a player " + i);
					}
				}
			}
			else {
				//da finire con le schermate di caricamento
			}
			turn=(turn+1)%4;
		}while(cfg.getPointLimit()<teamOneTotal && cfg.getPointLimit()<teamTwoTotal);
		// System.out.println(result);
	}

	@Override
	public void addPlayer(Player p) {
		myPlayers[players] = p;
		players++;
	}

	@Override
	public int playCard(Carta giocata) {
		// Faccio i conti delle combo;

		// Tolgo la carta dalla mano del giocatore e me la prendo
		Carta c = scarta(giocata);
		// Ciclo tutti i giocatori per comunicare quale carto ho giocato
		for (int i = 0; i < players; i++) {
			myPlayers[i].setPlayedCard(c);
		}

		// i giocatori vedono la carta che ho giocato
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Calcolo la presa
		if (c.getNum() == 1 && cfg.isAssoPigliatutto()) {
			//TODO Messo a null ma meglio ricontrollare
			assignPick(c, null);
			lastPick.add(c);
		} else {
			Carta res = ricercaCartaT(c);
			if (res != null) {
				assignPick(c, res);
				lastPick.add(c);
			} else {
				Carta[][] combos = ricercaCombinazioni(c);

				if (combos[0][0] != null) {
					// Ho almeno una combinazione
					if (combos[1][0] == null) {
						// Solo una possibilità
						assignPick(combos[0]);
					} else {
						// L'utente sceglie
						return myPlayers[turn].pickChoice(combos);
					}
					lastPick.add(c);
				} else {
					// Appoggia la carta
					table[dimTable] = c;
					dimTable++;
				}
			}
		}

		return 1;
		// Forse va messo fuori
		// myPlayers[turn].notifyTableState(table, dimTable);
	}

	@Override
	public int choiceCapture(Carta[] combos) {
		// Ok faccio cose col la tua scelta e aggiorno il tavolo
		assignPick(combos);
		// Muoio e faccio tornare alla funzione precedente?

		return 0;
	}

	/**
	 * Il metodo ritorna il vettore contenente le carte prese dalla squadra 1
	 * 
	 * @return il vettore contenente le carte prese dalla squadra 1
	 */
	public ArrayList<Carta> getTeamOnePicks() {
		return teamOnePicks;
	}

	/**
	 * Il metodo ritorna il vettore contenente le carte prese dalla squadra 2
	 * 
	 * @return il vettore contenente le carte prese dalla squadra 2
	 */
	public ArrayList<Carta> getTeamTwoPicks() {
		return teamTwoPicks;
	}

	/**
	 * Il metodo ricerca una carta in un vettore e ne ritorna l'indice, se non
	 * presente -1
	 * 
	 * @param arr vettore su cui effettuare la ricerca
	 * @param dim dimensione logica del vettore
	 * @param x   carta di cui effettuare la ricerca
	 * @return l'indice del vettore
	 */
	private int cardToIndex(Carta[] arr, int dim, Carta x) {
		int i = 0;
		boolean found = false;

		while (i < dim && !found) {
			if (arr[i].equals(x)) {
				found = true;
			} else {
				i++;
			}
		}

		if (found) {
			return i;
		} else {
			return -1;
		}

	}

	/**
	 * Il metodo scarta una carta dal vettore delle certe del giocatore del turno
	 * corrente
	 * 
	 * @param c carta da scartare
	 * @return carta scartata
	 */
	private Carta scarta(Carta c) {
		int i;
		int pos = cardToIndex(playersHands[turn], handSizes[turn], c);

		Carta app = playersHands[turn][pos];

		for (i = pos; i < handSizes[turn] - 1; i++) {
			playersHands[turn][i] = playersHands[turn][i + 1];
		}
		handSizes[turn]--;

		return app;
	}

	/**
	 * Il metodo elimina una carta presente sul tavolo e ne aggiorna la dimensione
	 * 
	 * @param c carta da eliminare
	 * @return carta eliminata
	 */
	private Carta scartaTavolo(Carta c) {
		int pos = cardToIndex(table, dimTable, c);
		Carta app = table[pos];

		for (int i = pos; i < dimTable - 1; i++) {
			table[i] = table[i + 1];
		}
		dimTable--;

		return app;
	}

	/**
	 * Il metodo aggiunge la carta scartata e le carte prese dal tavolo al mazzo
	 * delle carte prese della squadra corretta
	 * 
	 * @param calata carta calata in tavola
	 * @param presa  carta presa dalla tavola
	 */
	private void assignPick(Carta calata, Carta presa) {
		if (turn == 0 || turn == 2) {
			if (calata.getNum() == 1 && cfg.isAssoPigliatutto()) {
				for (int i = dimTable - 1; i >= 0; i--) {
					teamOnePicks.add(scartaTavolo(table[i]));
				}
			} else {
				teamOnePicks.add(scartaTavolo(presa));
				if (dimTable == 0) {
					teamOnePoints++;
				}
			}
			// teamOnePicks.add(calata);

			lastPick = teamOnePicks;
		} else {
			if (calata.getNum() == 1 && cfg.isAssoPigliatutto()) {
				for (int i = dimTable - 1; i >= 0; i--) {
					teamTwoPicks.add(scartaTavolo(table[i]));
				}
			} else {
				teamTwoPicks.add(scartaTavolo(presa));
				if (dimTable == 0) {
					teamTwoPoints++;
				}
			}
			// teamTwoPicks.add(calata);

			lastPick = teamTwoPicks;
		}
	}

	/**
	 * Il metodo aggiunge al vettore corretto delle carte prese un insieme di carte
	 * acquisite dalla scuadra
	 * 
	 * @param prese vettore di carte da aggiungere
	 */
	private void assignPick(Carta[] prese) {
		int i = 0;
		if (turn == 0 || turn == 2) {
			while (i < 3 && prese[i] != null) {
				teamOnePicks.add(scartaTavolo(prese[i]));
				i++;
			}
			if (dimTable == 0) {
				teamOnePoints++;
			}
			// teamOnePicks.add(calata);

			lastPick = teamOnePicks;
		} else {
			while (i < 3 && prese[i] != null) {
				teamTwoPicks.add(scartaTavolo(prese[i]));
				i++;
			}
			if (dimTable == 0) {
				teamTwoPoints++;
			}
			// teamTwoPicks.add(calata);

			lastPick = teamTwoPicks;
		}
	}

	/**
	 * Il metodo ricerca una carta del tavolo di pari valore rispetto a quella
	 * indicata nel parametro
	 * 
	 * @param c carta di cui effettuare la ricerca
	 * @return la carta del tavolo
	 */
	private Carta ricercaCartaT(Carta c) {
		int pos = -1;
		Carta app = null;
		for (int i = 0; i < dimTable; i++) {
			if (table[i].getNum() == c.getNum()) {
				pos = i;
			}
		}

		if (pos > -1) {
			app = table[pos];
		}
		return app;
	}

	/**
	 * Il metodo ricerca una combos di carte pari al valore di quella indicata nel
	 * parametro, tutte le celle della matrice di ritorno che non sono coinvolte
	 * sono poste a null
	 * 
	 * @param c carta di cui effettuare la ricerca
	 * @return matrice contenete le combos delle carte
	 */
	private Carta[][] ricercaCombinazioni(Carta c) {
		// il vettore contiene le posizione della somma posssibile
		Carta posizioni[][] = new Carta[5][3];
		// da finire
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				posizioni[i][j] = null;
			}
		}

		int somma;
		int l = 0;
		// analizzo tutte le somme che posso comporre
		for (int i = 0; i < dimTable; i++) {
			somma = table[i].getNum();
			for (int j = i; j < dimTable; j++) {
				// salto tutte le somme con la stessa carta
				if (i != j) {
					somma = somma + table[j].getNum();
					// se la somma che ottengo è corretta salvo le posizioni
					if (somma == c.getNum()) {
						posizioni[l][0] = table[i];
						posizioni[l][1] = table[j];
						posizioni[l][2] = null;
						l++;
					} else if (somma < c.getNum()) {
						// se la somma è minore del valore della carta somma una terza carta
						for (int k = j; k < dimTable; k++) {
							// salto tutte le carte che ho già sommato
							if (k != i && k != j) {
								somma = somma + table[k].getNum();
								if (somma == c.getNum()) {
									posizioni[l][0] = table[i];
									posizioni[l][1] = table[j];
									posizioni[l][2] = table[k];
									l++;
								}
								somma = somma - table[k].getNum();
							}
						}
					}
					somma = somma - table[j].getNum();
				}
			}
		}
		return posizioni;
	}

	/**
	 * Il metodo assegna le carte rimanenti a fine partita alla squadra che ha
	 * effettuato l'ultima presa
	 */
	private void assignLastPick() {
		for (int i = dimTable - 1; i >= 0; i--) {
			lastPick.add(scartaTavolo(table[i]));
		}
	}

	/**
	 * Il metodo interagisce e assegna i punti di ciascuna squadra
	 * 
	 * @return il vettore contenete i punti delle due squadre
	 */
	private Points[] stampaPunti() {
		Points[] squadra = new Points[2];

		squadra[0] = new Points();
		squadra[1] = new Points();

		squadra[0].setScope(teamOnePoints);
		squadra[1].setScope(teamTwoPoints);
		if (trovaSetteBello(teamOnePicks) == true) {
			squadra[0].setSettebello(true);
			squadra[1].setSettebello(false);
		} else {
			squadra[0].setSettebello(false);
			squadra[1].setSettebello(true);
		}
		if (trovaReBello(teamOnePicks) == true && cfg.isReBello()) {
			squadra[0].setRebello(true);
			squadra[1].setRebello(false);
		} else if(cfg.isReBello()){
			squadra[0].setRebello(false);
			squadra[1].setRebello(true);
		}
		if (trovaDanari(teamOnePicks) > 5) {
			squadra[0].setDanari(true);
			squadra[1].setDanari(false);
		} else if (trovaDanari(teamTwoPicks) > 5) {
			squadra[0].setDanari(false);
			squadra[1].setDanari(true);
		} else {
			squadra[0].setDanari(false);
			squadra[1].setDanari(false);
		}
		if(cfg.isNapoli()) {
			int nap = trovaNapoli(teamOnePicks);
			if (nap != 0) {
				squadra[0].setNapoli(nap);
				squadra[1].setNapoli(0);
			}
			nap = trovaNapoli(teamTwoPicks);
			if (nap != 0) {
				squadra[0].setNapoli(0);
				squadra[1].setNapoli(nap);
			}
		}
		if (teamOnePicks.size() > teamTwoPicks.size()) {
			squadra[0].setCarte(true);
			squadra[1].setCarte(false);
		} else if (teamTwoPicks.size() > teamOnePicks.size()) {
			squadra[0].setCarte(true);
		} else {
			squadra[0].setCarte(false);
			squadra[1].setCarte(false);
		}
		if (calcolaPremiera(teamOnePicks) > calcolaPremiera(teamTwoPicks)) {
			squadra[0].setPrimiera(true);
			squadra[1].setPrimiera(false);
		} else if (calcolaPremiera(teamTwoPicks) > calcolaPremiera(teamOnePicks)) {
			squadra[0].setPrimiera(false);
			squadra[1].setPrimiera(true);
		} else {
			squadra[0].setPrimiera(false);
			squadra[1].setPrimiera(false);
		}

		return squadra;
	}

	/**
	 * Il metodo determina se si è conquistato il sette di danari
	 * 
	 * @param cartePrese il puntatore alle carte prese della squadra analizzata
	 * @return se è presente il sette di danari
	 */
	private boolean trovaSetteBello(ArrayList<Carta> cartePrese) {
		boolean trovato = false;
		for (int i = 0; i < cartePrese.size(); i++) {
			if (cartePrese.get(i).getNum() == 7 && cartePrese.get(i).getSeme().compareTo(Semi.DENARI) == 0) {
				trovato = true;
			}
		}
		return trovato;
	}

	/**
	 * Il metodo determina se si è conquistato il re di danari
	 * 
	 * @param cartePrese il puntatore alle carte prese della squadra analizzata
	 * @return se è presente il re di danari
	 */
	private boolean trovaReBello(ArrayList<Carta> cartePrese) {
		boolean trovato = false;
		for (int i = 0; i < cartePrese.size(); i++) {
			if (cartePrese.get(i).getNum() == 10 && cartePrese.get(i).getSeme().compareTo(Semi.DENARI) == 0) {
				trovato = true;
			}
		}
		return trovato;
	}

	/**
	 * Il metodo determina se si è conquistato la maggioranza di danari
	 * 
	 * @param cartePrese il puntatore alle carte prese della squadra analizzata
	 * @return se è presente la maggioranza di danari
	 */
	private int trovaDanari(ArrayList<Carta> cartePrese) {
		int trovato = 0;
		for (int i = 0; i < cartePrese.size(); i++) {
			if (cartePrese.get(i).getSeme().compareTo(Semi.DENARI) == 0) {
				trovato++;
			}
		}
		return trovato;
	}

	/**
	 * Il metodo calcola il punteggio della napoli
	 * 
	 * @param cartePrese il puntatore alle carte prese della squadra analizzata
	 * @return il punteggio della napoli
	 */
	private int trovaNapoli(ArrayList<Carta> cartePrese) {
		int punti = 0;
		if (cartePrese.contains(new Carta(Semi.DENARI, 1))) {
			if (cartePrese.contains(new Carta(Semi.DENARI, 2))) {
				if (cartePrese.contains(new Carta(Semi.DENARI, 3))) {
					punti = 3;
					if (cartePrese.contains(new Carta(Semi.DENARI, 4))) {
						punti++;
						if (cartePrese.contains(new Carta(Semi.DENARI, 5))) {
							punti++;
							if (cartePrese.contains(new Carta(Semi.DENARI, 6))) {
								punti++;
								if (cartePrese.contains(new Carta(Semi.DENARI, 7))) {
									punti++;
									if (cartePrese.contains(new Carta(Semi.DENARI, 8))) {
										punti++;
										if (cartePrese.contains(new Carta(Semi.DENARI, 9))) {
											punti++;
											if (cartePrese.contains(new Carta(Semi.DENARI, 10))) {
												punti++;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return punti;
	}

	/**
	 * Calcola il punteggio della premiera
	 * 
	 * @param cartePrese il puntatore alle carte prese della squadra analizzata
	 * @return il punteggio della premiera
	 */
	private int calcolaPremiera(ArrayList<Carta> cartePrese) {
		int tot = 0;
		int pDanari = 0;
		int pCoppe = 0;
		int pSpade = 0;
		int pBastoni = 0;
		int app = 0;
		for (int i = 0; i < cartePrese.size(); i++) {
			if (cartePrese.get(i).getNum() == 7) {
				app = 21;
			} else if (cartePrese.get(i).getNum() == 6) {
				app = 18;
			} else if (cartePrese.get(i).getNum() == 1) {
				app = 16;
			} else if (cartePrese.get(i).getNum() == 5) {
				app = 15;
			} else if (cartePrese.get(i).getNum() == 4) {
				app = 14;
			} else if (cartePrese.get(i).getNum() == 3) {
				app = 13;
			} else if (cartePrese.get(i).getNum() == 2) {
				app = 12;
			} else {
				app = 10;
			}
			if (cartePrese.get(i).getSeme().compareTo(Semi.DENARI) == 0) {
				if (app > pDanari) {
					pDanari = app;
				}
			} else if (cartePrese.get(i).getSeme().compareTo(Semi.SPADE) == 0) {
				if (app > pSpade) {
					pSpade = app;
				}
			} else if (cartePrese.get(i).getSeme().compareTo(Semi.COPPE) == 0) {
				if (app > pCoppe) {
					pCoppe = app;
				}
			} else {
				if (app > pBastoni) {
					pBastoni = app;
				}
			}

		}
		tot = pDanari + pCoppe + pSpade + pBastoni;
		return tot;
	}
	
}
