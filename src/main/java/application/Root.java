package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import application.model.engine.types.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Root extends Application {

	private static Stage primaryStages;

	public static MediaPlayer mainTheme;

	public static final File settingsPath = new File("config/config.json");
	public static Settings settings;

	@Override
	public void start(Stage primaryStage) {
		// Carica le impostazioni
		settings = new Settings();
		settings.load(settingsPath);

		// Inizializza view
		try {
			primaryStages = primaryStage;

			primaryStages.setTitle("AC-130 Death Angel");
			primaryStages.getIcons().add(new Image(new FileInputStream("assets/ico.png")));

			primaryStages.setMinWidth(1280);
			primaryStages.setMinHeight(720);

			primaryStages.setFullScreen(Root.settings.isFullScreen());

			Root.changeStage("MainMenu.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void launchApp(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return primaryStages;
	}

	public static Object changeStage(String fxml) throws IOException {
		// setUserAgentStylesheet();

		FXMLLoader loader = new FXMLLoader(new URL("file:assets/view/" + fxml));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		primaryStages.setScene(scene);
		primaryStages.setWidth(primaryStages.getWidth());
		primaryStages.setHeight(primaryStages.getHeight());
		primaryStages.setFullScreen(Root.settings.isFullScreen());

		primaryStages.show();

		return loader.getController();
	}

	public static void playMainTheme(String sound) {
		if (mainTheme == null) {
			File f = new File("assets/sounds/" + sound);
			Media media = new Media(f.toURI().toString());
			mainTheme = new MediaPlayer(media);
			mainTheme.volumeProperty().set(settings.getVolume());
			mainTheme.play();
		} else if (!mainTheme.getStatus().equals(MediaPlayer.Status.PLAYING)) {
			//TODO Filepath
			File f = new File("assets/sounds/" + sound);
			Media media = new Media(f.toURI().toString());
			mainTheme = new MediaPlayer(media);
			mainTheme.volumeProperty().set(settings.getVolume());
			mainTheme.play();
		}
	}

	public static void stopMainTheme() {
		if (mainTheme.getStatus() == MediaPlayer.Status.PLAYING) {
			mainTheme.stop();
		}
	}

	public static void playSound(String sound) {
		File f = new File("assets/sounds/" + sound);

		Media media = new Media(f.toURI().toString());
		MediaPlayer mp = new MediaPlayer(media);

		mp.volumeProperty().set(settings.getVolume());
		mp.play();
	}

}
