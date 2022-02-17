package application.model.engine.TDA;

import application.Main;

import java.io.*;
import java.net.URL;

public class Settings implements Serializable {

	@Serial
	private static final long serialVersionUID = -6653167206316018113L;

	private final URL path = Main.class.getResource("settings.cfg");
	private URL table;
	private URL deck;
	private double volume;
	private boolean blasphemy;
	private boolean fullScreen;

	public void loadSettings() {
		Settings cfg = null;
		File f;

		try {
			f = new File(path.getPath());

			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);

				cfg = (Settings) ois.readObject();
				table = cfg.getTable();
				deck = cfg.getDeck();
				volume = cfg.getVolume();
				blasphemy = cfg.isBlasphemy();
				fullScreen = cfg.isFullScreen();

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
			f = new File(path.getPath());

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
		table = Main.class.getResource("assets/tables/table.jpg");
		deck = Main.class.getResource("assets/cards/trevigiane");
		volume = 1;
		blasphemy = false;
		fullScreen = false;
	}

	public Settings copy() {
		Settings s = new Settings();

		s.table = this.table;
		s.deck = this.deck;
		s.volume = this.volume;
		s.blasphemy = this.blasphemy;
		s.fullScreen = this.fullScreen;

		return s;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Settings other = (Settings) obj;
		if (blasphemy != other.blasphemy) {
			return false;
		}
		if (deck == null) {
			if (other.deck != null) {
				return false;
			}
		} else if (!deck.equals(other.deck)) {
			return false;
		}
		if (fullScreen != other.fullScreen) {
			return false;
		}
		if (table == null) {
			if (other.table != null) {
				return false;
			}
		} else if (!table.equals(other.table)) {
			return false;
		}
		if (Double.doubleToLongBits(volume) != Double.doubleToLongBits(other.volume)) {
			return false;
		}
		return true;
	}

	public URL getTable() {
		return table;
	}

	public void setTable(URL table) {
		this.table = table;
	}

	public URL getDeck() {
		return deck;
	}

	public void setDeck(URL deck) {
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

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}
}
