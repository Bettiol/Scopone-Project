package application.model.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import application.model.networking.TDA.Message;
import application.model.networking.TDA.Packet;

/**
 * 
 * Classe che gestisce un socket UDP
 * 
 * @author Moscatelli
 *
 * @see DatagramSocket
 */
public class UDPSocket {

	private DatagramSocket dg;

	/**
	 * Metodo costruttore che crea un socket UDP in una porta libera casuale
	 * 
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public UDPSocket() throws SocketException, UnknownHostException {
		this.dg = new DatagramSocket();
		// this.dg = new DatagramSocket(10000,
		// InetAddress.getByName("192.168.178.201"));
		dg.setBroadcast(true);
	}

	/**
	 * Metodo costruttore che crea un socket UDP in una porta specificata
	 * 
	 * @param port Porta a cui verr� collegato il socket
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public UDPSocket(int port) throws SocketException, UnknownHostException {
		this.dg = new DatagramSocket(port);
		// this.dg = new DatagramSocket(port, InetAddress.getByName("25.148.235.191"));
		dg.setBroadcast(true);
	}

	/**
	 * Il metodo rimane in ascolto del canale fino a che non riceve un oggetto o
	 * venga lanciata un'eccezione SocketTimeuot
	 * 
	 * @return Pacchetto contenente l'oggetto ricevuto e l'ip e la porta del
	 *         mittente
	 * @throws SocketTimeoutException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Packet receive() throws SocketTimeoutException, IOException, ClassNotFoundException {
		byte buff[] = new byte[1024];
		DatagramPacket packetIn = new DatagramPacket(buff, buff.length);

		ByteArrayInputStream baos;
		ObjectInputStream ois;
		Message read = null;

		// Ascolto
		dg.receive(packetIn);

		// Deserializza
		baos = new ByteArrayInputStream(buff);
		ois = new ObjectInputStream(baos);
		read = (Message) ois.readObject();

		return new Packet(packetIn.getAddress(), packetIn.getPort(), read);

	}

	/**
	 * Questo metodo spedisce un oggetto ad un socket specificato. L'arrivo del
	 * messaggio non � assicurato
	 * 
	 * @param sendToIp   Ip del destinatario
	 * @param sendToPort Porta del destinatario
	 * @param obj        Oggetto spedito
	 * @throws IOException
	 */
	public void sendTo(InetAddress sendToIp, int sendToPort, Message obj) throws IOException {
		byte[] buff = new byte[1024];

		// Crea wrapper e lo converte in bytes
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		buff = baos.toByteArray();

		// Spedisci e spera
		DatagramPacket packetOut = new DatagramPacket(buff, buff.length, sendToIp, sendToPort);
		dg.send(packetOut);
	}

	/**
	 * Questo metodo manda in broadcast a tutte le reti a una specificata port aun
	 * oggetto
	 * 
	 * @param sendToPort Porta destinataria
	 * @param obj        Oggetto spedito
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void broadcast(int sendToPort, Message obj) throws UnknownHostException, IOException {
		Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
		NetworkInterface i;

		InetAddress ip;
		InetAddress loop = InetAddress.getLoopbackAddress();
		InetAddress vm = InetAddress.getByName("192.168.56.1");

		while (list.hasMoreElements()) {
			i = list.nextElement();

			for (InterfaceAddress addr : i.getInterfaceAddresses()) {
				ip = addr.getAddress();
				if (ip instanceof Inet4Address) {
					if (!ip.equals(loop) && !ip.equals(vm)) {
						sendTo(addr.getBroadcast(), sendToPort, obj);
					}
				}
			}

		}
	}

	/**
	 * Modifica il SO_TIMEOUT dell'oggetto DatagramSocket
	 * 
	 * @param millis
	 * @throws SocketException
	 */
	public void setSoTimeout(int millis) throws SocketException {
		dg.setSoTimeout(millis);
	}

	/**
	 * Chiude il DatagramSocket. Ogni socket bloccato in recezione generer� una
	 * SocketException
	 */
	public void close() {
		dg.close();
	}

	/**
	 * Ritorna se il DatagramSocket � chiuso
	 * 
	 * @return true se il socket � chiuso
	 */
	public boolean isClosed() {
		return dg.isClosed();
	}
}
