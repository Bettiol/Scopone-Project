package application.model.engine.types;

import application.Main;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Settings {

	private URL table;
	private URL deck;
	private double volume;
	private boolean blasphemy;

	private int screenWidth;
	private int screenHeight;
	private boolean fullScreen;

	public void load(URL path) {
		Settings cfg = new Settings();
		File f;

		try {
			//TODO Se il file non esiste esplode
			f = new File(path.getPath());
			if (f.exists()) {
				try {
					String savedJson = new String(Files.readAllBytes(f.toPath()));
					JSONObject root = new JSONObject(savedJson);

					table = new URL(root.getString("table"));
					deck = new URL(root.getString("deck"));
					volume = root.getDouble("volume");
					blasphemy = root.getBoolean("blasphemy");
					fullScreen = root.getBoolean("fullscreen");
					screenWidth = root.getInt("screenWidth");
					screenHeight = root.getInt("screenHeight");
				} catch (JSONException e){
					defaultSettings();
					save(path);
				}
			} else if (f.createNewFile()) {
				defaultSettings();
				save(path);
			} else {
				System.err.println("Error in file creation");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void save(URL path) {
		File f;

		try {
			f = new File(path.getPath());

			if (f.exists()) {
				FileOutputStream fos = new FileOutputStream(f);

				JSONObject root = new JSONObject();
				root.put("table", table);
				root.put("deck", deck);
				root.put("volume", volume);
				root.put("blasphemy", blasphemy);
				root.put("fullscreen", fullScreen);
				root.put("screenWidth", screenWidth);
				root.put("screenHeight", screenHeight);

				fos.write(root.toString(2).getBytes(StandardCharsets.UTF_8));

				fos.close();
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
		screenWidth = 1280;
		screenHeight = 720;
	}

	public Settings copy() {
		Settings s = new Settings();

		s.table = this.table;
		s.deck = this.deck;
		s.volume = this.volume;
		s.blasphemy = this.blasphemy;
		s.fullScreen = this.fullScreen;
		s.screenWidth = this.screenWidth;
		s.screenHeight = this.screenHeight;

		return s;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Settings settings = (Settings) o;

		if (Double.compare(settings.volume, volume) != 0) return false;
		if (blasphemy != settings.blasphemy) return false;
		if (screenWidth != settings.screenWidth) return false;
		if (screenHeight != settings.screenHeight) return false;
		if (fullScreen != settings.fullScreen) return false;
		if (!table.equals(settings.table)) return false;
		return deck.equals(settings.deck);
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = table.hashCode();
		result = 31 * result + deck.hashCode();
		temp = Double.doubleToLongBits(volume);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (blasphemy ? 1 : 0);
		result = 31 * result + screenWidth;
		result = 31 * result + screenHeight;
		result = 31 * result + (fullScreen ? 1 : 0);
		return result;
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

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}
}
