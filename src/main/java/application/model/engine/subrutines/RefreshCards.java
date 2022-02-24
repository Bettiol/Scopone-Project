package application.model.engine.subrutines;

import application.model.engine.types.cards.CardDictionary;
import application.model.engine.types.cards.Card;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * La classe gestisce l'aggiornamento delle carte in un pannello (mano / tavolo)
 *
 * @author Bettiol
 * @author Silvello
 *
 * @see CardDictionary
 *
 */
public class RefreshCards implements Runnable {

    private ArrayList<Card> arr;
    private Pane target;
    private int maxSpaces;
    private CardDictionary cd;

    /**
     * Metodo costruttore della classe
     *
     * @param target Pannello bersaglio
     * @param arr    Array di carte
     * @param cd     Dizionario delle carte
     */
    public RefreshCards(Pane target, ArrayList<Card> arr, int maxSpaces, CardDictionary cd) {
        super();
        this.target = target;
        this.arr = arr;
        this.maxSpaces = maxSpaces;
        this.cd = cd;
    }

    /**
     * Aggiorna un pannello con un dato array di carte. Se il pannello contiene pi�
     * immagini della lunghezza dell'array queste diventano trasparenti
     */
    @Override
    public void run() {
        ObservableList<Node> ol = target.getChildren();
        ImageView iv;

        int i = 0;
        int cont = 0;
        while (i < arr.size()) {
            if (ol.get(cont) instanceof ImageView) {
                iv = (ImageView) ol.get(cont);
                iv.setImage(cd.cardToImage(arr.get(i)));
                i++;
            }
            cont++;
        }

        /*TODO Bisogna sbianchettare il tavolo (arraylist ha size diversa dall'array fisso). Diversificare tra array e mani
          Al posto di arr.length c'è 8 ma bisogna sistemare
          Provare a usare capacity se si riesce
        */
        while (i < maxSpaces) {
            if (ol.get(cont) instanceof ImageView) {
                iv = (ImageView) ol.get(cont);
                iv.setImage(cd.cardToImage(null));
                i++;
            }
            cont++;
        }

    }

}
