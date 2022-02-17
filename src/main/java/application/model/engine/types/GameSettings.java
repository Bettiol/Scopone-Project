package application.model.engine.types;

import java.io.Serial;
import java.io.Serializable;

public class GameSettings implements Serializable {

	@Serial
	private static final long serialVersionUID = -1859116387785651578L;

	private int pointLimit;

	private boolean assoPigliatutto;
	private boolean reBello;
	private boolean napoli;

	public GameSettings(int pointLimit, boolean assoPigliatutto, boolean reBello, boolean napoli) {
		super();
		this.pointLimit = pointLimit;
		this.assoPigliatutto = assoPigliatutto;
		this.reBello = reBello;
		this.napoli = napoli;
	}

	public int getPointLimit() {
		return pointLimit;
	}

	public boolean isAssoPigliatutto() {
		return assoPigliatutto;
	}

	public boolean isReBello() {
		return reBello;
	}

	public boolean isNapoli() {
		return napoli;
	}

}
