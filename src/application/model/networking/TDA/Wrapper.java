package application.model.networking.TDA;

import java.io.Serializable;

/**
 * 
 * La classe rappresenta un contenitore per tutti gli oggetti.
 * 
 * @author Moscatelli
 *
 */
public class Wrapper implements Serializable {

	private static final long serialVersionUID = -5627843713201267585L;
	private String cls;
	private Object obj;

	/**
	 * Metodo construttore della classe
	 * 
	 * @param obj Oggetto
	 */
	public Wrapper(Serializable obj) {
		if (obj != null) {
			this.cls = obj.getClass().getSimpleName();
			this.obj = obj;
		} else {
			this.cls = null;
			this.obj = null;
		}
	}

	@Override
	public String toString() {
		return "Wrapper [cls=" + cls + ", obj=" + obj + "]";
	}

	public String getCls() {
		return cls;
	}

	public Object getObj() {
		return obj;
	}
}
