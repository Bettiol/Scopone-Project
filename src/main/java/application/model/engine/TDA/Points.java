package application.model.engine.TDA;

import java.io.Serializable;

/**
 * La classe rappresenta i punti possibili effettuati a fine gioco
 * 
 * @author Brognera
 * @author Moscatelli
 *
 */

public class Points implements Serializable {

	private static final long serialVersionUID = 6776383109489534815L;
	private int scope;
	private int napoli;
	private boolean settebello;
	private boolean rebello;
	private boolean primiera;
	private boolean carte;
	private boolean danari;

	/**
	 * Metodo costruttore della classe
	 */
	public Points() {
		super();
		scope = 0;
		napoli = 0;
		settebello = false;
		rebello = false;
		primiera = false;
		carte = false;
		danari = false;

	}

	/**
	 * Il metodo ritorna la quantità di scope
	 * 
	 * @return l'intero rappresentante la quantità di scope
	 */
	public int getScope() {
		return scope;
	}

	/**
	 * Il metodo setta la quantità di scope
	 * 
	 * @param scope Quantità di scope
	 */
	public void setScope(int scope) {
		this.scope = scope;
	}

	/**
	 * Il metodo ritorna il valore della napoli
	 * 
	 * @return l'intero rappresentante il valore della napoli
	 */
	public int getNapoli() {
		return napoli;
	}

	/**
	 * Il metodo setta il valore della napoli
	 * 
	 * @param napoli valore della napoli
	 */
	public void setNapoli(int napoli) {
		this.napoli = napoli;
	}

	/**
	 * Il metodo ritorna lo stato del sette di danari
	 * 
	 * @return stato del sette di danari
	 */
	public boolean isSettebello() {
		return settebello;
	}

	/**
	 * Il metodo setta lo stato del sette di danari
	 * 
	 * @param settebello stato del sette di danari
	 */
	public void setSettebello(boolean settebello) {
		this.settebello = settebello;
	}

	/**
	 * Il metodo ritorna lo stato del re di danari
	 * 
	 * @return stato del re di danari
	 */
	public boolean isRebello() {
		return rebello;
	}

	/**
	 * Il metodo setta lo stato del re di danari
	 * 
	 * @param rebello stato del re di danari
	 */
	public void setRebello(boolean rebello) {
		this.rebello = rebello;
	}

	/**
	 * Il metodo ritorna lo stato della premiera
	 * 
	 * @return stato della premiera
	 */
	public boolean isPrimiera() {
		return primiera;
	}

	/**
	 * Il metodo setta lo stato della premiera
	 * 
	 * @param primiera stato della premiera
	 */
	public void setPrimiera(boolean primiera) {
		this.primiera = primiera;
	}

	/**
	 * Il metodo ritorna lo stato della maggioranza di carte
	 * 
	 * @return stato della maggioranza di carte
	 */
	public boolean isCarte() {
		return carte;
	}

	/**
	 * Il metodo setta lo stato della maggioranza di carte
	 * 
	 * @param carte stato della maggioranza di carte
	 */
	public void setCarte(boolean carte) {
		this.carte = carte;
	}

	/**
	 * Il metodo ritorna lo stato della maggioranza di danari
	 * 
	 * @return stato della maggioranza di danari
	 */
	public boolean isDanari() {
		return danari;
	}

	/**
	 * Il metodo setta lo stato della maggioranza di danari
	 * 
	 * @param danari stato della maggioranza di danari
	 */
	public void setDanari(boolean danari) {
		this.danari = danari;
	}

	/**
	 * Il metodo calcola il totale dei punti
	 * 
	 * @return il punetggio totale
	 */
	public int calcolaTotale() {
		int tot = 0;

		if (carte) {
			tot++;
		}
		if (danari) {
			tot++;
		}
		if (primiera) {
			tot++;
		}
		if (settebello) {
			tot++;
		}
		if (rebello) {
			tot++;
		}
		tot = tot + scope + napoli;

		return tot;
	}

}
