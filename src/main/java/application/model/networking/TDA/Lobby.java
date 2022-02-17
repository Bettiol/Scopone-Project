package application.model.networking.TDA;

import java.io.Serial;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * 
 * @author Moscatelli
 * 
 *         La classe Lobby rappresenta un'entità fornita di nome, ip e porta.
 *         Questa classe serve essenzialmente per la trasmissione di socket tra
 *         terminali
 *
 */
public class Lobby implements Serializable {

	@Serial
	private static final long serialVersionUID = 5206357110128899112L;

	private String name;
	private InetAddress ip;
	private int port;

	private int lobbyType;

	/**
	 * Costante per indicare una lobby da 2 persone
	 */
	public static final int people2 = 1;
	/**
	 * Costante per indicare una lobby da 4 persone
	 */
	public static final int people4 = 2;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param name      Nome della lobby
	 * @param lobbyType Tipo della lobby che può essere scelto tra le costanti della
	 *                  stessa
	 * @param ip        Ip alla quale è situata la lobby
	 * @param port      Ip alla quale è situata la lobby
	 */
	public Lobby(String name, int lobbyType, InetAddress ip, int port) {
		super();
		this.name = name;
		this.lobbyType = lobbyType;
		this.ip = ip;
		this.port = port;
	}

	/**
	 * Questo metodo costruttore serve per la creazione di lobby senza sapere dove
	 * sono localizzate. E' compito di chi riceve questo oggetto ricavare la sua
	 * localizzazione
	 * 
	 * @param name      Nome della lobby
	 * @param lobbyType Tipo della lobby che può essere scelto tra le costanti della
	 *                  stessa
	 */
	public Lobby(String name, int lobbyType) {
		this(name, lobbyType, null, 0);
	}

	@Override
	public String toString() {
		return "Lobby [name=" + name + ", ip=" + ip + ", port=" + port + ", lobbyType=" + lobbyType + "]";
	}

	public String getName() {
		return name;
	}

	public int getLobbyType() {
		return lobbyType;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
