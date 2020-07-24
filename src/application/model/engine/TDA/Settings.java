package application.model.engine.TDA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Settings implements Serializable {

	private static final long serialVersionUID = -6653167206316018113L;
	private final String path = "resources/settings.cfg";
	private File table;
	private File deck;
	private double volume;
	private boolean blasphemy;

	public void loadSettings() {
		Settings cfg = null;
		File f;

		try {
			f = new File(path);

			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);

				cfg = (Settings) ois.readObject();
				table = cfg.getTable();
				deck = cfg.getDeck();
				volume = cfg.getVolume();
				blasphemy = cfg.isBlasphemy();

				ois.close();
			} else if (f.createNewFile()) {
				defaultSettings();
				saveSettings();
			} else {
				System.err.println("Error in file creation");
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void saveSettings() {
		File f;

		try {
			f = new File(path);

			if (f.exists()) {
				FileOutputStream fos = new FileOutputStream(f);
				ObjectOutputStream oos = new ObjectOutputStream(fos);

				oos.writeObject(this);

				oos.close();
			} else {
				System.err.println("Error");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void defaultSettings() {
		table = new File("resources/tables/table.jpg");
		deck = new File("resources/cards/trevigiane");
		volume = 1;
		blasphemy = false;
	}

	public Settings copy() {
		Settings s = new Settings();

		s.table = this.table;
		s.deck = this.deck;
		s.volume = this.volume;
		s.blasphemy = this.blasphemy;

		return s;
	}

	public File getTable() {
		return table;
	}

	public void setTable(File table) {
		this.table = table;
	}

	public File getDeck() {
		return deck;
	}

	public void setDeck(File deck) {
		this.deck = deck;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public boolean isBlasphemy() {
		return blasphemy;
	}

	public void setBlasphemy(boolean blasphemy) {
		this.blasphemy = blasphemy;
	}

}
