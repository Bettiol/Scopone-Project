package application.model.engine;

import java.util.ArrayList;

import application.model.engine.TDA.Carta;
import application.model.engine.TDA.Initialization;
import application.model.engine.TDA.Player;
import application.model.engine.TDA.Points;

/**
 * La classe AI rappresenta un Player che effettua le scelte senza interagire
 * con utenti. La sua funzione è di scegliere la carta migliore da scartare e
 * enventualmente la combos di carte da prendere. La classe per poter attuare la
 * scelta oltre alle carte che possiede in mano ha la possibilità di interagire
 * con le carte scartate da i vari Player
 * 
 * @author Basei
 * @author Brognera
 */

public class AI extends Player {

	private LocalTable table;

	private Carta[] tableCards; // 40
	private int dimTable;

	private Carta[] handCards;
	private int dimHand;

	private ArrayList<Carta> teamOnePicks;
	private ArrayList<Carta> teamTwoPicks;
	
	private boolean assoPiglia=true;
	private boolean reBello=true;

	/**
	 * Metodocostruttore della classe
	 * 
	 * @param table tavolo a cui il giocatore è collegato
	 * @param sub   se deve sostituire un Player che si è disconnesso
	 */
	public AI(LocalTable table, boolean sub) {
		super();
		this.table = table;

		if (!sub) {
			table.addPlayer(this);
		}

	}

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param table tavolo a cui il giocatore è collegato
	 */
	public AI(LocalTable table) {
		this(table, false);
	}

	@Override
	public int init(Initialization in) {
		return 1;
	}

	@Override
	public int setPlayerTurn(Carta[] handCards, int dimHand, Carta[] tableCards, int dimTable) {
		this.tableCards = tableCards;
		this.dimTable = dimTable;
		this.handCards = handCards;
		this.dimHand = dimHand;

		teamOnePicks = table.getTeamOnePicks();
		teamTwoPicks = table.getTeamTwoPicks();

		Carta c = this.pensa();

		return table.playCard(c);
	}

	@Override
	public int setPlayedCard(Carta c) {

		return 1;
	}

	@Override
	public int pickChoice(Carta[][] combos) {
		int index = 0;

		index = choiceCombos(combos);

		return table.choiceCapture(combos[index]);
	}

	@Override
	public int notifyTableState(Carta[] tableCards, int dimTable) {

		return 1;

	}

	@Override
	public int showResult(Points[] squadra) {
		return 1;
	}

	/**
	 * Il metodo permette la ricerca della carta con il maggiore tasso di
	 * giocabilità
	 * 
	 * @return la carta da scartare
	 */
	public Carta pensa() {
		int puntiMax = -1;
		int scarta = 0;
		int punti = 0;
		int p = 0;
		// analizzo ciascuna carta e assegno un punto di giocabilità
		for (int i = 0; i < dimHand; i++) {
			punti = 0;
			// assegno punti fissi all'asso
			if (handCards[i].getNum() == 1 && assoPiglia==true) {
				punti = selezionaPuntiAsso();
			} else {
				// visualizzo se poter far scopa
				punti = this.scopa(handCards[i]);
				// visualizzo se prendere carte dal tavolo
				p = prendiCarta(handCards[i]);
				if (p > punti) {
					punti = p;
				}
				// visualizzo quale carta aggiungere al tavolo
				p = appoggiaCarta(handCards[i]);
				if (p > punti) {
					punti = p;
					// seleziono e salvo la carta con un punteggio migliore
				}
			}
			if (punti > puntiMax) {
				scarta = i;
				puntiMax = punti;
			}
		}
		return handCards[scarta];
	}

	/**
	 * Il metodo analizza se c'è la possibilità di fare scopa e assegna un punteggio
	 * di giocabilità: 99 se possibile 100 se possibile con una carta di danari 0 se
	 * impossibile
	 * 
	 * @param c carta su cui effettuare il controllo
	 * @return il punteggio di giocabilità
	 */
	public int scopa(Carta c) {
		int sommaTavolo = contaPuntiTavolo();
		int p = 0;
		// scopa
		if (sommaTavolo == c.getNum()) {
			p = 99;
			// scopa con danaro
			if (sommaTavolo == c.getNum() && c.getSeme().compareTo("danaro") == 0) {
				p = 100;
			}
		}
		return p;
	}

