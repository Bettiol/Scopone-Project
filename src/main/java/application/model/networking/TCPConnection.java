package application.model.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import application.model.networking.TDA.Message;

/**
 * 
 * La classe serve per gestire uno stream di dati TCP tra due host
 * 
 * @author Moscatelli
 *
 */
public class TCPConnection {

	private Socket s;

	/**
	 * Metodo costruttore della classe
	 * 
	 * @param s Socket alla quale sono connesso
	 */
	public TCPConnection(Socket s) {
		this.s = s;
	}

	/**
	 * La classe rimane in ascolto dello stream fino all'arrivo un oggetto di tipo
	 * Messaggio
	 * 
	 * @return Messaggio in entrata
	 * @throws IOException            L'host si è scollegato
	 * @throws ClassNotFoundException
	 */
	public Message receive() throws IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		Message object = (Message) ois.readObject();

		return object;
	}

	/**
	 * Il metodo scrive un oggetto sullo stream
	 * 
	 * @param obj Messaggio da scrivere sullo stream
	 * @throws IOException L'host si è scollegato
	 */
	public void send(Message obj) throws IOException {

		OutputStream os = s.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(obj);

	}

	/**
	 * Chiusura dello stream
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		s.close();
	}

	/**
	 * Ritorna se il socket è chiuso o no
	 * 
	 * @return true se il socket è chiuso
	 */
	public boolean isClosed() {
		return s.isClosed();
	}
}
