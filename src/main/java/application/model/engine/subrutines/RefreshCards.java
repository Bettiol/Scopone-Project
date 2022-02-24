package application.model.engine.subrutines;

import application.model.engine.types.cards.CardDictionary;
import application.model.engine.types.cards.Carta;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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

    private Pane target;
    private Carta[] arr;
    private int dim;

    private CardDictionary cd;

    /**
     * Metodo costruttore della classe
     *
     * @param target Pannello bersaglio
     * @param arr    Array di carte
     * @param dim    Dimensione dell'array
     * @param cd     Dizionario delle carte
     */
    public RefreshCards(Pane target, Carta[] arr, int dim, CardDictionary cd) {
        super();
        this.target = target;
        this.arr = arr;
        this.dim = dim;
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
        while (i < dim) {
            if (ol.get(cont) instanceof ImageView) {
                iv = (ImageView) ol.get(cont);
                iv.setImage(cd.cardToImage(arr[i]));
                i++;
            }
            cont++;
        }

        /*TODO Bisogna sbianchettare il tavolo (arraylist ha size diversa dall'array fisso)
          Al posto di arr.length c'è 8 ma bisogna sistemare*/
        while (i < 8) {
            if (ol.get(cont) instanceof ImageView) {
                iv = (ImageView) ol.get(cont);
                iv.setImage(cd.cardToImage(null));
                i++;
            }
            cont++;
        }

    }

}