	/**
	 * Il metodo somma tutti i valori delle carte in tavola
	 * 
	 * @return la somma dei punti nel tavolo
	 */
	public int contaPuntiTavolo() {
		int somma = 0;
		for (int i = 0; i < dimTable; i++) {
			somma = somma + tableCards[i].getNum();
		}
		return somma;
	}

	/**
	 * Il metodo analizza se c'è la possibilità di prendere una carta/combos
	 * presente nel tavolo e assegna un punteggio di giocabilità: 80 se è un 7 (se
	 * danari +3) 75 se è un 2;3 (se danari +3) 72 se è un 10 (se danari +10) 70 se
	 * è un 6 (se danari +3) 60 se è un 4;5;8;9 (se danari +3) 1 se sucessivamente
	 * all'ipotetica presa lascio una possibiltà di scopa in tavola
	 * 
	 * @param c carta su cui effettuare il controllo
	 * @return il punteggio di giocabilità
	 */
	public int prendiCarta(Carta c) {
		int p = 0;
		int pos;
		int i = 0;
		int posizioni[][] = ricercaCombinazioni(c);
		// ricreco se presente una carta da prendere
		pos = ricercaCartaT(c);
		// se sono presenti combinazioni analizzo il loro punteggio
		if (pos != -1) {
			if (c.getNum() == 7) {
				p = p + 80;
			} else if (c.getNum() == 2 || c.getNum() == 3) {
				p = p + 75;
			} else if (c.getNum() == 6) {
				p = p + 70;
			} else if (c.getNum() == 10) {
				p = p + 72;
			} else {
				p = p + 60;
			}
			if (c.getSeme().compareTo("danaro") == 0 || tableCards[pos].getSeme().compareTo("danaro") == 0) {
				p = p + 3;
				if ((c.getNum() == 10 && c.getSeme().compareTo("danaro") == 0 && reBello==true)
						|| (tableCards[pos].getNum() == 10 && tableCards[pos].getSeme().compareTo("danaro") == 0)) {
					p = p + 7;
				}
				if ((c.getNum() == 7 && c.getSeme().compareTo("danaro") == 0)
						|| (tableCards[pos].getNum() == 7 && tableCards[pos].getSeme().compareTo("danaro") == 0)) {
					p = p + 7;
				}
				if ((c.getNum() == 2 && c.getSeme().compareTo("danaro") == 0)
						|| (tableCards[pos].getNum() == 2 && tableCards[pos].getSeme().compareTo("danaro") == 0)) {
					p = p + 7;
				}
				if ((c.getNum() == 3 && c.getSeme().compareTo("danaro") == 0)
						|| (tableCards[pos].getNum() == 3 && tableCards[pos].getSeme().compareTo("danaro") == 0)) {
					p = p + 7;
				}
			}
		} else {
			// ricerco somme di carte che posso prendere
			int appP = 0;
			// se sono presenti combinazioni analizzo il loro punteggio
			if (posizioni[0][0] != -1) {
				while (posizioni[i][0] != -1) {
					if (c.getNum() == 7 || posizioni[i][0] == 7 || posizioni[i][1] == 7 || posizioni[i][2] == 7) {
						appP = appP + 80;
					} else if (c.getNum() == 6 || posizioni[i][0] == 6 || posizioni[i][1] == 6
							|| posizioni[i][2] == 6) {
						appP = appP + 70;
					} else if ((c.getNum() == 10 || posizioni[i][0] == 10 || posizioni[i][1] == 10
							|| posizioni[i][2] == 10 )&& reBello==true) {
						appP = appP + 72;
					} else if (c.getNum() == 2 || c.getNum() == 3 || posizioni[i][0] == 2 || posizioni[i][1] == 2
							|| posizioni[i][2] == 2 || posizioni[i][0] == 3 || posizioni[i][1] == 3
							|| posizioni[i][2] == 3) {
						appP = appP + 75;
					} else {
						appP = appP + 60;
					}
					if (c.getSeme().compareTo("danaro") == 0) {
						appP = appP + 3;
						if (c.getNum() == 10 && reBello==true) {
							appP = appP + 7;
						}
						if (posizioni[i][0] != -1 && tableCards[posizioni[i][0]].getSeme().compareTo("danaro") == 0) {
							appP = appP + 3;
						}
						if (posizioni[i][1] != -1 && tableCards[posizioni[i][1]].getSeme().compareTo("danaro") == 0) {
							appP = appP + 3;
						}
						if (posizioni[i][2] != -1 && tableCards[posizioni[i][2]].getSeme().compareTo("danaro") == 0) {
							appP = appP + 3;
						}
					}
					i++;
					if (appP > p) {
						p = appP;
					}
				}
			}
		}
		// se lascio scopa in tavola
		int somma = contaPuntiTavolo();
		if ((somma - c.getNum() < 11) && (pos != -1 || posizioni[0][0] != -1)
				&& this.carteRimaneti(somma - c.getNum()) != 0) {
			p = 1;
		}
		return p;
	}

