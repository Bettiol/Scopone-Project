package application.control;

import java.io.IOException;

import application.Main;
import application.model.engine.TDA.Points;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Questa classe controller gestisce il view del ricapitolo dei punti
 * 
 * @author Bettiol
 * @author Silvello
 *
 */
public class MatchEndController {

	@FXML
	private VBox s_1;
	@FXML
	private VBox s_2;
	@FXML
	private Text winner;
	@FXML
	private Stage MainStage;

	/**
	 * Aggiorna le label del view in base ai punti ottenuti dalle due squadre
	 * 
	 * @param squadra Punti di entrambe le squadre
	 */
	public void endMatch(Points[] squadra) {
		ObservableList<Node> ol1 = s_1.getChildren();
		ObservableList<Node> ol2 = s_2.getChildren();
		Label lal;

		lal = (Label) ol1.get(0);
		setLable(lal, squadra[0].isCarte());
		lal = (Label) ol2.get(0);
		setLable(lal, squadra[1].isCarte());

		lal = (Label) ol1.get(1);
		setLable(lal, squadra[0].isDanari());
		lal = (Label) ol2.get(1);
		setLable(lal, squadra[1].isDanari());

		lal = (Label) ol1.get(2);
		setLable(lal, squadra[0].isPrimiera());
		lal = (Label) ol2.get(2);
		setLable(lal, squadra[1].isPrimiera());

		lal = (Label) ol1.get(2);
		setLable(lal, squadra[0].isPrimiera());
		lal = (Label) ol2.get(2);
		setLable(lal, squadra[1].isPrimiera());

		lal = (Label) ol1.get(3);
		setLable(lal, squadra[0].isSettebello());
		lal = (Label) ol2.get(3);
		setLable(lal, squadra[1].isSettebello());

		lal = (Label) ol1.get(4);
		setLable(lal, squadra[0].isRebello());
		lal = (Label) ol2.get(4);
		setLable(lal, squadra[1].isRebello());

		lal = (Label) ol1.get(5);
		setLable(lal, "+" + squadra[0].getNapoli());
		lal = (Label) ol2.get(5);
		setLable(lal, "+" + squadra[1].getNapoli());

		lal = (Label) ol1.get(6);
		setLable(lal, "+" + squadra[0].getScope());
		lal = (Label) ol2.get(6);
		setLable(lal, "+" + squadra[1].getScope());

		int tot_s1 = squadra[0].calcolaTotale();
		int tot_s2 = squadra[1].calcolaTotale();

		lal = (Label) ol1.get(7);
		setLable(lal, "" + tot_s1);
		lal = (Label) ol2.get(7);
		setLable(lal, "" + tot_s2);

		if (tot_s1 > tot_s2) {
			winner.setText("La squadra uno vince!");
		} else if (tot_s1 < tot_s2) {
			winner.setText("La squadra due vince!");
			Main.playSound("MissionFailed.mp3");
		} else {
			winner.setText("Le squadre pareggiano");
		}
	}

	/**
	 * Questo metodo cambia il testo di una label in "+1" se x è vero o "0" se x è
	 * falso
	 * 
	 * @param l Label
	 * @param x Boolean
	 */
	private void setLable(Label l, boolean x) {
		if (x) {
			l.setText("+1");
		} else {
			l.setText("0");
		}
	}

	/**
	 * Questo metodo cambia il testo di una label
	 * 
	 * @param l Label
	 * @param x Testo
	 */
	private void setLable(Label l, String x) {
		l.setText(x);
	}

	/**
	 * Torna al menù principale
	 * 
	 * @throws IOException
	 */
	@FXML
	public void returnButton() throws IOException {
		Main.changeStage("MainMenu.fxml");
	}

}
