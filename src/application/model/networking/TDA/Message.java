package application.model.networking.TDA;

import java.io.Serializable;

/**
 * La classe messaggio è la base per la comunicazione tra terminali. La classe è
 * dotata di due attributi, un numero identificativo per la tipologia di
 * messaggio e l'oggetto trasmesso
 * 
 * 
 * @author Moscatelli
 *
 */
public class Message implements Serializable {

	private static final long serialVersionUID = -2723363051271966964L;
	private int type;
	private Wrapper msg;

	/**
	 * Costante per messaggi
	 */
	public final static int START_MATCH = 0;
	/**
	 * Costante per messaggi
	 */
	public final static int END_MATCH = 1;

	/**
	 * Costante per messaggi
	 */
	public final static int HELLO_THERE = 10;
	/**
	 * Costante per messaggi
	 */
	public final static int SET_TURN = 11;
	/**
	 * Costante per messaggi
	 */
	public final static int CARD_PLAYED = 12;
	/**
	 * Costante per messaggi
	 */
	public final static int STATE = 13;
	/**
	 * Costante per messaggi
	 */
	public final static int CHOICE = 14;

	/**
	 * Costante per messaggi
	 */
	public final static int IS_SOMEONE_THERE = 100;
	/**
	 * Costante per messaggi
	 */
	public final static int IM_THERE = 101;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param type Tipo di messaggio
	 * @param msg  Oggetto
	 */
	public Message(int type, Serializable msg) {
		super();
		this.type = type;
		this.msg = new Wrapper(msg);
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", msg=" + msg + "]";
	}

	public int getType() {
		return type;
	}

	public Wrapper getMsg() {
		return msg;
	}
}