	/**
	 * Il metodo analizza la quantità di rischio sucessivamente alla giocata di una
	 * ipotetica carta che non può effettuare alcuna presa dalla tavola e assegna un
	 * punteggio di giocabilità: 55 se le carte di pari valore rimanenti da giocare
	 * sono 0 45 se le carte di pari valore rimanenti da giocare sono 1 35 se le
	 * carte di pari valore rimanenti da giocare sono 2 25 se le carte di pari
	 * valore rimanenti da giocare sono 3 se danari -5 se 7;10;2;3 -20 se
	 * sucessivamente all'ipotetica giocata lascio una possibiltà di scopa in tavola
	 * -30
	 * 
	 * @param c carta su cui effettuare il controllo
	 * @return il punteggio di giocabilità
	 */
	public int appoggiaCarta(Carta c) {
		int p = 0;
		int appP = 0;
		int rimantenti = this.carteRimaneti(c.getNum());
		// analizzo quante carte sono già state giocate
		if (rimantenti == 0) {
			p = 55;
		} else if (rimantenti == 1) {
			p = 45;
		} else if (rimantenti == 2) {
			p = 35;
		} else if (rimantenti == 3) {
			p = 25;
		}
		// analizzo il seme e il valore della carta
		if (c.getSeme().compareTo("danaro") == 0) {
			p = p - 5;
		}
		// decremento vedere se manca re bello, 7 bello...
		if ((c.getNum() == 2 && !this.scesoDueBello()) || (c.getNum() == 3 && !this.scesoTreBello())
				|| (c.getNum() == 7 && !this.scesoSetteBello()) || (c.getNum() == 10 && !this.scesoReBello() && reBello==true)) {
			p = p - 20;
		}
		// decremento se e re bello sette bello...
		if ((3 == c.getNum() && c.getSeme().compareTo("danaro") == 0)
				|| (2 == c.getNum() && c.getSeme().compareTo("danaro") == 0)
				|| (7 == c.getNum() && c.getSeme().compareTo("danaro") == 0)
				|| (10 == c.getNum() && c.getSeme().compareTo("danaro") == 0 && reBello==true)) {
			p = p - 20;
		}
		// controllo di non lasciare scopa in tavola
		if (this.contaPuntiTavolo() + c.getNum() < 11 && dimTable != 0) {
			p = 10;
		}
		int sommPossibili[] = this.ricercaSommePossibili(c);
		if (sommPossibili[0] != 0) {
			// analizzo tutte le combinazioni di somme possibli nel mazzo
			for (int i = 1; i <= sommPossibili[0]; i++) {
				rimantenti = this.carteRimaneti(sommPossibili[i]);
				// analizzo quante carte sono già state giocate
				if (rimantenti == 0) {
					appP = 55;
				} else if (rimantenti == 1) {
					appP = 45;
				} else if (rimantenti == 2) {
					appP = 35;
				} else if (rimantenti == 3) {
					appP = 25;
				} else {
					appP = 20;
				}
				// analizzo il seme e il valore della carta
				if (c.getSeme().compareTo("danaro") == 0) {
					appP = appP - 5;
				}
				// analizzo di non lasciare somme valide per punti
				if ((sommPossibili[i] == 7 && !this.scesoSetteBello()) || (sommPossibili[i] == 10 && !this.scesoReBello() && reBello==true) || (sommPossibili[i] == 2 && !this.scesoDueBello()) || (sommPossibili[i] == 3 && !this.scesoTreBello())) {
					appP = appP - 30;
				}
				// controllo di non lasciare scopa in tavolo
				if ((this.contaPuntiTavolo() + sommPossibili[i] < 11) && (this.carteRimaneti(this.contaPuntiTavolo() + sommPossibili[i]) != 0)) {
					p = 10;
				}
				// se ottengo un valore peggiore lo aggiorno
				if (appP < p) {
					p = appP;
				}
			}
		}
		int app[][] = this.ricercaCombinazioni(c);
		// controllo che la carta non possa prendere nulla in contrario tolgo 30 punti
		if (this.ricercaCartaT(c) != -1 || app[0][0] != -1) {
			//p = p - 30;
			p = 10;
		}
		return p;
	}

