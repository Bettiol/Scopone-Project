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
import application.model.engine.types.cards.Suit;

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
	private ArrayList<Carta> table;
	private int turn;

	// Giocatori
	private ArrayList<Player> myPlayers;

	// Carte e punti dei giocatori
	private ArrayList<ArrayList<Carta>> playersHands;
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
		table = new ArrayList<>(8);
		turn = 0;

		myPlayers = new ArrayList<>(4);

		playersHands = new ArrayList<>(4);
		for (int i = 0; i < 4; ++i) {
			playersHands.add(new ArrayList<>(10));
		}

		teamOnePicks = new ArrayList<>(40);
		teamTwoPicks = new ArrayList<>(40);
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
					playersHands.get(i).add(m.pescaCarta());
				}
				playersHands.get(i).sort(null);
			}

			// Dico ai player chi sono
			int app = turn;
			Initialization pippo = null;
			for (int i = 0; i < 4; i++) {
				pippo = new Initialization(i, app, playersHands.get(i).toArray(new Carta[0]), null);
				allWentOk = myPlayers.get(i).init(pippo);
				// System.out.println("turn : "+pippo.getTurn()+" whoamI : "+pippo.getWhoAmI());
				// System.out.println("Player(" + i + ") + turno " + app);
				if (allWentOk == -1) {
					myPlayers.set(i, new AI(this, true, cfg.isAssoPigliatutto(), cfg.isReBello()));
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
				allWentOk = myPlayers.get(turn).setPlayerTurn(playersHands.get(turn).toArray(new Carta[0]), playersHands.get(turn).size(), table.toArray(new Carta[0]), table.size());
				if (allWentOk != -1) {
					for (int j = 0; j < 4; j++) {
						// System.out.println("turno 56 :" + turn);
						Carta[] debug = table.toArray(new Carta[0]);
						allWentOk = myPlayers.get(j).notifyTableState(table.toArray(new Carta[0]), table.size());
						if (allWentOk == -1) {
							System.out.println("Qualcosa è andato storto nella notifica al player " + j);
							myPlayers.set(j, new AI(this, true, cfg.isAssoPigliatutto(), cfg.isReBello()));
						}
					}

					turn++;
					i++;
				} else {
					System.out.println("Player " + turn + " non ha fatto nulla");
					myPlayers.set(turn, new AI(this, true, cfg.isAssoPigliatutto(), cfg.isReBello()));
				}

			}
			assignLastPick();

			Points[] result = stampaPunti();
			teamOnePoints=result[0].calcolaTotale();
			teamTwoPoints=result[1].calcolaTotale();

			// Notifico a tutti la fine del match
			//TODO Provare a mettere questo controllo fuori
			if(cfg.getPointLimit() == 0) {
				for (i = 0; i < 4; i++) {
					allWentOk = myPlayers.get(i).showResult(result);
					if (allWentOk == -1) {
						System.out.println("Qualcosa è andato storto nella notifica a player " + i);
					}
				}
			} else {
				//da finire con le schermate di caricamento
			}

			//Quello che inizia la prossima partita è quello dopo a chi l'ha finita
			turn = (turn + 1) % 4;

			//TODO Penso che la condizione di uscita sia sbagliata
		} while((cfg.getPointLimit() < teamOneTotal && cfg.getPointLimit() < teamTwoTotal));
		// System.out.println(result);
	}

	@Override
	public void addPlayer(Player p) {
		myPlayers.add(p);
	}

	@Override
	public int playCard(Carta giocata) {
		// Faccio i conti delle combo;

		// Tolgo la carta dalla mano del giocatore e me la prendo
		Carta c = scarta(giocata);
		// Ciclo tutti i giocatori per comunicare quale carto ho giocato
		for (Player player : myPlayers) {
			player.setPlayedCard(c);
		}

		// i giocatori vedono la carta che ho giocato
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Calcolo la presa
		if (c.getRank() == 1 && cfg.isAssoPigliatutto()) {
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
						return myPlayers.get(turn).pickChoice(combos);
					}
					lastPick.add(c);
				} else {
					// Appoggia la carta
					table.add(c);
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
	 * Il metodo scarta una carta dal vettore delle certe del giocatore del turno
	 * corrente
	 * 
	 * @param c carta da scartare
	 * @return carta scartata
	 */
	private Carta scarta(Carta c) {
		playersHands.get(turn).remove(c);

		return c;
	}

	/**
	 * Il metodo elimina una carta presente sul tavolo e ne aggiorna la dimensione
	 * 
	 * @param c carta da eliminare
	 * @return carta eliminata
	 */
	private Carta scartaTavolo(Carta c) {
		table.remove(c);

		return c;
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
			if (calata.getRank() == 1 && cfg.isAssoPigliatutto()) {
				for (int i = table.size() - 1; i >= 0; i--) {
					teamOnePicks.add(scartaTavolo(table.get(i)));
				}
			} else {
				teamOnePicks.add(scartaTavolo(presa));
				if (table.isEmpty()) {
					teamOnePoints++;
				}
			}
			// teamOnePicks.add(calata);

			lastPick = teamOnePicks;
		} else {
			if (calata.getRank() == 1 && cfg.isAssoPigliatutto()) {
				for (int i = table.size() - 1; i >= 0; i--) {
					teamTwoPicks.add(scartaTavolo(table.get(i)));
				}
			} else {
				teamTwoPicks.add(scartaTavolo(presa));
				if (table.isEmpty()) {
					teamTwoPoints++;
				}
			}
			// teamTwoPicks.add(calata);

			lastPick = teamTwoPicks;
		}
	}

	/**
	 * Il metodo aggiunge al vettore corretto delle carte prese un insieme di carte
	 * acquisite dalla squadra
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
			if (table.isEmpty()) {
				teamOnePoints++;
			}
			// teamOnePicks.add(calata);

			lastPick = teamOnePicks;
		} else {
			while (i < 3 && prese[i] != null) {
				teamTwoPicks.add(scartaTavolo(prese[i]));
				i++;
			}
			if (table.isEmpty()) {
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
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getRank() == c.getRank()) {
				pos = i;
			}
		}

		if (pos > -1) {
			app = table.get(pos);
		}

		//TODO ??? table.contains(c);

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
		Carta[][] posizioni = new Carta[5][3];
		// da finire
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				posizioni[i][j] = null;
			}
		}

		int somma;
		int l = 0;
		// analizzo tutte le somme che posso comporre
		for (int i = 0; i < table.size(); i++) {
			somma = table.get(i).getRank();
			for (int j = i; j < table.size(); j++) {
				// salto tutte le somme con la stessa carta
				if (i != j) {
					somma = somma + table.get(j).getRank();
					// se la somma che ottengo è corretta salvo le posizioni
					if (somma == c.getRank()) {
						posizioni[l][0] = table.get(i);
						posizioni[l][1] = table.get(j);
						posizioni[l][2] = null;
						l++;
					} else if (somma < c.getRank()) {
						// se la somma è minore del valore della carta somma una terza carta
						for (int k = j; k < table.size(); k++) {
							// salto tutte le carte che ho già sommato
							if (k != i && k != j) {
								somma = somma + table.get(k).getRank();
								if (somma == c.getRank()) {
									posizioni[l][0] = table.get(i);
									posizioni[l][1] = table.get(j);
									posizioni[l][2] = table.get(k);
									l++;
								}
								somma = somma - table.get(k).getRank();
							}
						}
					}
					somma = somma - table.get(j).getRank();
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
		for (int i = table.size() - 1; i >= 0; i--) {
			lastPick.add(scartaTavolo(table.get(i)));
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
		if (trovaSetteBello(teamOnePicks)) {
			squadra[0].setSettebello(true);
			squadra[1].setSettebello(false);
		} else {
			squadra[0].setSettebello(false);
			squadra[1].setSettebello(true);
		}
		if (trovaReBello(teamOnePicks) && cfg.isReBello()) {
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
			if (cartePrese.get(i).getRank() == 7 && cartePrese.get(i).getSuit().compareTo(Suit.DENARI) == 0) {
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
			if (cartePrese.get(i).getRank() == 10 && cartePrese.get(i).getSuit().compareTo(Suit.DENARI) == 0) {
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
			if (cartePrese.get(i).getSuit().compareTo(Suit.DENARI) == 0) {
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
		if (cartePrese.contains(new Carta(Suit.DENARI, 1))) {
			if (cartePrese.contains(new Carta(Suit.DENARI, 2))) {
				if (cartePrese.contains(new Carta(Suit.DENARI, 3))) {
					punti = 3;
					if (cartePrese.contains(new Carta(Suit.DENARI, 4))) {
						punti++;
						if (cartePrese.contains(new Carta(Suit.DENARI, 5))) {
							punti++;
							if (cartePrese.contains(new Carta(Suit.DENARI, 6))) {
								punti++;
								if (cartePrese.contains(new Carta(Suit.DENARI, 7))) {
									punti++;
									if (cartePrese.contains(new Carta(Suit.DENARI, 8))) {
										punti++;
										if (cartePrese.contains(new Carta(Suit.DENARI, 9))) {
											punti++;
											if (cartePrese.contains(new Carta(Suit.DENARI, 10))) {
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
		//TODO Se mi manca un seme devo incularmi
		int tot = 0;
		int pDanari = 0;
		int pCoppe = 0;
		int pSpade = 0;
		int pBastoni = 0;
		int app = 0;
		for (Carta carta : cartePrese) {
			if (carta.getRank() == 7) {
				app = 21;
			} else if (carta.getRank() == 6) {
				app = 18;
			} else if (carta.getRank() == 1) {
				app = 16;
			} else if (carta.getRank() == 5) {
				app = 15;
			} else if (carta.getRank() == 4) {
				app = 14;
			} else if (carta.getRank() == 3) {
				app = 13;
			} else if (carta.getRank() == 2) {
				app = 12;
			} else {
				app = 10;
			}
			if (carta.getSuit().compareTo(Suit.DENARI) == 0) {
				if (app > pDanari) {
					pDanari = app;
				}
			} else if (carta.getSuit().compareTo(Suit.SPADE) == 0) {
				if (app > pSpade) {
					pSpade = app;
				}
			} else if (carta.getSuit().compareTo(Suit.COPPE) == 0) {
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
