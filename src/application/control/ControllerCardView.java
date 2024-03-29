package application.control;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import application.model.engine.TDA.CardDictionary;
import application.model.engine.TDA.Carta;
import application.model.engine.subrutines.RefreshCards;
import application.model.engine.subrutines.RefreshCombos;
import application.model.engine.subrutines.RefreshPlayed;
import application.model.engine.subrutines.RefreshScope;
import application.model.engine.subrutines.RefreshTurn;
import application.model.engine.subrutines.RemoveCard;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ControllerCardView implements Initializable {

	@FXML
	private BorderPane background;
	@FXML
	private StackPane sp;
	@FXML
	private GridPane table;
	@FXML
	private ImageView played;
	@FXML
	private VBox scelta;

	@FXML
	private HBox g1;
	@FXML
	private VBox g2;
	@FXML
	private HBox g3;
	@FXML
	private VBox g4;

	@FXML
	private Label l_g1;
	@FXML
	private Label l_g2;
	@FXML
	private Label l_g3;
	@FXML
	private Label l_g4;

	@FXML
	private Label s_g1;
	@FXML
	private Label s_g2;
	@FXML
	private Label s_g3;
	@FXML
	private Label s_g4;

	private Semaphore input = new Semaphore(0);
	private boolean clickEvent = false;
	private boolean choiceEvent = false;
	private Integer giocata;

	private CardDictionary cd = new CardDictionary();

	public Semaphore getInput() {
		return input;
	}

	public Integer getGiocata() {
		return giocata;
	}

	public void setClickEvent(boolean clickEvent) {
		this.clickEvent = clickEvent;
	}

	public void setChoiceEvent(boolean choiceEvent) {
		this.choiceEvent = choiceEvent;
	}

	private void inizializeCards(Pane pane) {
		ObservableList<Node> ol = pane.getChildren();
		ImageView iv;

		for (int i = 0; i < ol.size(); i++) {
			if (ol.get(i) instanceof ImageView) {
				iv = (ImageView) ol.get(i);

				if (pane instanceof HBox) {
					iv.setImage(cd.getRetro());
					iv.fitHeightProperty().bind(pane.heightProperty());
				} else if (pane instanceof VBox) {
					iv.setImage(cd.getRetro90());
					iv.fitWidthProperty().bind(pane.widthProperty());
				}
			}
		}

		if (pane instanceof HBox) {
			pane.widthProperty().addListener((observable, oldValue, newValue) -> {

				ObservableList<Node> lal = pane.getChildren();
				ImageView vi;

				for (int i = 0; i < lal.size(); i++) {
					if (lal.get(i) instanceof ImageView) {
						vi = (ImageView) lal.get(i);
						vi.setFitWidth(newValue.doubleValue() / lal.size());
					}
				}
			});
		} else if (pane instanceof VBox) {
			pane.heightProperty().addListener((observable, oldValue, newValue) -> {

				ObservableList<Node> lal = pane.getChildren();
				ImageView vi;

				for (int i = 0; i < lal.size(); i++) {
					if (lal.get(i) instanceof ImageView) {
						vi = (ImageView) lal.get(i);
						vi.setFitHeight(newValue.doubleValue() / 5);
					}
				}
			});
		}
	}

	private void inizializeTable() {
		ObservableList<Node> ol = table.getChildren();
		ImageView iv = null;

		for (int i = 0; i < ol.size(); i++) {
			iv = (ImageView) ol.get(i);
			iv.fitWidthProperty().bind(table.widthProperty().divide(4));
			iv.fitHeightProperty().bind(table.heightProperty().divide(3));
		}

		played.fitWidthProperty().bind(iv.fitWidthProperty());
		played.fitHeightProperty().bind(iv.fitHeightProperty());
	}

	private void inizializeCombos() {
		ObservableList<Node> scelte = scelta.getChildren();

		scelta.widthProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<Node> ol = scelta.getChildren();
			HBox pane;

			for (int z = 0; z < ol.size(); z++) {
				pane = (HBox) ol.get(z);
				pane.setPrefWidth(newValue.doubleValue());
			}
		});
		scelta.heightProperty().addListener((observable, oldValue, newValue) -> {
			ObservableList<Node> ol = scelta.getChildren();
			HBox pane;

			for (int z = 0; z < ol.size(); z++) {
				pane = (HBox) ol.get(z);
				pane.setPrefHeight(newValue.doubleValue() / 3);
			}

		});

		for (int z = 0; z < scelte.size(); z++) {
			HBox pane;
			ImageView iv;
			pane = (HBox) scelte.get(z);

			ObservableList<Node> ol = pane.getChildren();
			for (int i = 0; i < ol.size(); i++) {
				if (ol.get(i) instanceof ImageView) {
					iv = (ImageView) ol.get(i);
					iv.setImage(cd.getTransparent());
					iv.fitHeightProperty().bind(pane.heightProperty());
				}
			}

			pane.widthProperty().addListener((observable, oldValue, newValue) -> {

				ObservableList<Node> lal = pane.getChildren();
				ImageView vi;

				for (int i = 0; i < lal.size(); i++) {
					if (lal.get(i) instanceof ImageView) {
						vi = (ImageView) lal.get(i);
						vi.setFitWidth(newValue.doubleValue() / 4);
					}
				}
			});
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		background.widthProperty().addListener((observable, oldValue, newValue) -> {
			double val = newValue.doubleValue();

			background.setPrefWidth(val);

			sp.setPrefWidth(val / 2);
			g1.setPrefWidth(val);
			g2.setPrefWidth(val / 4);
			g3.setPrefWidth(val);
			g4.setPrefWidth(val / 4);
		});
		background.heightProperty().addListener((observable, oldValue, newValue) -> {
			double val = newValue.doubleValue();

			background.setPrefHeight(val);

			sp.setPrefHeight(val / 5 * 3);
			g1.setPrefHeight(val / 5);
			g2.setPrefHeight((val - 2 / 5) / 2);
			g3.setPrefHeight(val / 5);
			g4.setPrefHeight((val - 2 / 5) / 2);
		});

		sp.widthProperty().addListener((observable, oldValue, newValue) -> {
			table.setPrefWidth(newValue.doubleValue() - 50);
			scelta.setPrefWidth(newValue.doubleValue());
		});
		sp.heightProperty().addListener((observable, oldValue, newValue) -> {
			table.setPrefHeight(newValue.doubleValue());
			scelta.setPrefHeight(newValue.doubleValue());
		});

		inizializeCards(g1);
		inizializeCards(g2);
		inizializeCards(g3);
		inizializeCards(g4);
		inizializeTable();
		inizializeCombos();

		played.setImage(cd.getTransparent());
	}

	public void refreshHand(Carta arr[], int dim) {
		Platform.runLater(new RefreshCards(g1, arr, dim, cd));
	}

	public void refreshTable(Carta arr[], int dim) {
		Platform.runLater(new RefreshCards(table, arr, dim, cd));
	}

	public void refreshCombos(Carta[][] combos) {
		Platform.runLater(new RefreshCombos(table, scelta, combos, cd));
	}

	public void refreshPlayed(Carta c) {
		Platform.runLater(new RefreshPlayed(played, c, cd));
	}

	public void removeCard(int player) {
		Pane g = null;

		switch (player) {
		case 0:
			g = g1;
			break;
		case 1:
			g = g2;
			break;
		case 2:
			g = g3;
			break;
		case 3:
			g = g4;
			break;
		}

		Platform.runLater(new RemoveCard(g, cd.getTransparent()));
	}

	public void refreshTurn(int turn) {
		Platform.runLater(new RefreshTurn(l_g1, l_g2, l_g3, l_g4, turn));
	}

	public void refreshScope(int scope, int squad) {
		if (squad == 1) {
			Platform.runLater(new RefreshScope(s_g1, s_g3, scope, squad));
		} else if (squad == 2) {
			Platform.runLater(new RefreshScope(s_g2, s_g4, scope, squad));
		}
	}

	@FXML
	public void getCarta(Event e) {
		if (clickEvent) {
			ImageView iv = (ImageView) e.getSource();

			String key = iv.getId();
			key = key.split("_")[1];

			giocata = Integer.valueOf(key);
			giocata = Integer.valueOf(giocata.intValue() - 1);

			clickEvent = false;
			input.release();
		}
	}

	@FXML
	public void getChoiche(Event e) {
		if (choiceEvent) {
			HBox hb = (HBox) e.getSource();

			// System.out.println(hb.getId());
			String key = hb.getId();
			key = key.split("_")[1];
			// System.out.println(key);

			giocata = Integer.valueOf(key);
			giocata = Integer.valueOf(giocata.intValue() - 1);

			// Reset
			table.setVisible(true);
			scelta.setVisible(false);

			choiceEvent = false;
			input.release();
		}
	}

}