	/**
	 * Il metodo ricerca la posizione nel tavolo di una carta di pari valore
	 * rispetto a quella indicata nel parametro se non è presente torna -1
	 * 
	 * @param c carta di cui effettuare la ricerca
	 * @return la posizione nel tavolo
	 */
	public int ricercaCartaT(Carta c) {
		int pos = -1;
		for (int i = 0; i < dimTable; i++) {
			if (tableCards[i].getNum() == c.getNum()) {
				pos = i;
			}
		}
		return pos;
	}

	/**
	 * Il metodo ricerca la posizione nel tavolo di una combos di carte pari al
	 * valore di quella indicata nel parametro, tutte le celle della matrice di
	 * ritorno che non sono coinvolte sono poste a -1
	 * 
	 * @param c carta di cui effettuare la ricerca
	 * @return matrice contenete le combos delle posizioni delle carte
	 */
	public int[][] ricercaCombinazioni(Carta c) {
		// il vettore contiene le posizione della somma posssibile
		int posizioni[][] = new int[5][3];
		int l = 0;
		// da finire
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				posizioni[i][j] = -1;
			}
		}
		int somma;
		// analizzo tutte le somme che posso comporre
		for (int i = 0; i < dimTable; i++) {
			somma = tableCards[i].getNum();
			for (int j = i; j < dimTable; j++) {
				// salto tutte le somme con la stessa carta
				if (i != j) {
					somma = somma + tableCards[j].getNum();
					// se la somma che ottengo è corretta salvo le posizioni
					if (somma == c.getNum()) {
						posizioni[l][0] = i;
						posizioni[l][1] = j;
						l++;
					} else if (somma < c.getNum()) {
						// se la somma è minore del valore della carta somma una terza carta
						for (int k = j; k < dimTable; k++) {
							// salto tutte le carte che ho già sommato
							if (k != i && k != j) {
								somma = somma + tableCards[k].getNum();
								if (somma == c.getNum()) {
									posizioni[l][0] = i;
									posizioni[l][1] = j;
									posizioni[l][2] = k;
									l++;
								} else {
									somma = somma - tableCards[k].getNum();
								}
							}
						}
					}
				} else {
					somma = somma - tableCards[j].getNum();
				}
			}
		}
		return posizioni;
	}

	/**
	 * Il metodo calcola e salva tutte le somme possibili che si possono ottenere
	 * tra la carta c e le carte presenti in tavola
	 * 
	 * @param c carta di cui si vuole sapere tutte le somme possibli con le carte
	 *          presenti in tavola
	 * @return il vettore conteneti tutte le somme posibili
	 */
	public int[] ricercaSommePossibili(Carta c) {
		// il vettore contiene nella prima cella la dimensione del vettore, nel resto le
		// somme possibili che posso ottenere
		int somme[] = new int[30];
		int j = 0;
		int somma = c.getNum();
		for (int i = 0; i < dimTable; i++) {
			somma = somma + tableCards[i].getNum();
			if (somma < 11) {
				j++;
				somme[j] = somma;
			}
			somma = somma - tableCards[i].getNum();
		}
		somme[0] = j;
		return somme;
	}

	/**
	 * Il metodo ricerca tra tutte le carte da lui conosciute (carte prese dalla
	 * squadra 1, carte prese dalla squadra 2, carte in tavola, carte in mano) la
	 * quantità di rimanti di valore num dato
	 * 
	 * @param num il valore della carta di cui si vuole calcolare le rimanenti
	 * @return il numero delle carte dallo stesso valore rimanenti
	 */
	public int carteRimaneti(int num) {
		int somma = 4;
		for (int i = 0; i < dimHand; i++) {
			if (num == handCards[i].getNum()) {
				somma--;
			}
		}
		for (int i = 0; i < dimTable; i++) {
			if (num == tableCards[i].getNum()) {
				somma--;
			}
		}
		ArrayList<Carta> app = teamOnePicks;
		for (int i = 0; i < app.size(); i++) {
			if (num == app.get(i).getNum()) {
				somma--;
			}
		}
		ArrayList<Carta> app1 = teamTwoPicks;
		for (int i = 0; i < app1.size(); i++) {
			if (num == app1.get(i).getNum()) {
				somma--;
			}
		}
		return somma;
	}

	/**
	 * Il metodo ricerca tra tutte le carte da lui conosciute (carte prese dalla
	 * squadra 1, carte prese dalla squadra 2, carte in tavola, carte in mano) se è
	 * già sceso il re di danari
	 * 
	 * @return true sceso, altrimenti false
	 */
	public boolean scesoReBello() {
		boolean sceso = false;
		for (int i = 0; i < dimHand; i++) {
			if (10 == handCards[i].getNum() && handCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < dimTable; i++) {
			if (10 == tableCards[i].getNum() && tableCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamOnePicks.size(); i++) {
			if (10 == teamOnePicks.get(i).getNum() && teamOnePicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamTwoPicks.size(); i++) {
			if (10 == teamTwoPicks.get(i).getNum() && teamTwoPicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		return sceso;
	}

	/**
	 * Il metodo ricerca tra tutte le carte da lui conosciute (carte prese dalla
	 * squadra 1, carte prese dalla squadra 2, carte in tavola, carte in mano) se è
	 * già sceso il sette di danari
	 * 
	 * @return true sceso, altrimenti false
	 */
	public boolean scesoSetteBello() {
		boolean sceso = false;
		for (int i = 0; i < dimHand; i++) {
			if (7 == handCards[i].getNum() && handCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < dimTable; i++) {
			if (7 == tableCards[i].getNum() && tableCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamOnePicks.size(); i++) {
			if (7 == teamOnePicks.get(i).getNum() && teamOnePicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamTwoPicks.size(); i++) {
			if (7 == teamTwoPicks.get(i).getNum() && teamTwoPicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		return sceso;
	}

	/**
	 * Il metodo ricerca tra tutte le carte da lui conosciute (carte prese dalla
	 * squadra 1, carte prese dalla squadra 2, carte in tavola, carte in mano) se è
	 * già sceso il due di danari
	 * 
	 * @return true sceso, altrimenti false
	 */
	public boolean scesoDueBello() {
		boolean sceso = false;
		for (int i = 0; i < dimHand; i++) {
			if (2 == handCards[i].getNum() && handCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < dimTable; i++) {
			if (2 == tableCards[i].getNum() && tableCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamOnePicks.size(); i++) {
			if (2 == teamOnePicks.get(i).getNum() && teamOnePicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamTwoPicks.size(); i++) {
			if (2 == teamTwoPicks.get(i).getNum() && teamTwoPicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		return sceso;
	}

	/**
	 * Il metodo ricerca tra tutte le carte da lui conosciute (carte prese dalla
	 * squadra 1, carte prese dalla squadra 2, carte in tavola, carte in mano) se è
	 * già sceso il tre di danari
	 * 
	 * @return true sceso, altrimenti false
	 */
	public boolean scesoTreBello() {
		boolean sceso = false;
		for (int i = 0; i < dimHand; i++) {
			if (3 == handCards[i].getNum() && handCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < dimTable; i++) {
			if (3 == tableCards[i].getNum() && tableCards[i].getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamOnePicks.size(); i++) {
			if (3 == teamOnePicks.get(i).getNum() && teamOnePicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		for (int i = 0; i < teamTwoPicks.size(); i++) {
			if (3 == teamTwoPicks.get(i).getNum() && teamTwoPicks.get(i).getSeme().compareTo("danaro") == 0) {
				sceso = true;
			}
		}
		return sceso;
	}

	/**
	 * Il metodo analizza unicamente l'asso e assegna un punteggio di giocabilità:
	 * 24 (se 7,10,3,2 sommo 10 e sottraggo la dimensione della mano, se danari
	 * sommo 5) 81 se 7;10;2;3 di danari
	 * 
	 * @return il punteggio di giocabilità
	 */
	public int selezionaPuntiAsso() {
		int punti = 24;
		for (int i = 0; i < dimTable; i++) {
			// se ci sono sette o re o due o tre aumento per evitare la presa del danaro da
			// parte degli avversari
			if (tableCards[i].getNum() == 7) {
				punti = punti + 10 - dimHand;
			}
			if (tableCards[i].getNum() == 10 && scesoReBello() == false) {
				punti = punti + 10 - dimHand;
			}
			if (tableCards[i].getNum() == 7 && scesoSetteBello() == false) {
				punti = punti + 10 - dimHand;
			}
			if (tableCards[i].getNum() == 3 && scesoTreBello() == false) {
				punti = punti + 10 - dimHand;
			}
			if (tableCards[i].getNum() == 2 && scesoDueBello() == false) {
				punti = punti + 10 - dimHand;
			}
			if (tableCards[i].getSeme().compareTo("danaro") == 0) {
				punti = punti + 5;
			}
			// se è prensente il re/sette/due/tre bello in tavola aumento i punti
			if (tableCards[i].getSeme().compareTo("danaro") == 0 && tableCards[i].getNum() == 10) {
				punti = 75 + 6;
			}
			if (tableCards[i].getSeme().compareTo("danaro") == 0 && tableCards[i].getNum() == 7) {
				punti = 75 + 6;
			}
			if (tableCards[i].getSeme().compareTo("danaro") == 0 && tableCards[i].getNum() == 2) {
				punti = 75 + 6;
			}
			if (tableCards[i].getSeme().compareTo("danaro") == 0 && tableCards[i].getNum() == 3) {
				punti = 75 + 6;
			}
		}

		return punti;
	}

	/**
	 * Il metodo analizza le combos presenti in tavola sucessivamente alla giocata
	 * di una carta e le analizza e ne sceglie la migliore
	 * 
	 * @param combos la matrice contenete le combos possibili in tavola
	 * @return l'indice della matrice della scelta tra le combos
	 */
	public int choiceCombos(Carta[][] combos) {
		int index = 0;
		int pMax = -1;
		int p = 0;
		int i = 0;
		int j = 0;

		while (combos[i][0] != null && i < 5) {
			p = 0;
			j = 0;
			while (combos[i][j] != null && j < 3) {
				p = p + 5;
				if (combos[i][j].getNum() == 7 && combos[i][j].getSeme().compareTo("danaro") == 0) {
					p = p + 25;
				} else if ((combos[i][j].getNum() == 2 || combos[i][j].getNum() == 3)
						&& combos[i][j].getSeme().compareTo("danaro") == 0) {
					p = p + 20;
				} else if (combos[i][j].getSeme().compareTo("danaro") == 0) {
					p = p + 5;
				} else if (combos[i][j].getNum() == 7) {
					p = p + 10;
				}
				if (combos[i][j].getNum() == 6) {
					p = p + 8;
				}
				j++;
			}
			if (p > pMax) {
				pMax = p;
				index = i;
			}
			i++;
		}

		return index;
	}
}
