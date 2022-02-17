package application.model.networking.TDA;

import java.net.InetAddress;

/**
 * Classe che rappresenta un pacchetto in ingresso
 * 
 * @author Moscatelli
 *
 */
public class Packet {

	private InetAddress ip;
	private int port;
	private Message msg;

	/**
	 * Metodo construttore della classe
	 * 
	 * @param ip   Ip del mittente
	 * @param port Porta del mittente
	 * @param msg  Messaggio in entrata
	 */
	public Packet(InetAddress ip, int port, Message msg) {
		super();
		this.ip = ip;
		this.port = port;
		this.msg = msg;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public Message getMsg() {
		return msg;
	}
}
