package application.model.engine.types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Settings {

	private File table;
	private File deck;
	private double volume;
	private boolean blasphemy;

	private int screenWidth;
	private int screenHeight;
	private boolean fullScreen;

	public void load(File path) {
		Settings cfg = new Settings();

		try {
			//TODO Se il file non esiste esplode
			if (path.exists()) {
				try {
					String savedJson = new String(Files.readAllBytes(path.toPath()));
					JSONObject root = new JSONObject(savedJson);

					table = new File(root.getString("table"));
					deck = new File(root.getString("deck"));
					volume = root.getDouble("volume");
					blasphemy = root.getBoolean("blasphemy");
					fullScreen = root.getBoolean("fullscreen");
					screenWidth = root.getInt("screenWidth");
					screenHeight = root.getInt("screenHeight");
				} catch (JSONException e){
					defaultSettings();
					save(path);
				}
			} else if (path.createNewFile()) {
				defaultSettings();
				save(path);
			} else {
				System.err.println("Error in file creation");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void save(File path) {

		try {
			path = new File(path.getPath());

			if (path.exists()) {
				FileOutputStream fos = new FileOutputStream(path);

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
		table = new File("assets/tables/table.jpg");
		deck = new File("assets/cards/trevigiane");
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
