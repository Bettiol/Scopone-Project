package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage primaryStages;

	private static MediaPlayer mainTheme;

	@Override
	public void start(Stage primaryStage) {
		// Inizializza view
		try {
			primaryStages = primaryStage;

			primaryStages.setTitle("AC-130 Death Angel");
			primaryStages.getIcons().add(new Image("file:resources/ico.png"));

			primaryStages.setMinWidth(1280);
			primaryStages.setMinHeight(720);

			Main.changeStage("MainMenu.fxml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

		System.exit(0);
	}

	public static Stage getPrimaryStage() {
		return primaryStages;
	}

	public static Object changeStage(String fxml) throws IOException {
		// setUserAgentStylesheet();

		// Quando la lobby � piena fai partire il tavolo
		// MainStage = Buono.getPrimaryStage();
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/" + fxml));
		Parent root = loader.load();
		Scene scene = new Scene(root);

		primaryStages.setScene(scene);
		primaryStages.show();

		primaryStages.setWidth(primaryStages.getWidth());
		primaryStages.setHeight(primaryStages.getHeight());

		primaryStages.centerOnScreen();

		return loader.getController();
	}

	public static void playMainTheme(String sound) {
		if (mainTheme == null) {
			File f = new File("sounds/" + sound);
			Media media = new Media(f.toURI().toString());
			mainTheme = new MediaPlayer(media);
			mainTheme.volumeProperty().set(0.1);
			mainTheme.play();
		} else if (!mainTheme.getStatus().equals(Status.PLAYING)) {
			File f = new File("sounds/" + sound);
			Media media = new Media(f.toURI().toString());
			mainTheme = new MediaPlayer(media);
			mainTheme.volumeProperty().set(0.1);
			mainTheme.play();
		}
	}

	public static void stopMainTheme() {
		if (mainTheme.getStatus().equals(Status.PLAYING)) {
			mainTheme.stop();
		}
	}

	public static void playSound(String sound) {
		File f = new File("sounds/" + sound);

		Media media = new Media(f.toURI().toString());
		MediaPlayer mp = new MediaPlayer(media);

		mp.volumeProperty().set(0.1);
		mp.play();
	}

}
