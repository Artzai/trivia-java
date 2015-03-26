import java.awt.Polygon;

/**
 * Clase que define las casillas del tablero
 *
 * @author Artzai San José
 *
 * @version 03/06/2014 v1.0
*/
public class Casilla extends Polygon {
	
	private boolean posibilidadDelante, posibilidadDetras, posibilidad, fichaNegra, fichaBlanca, quesito;
	private int posicion, tema;
	
	/**
     *	Constructor que define las propiedades de la casilla
     *	
     *  @param puntosX Los puntos X en los que se coloca la casilla
     *  @param puntosY Los puntos Y en los que se colova la casilla
     *  @param puntos Numero de puntos por los que se dibuja la casilla
     *  @param queso Indica si la casilla es quesito o no
     *  @param pos Indica la posicion(horizontal o girado a un lado) de la casilla para pintar las fichas en la posición correcta
     *  @param tem El tema al que pertenece la casilla
    */
	public Casilla(int[] puntosX, int[] puntosY, int puntos, boolean queso, int pos, int tem) {
		super(puntosX, puntosY, puntos);
		posibilidadDelante = false;
		posibilidadDetras = false;
		posibilidad = false;
		fichaNegra = false;
		fichaBlanca = false;
		quesito = queso;
		posicion = pos;
		tema = tem;
	}
	
	//GETTERS & SETTERS
	public int getTema() {
		return tema;
	}
	public boolean isPosibilidadDelante() {
		return posibilidadDelante;
	}	
	public void setPosibilidadDelante(boolean pos) {
		posibilidadDelante = pos;
	}
	public boolean isPosibilidad() {
		return posibilidad;
	}	
	public void setPosibilidad(boolean pos) {
		posibilidad = pos;
	}
	public boolean isPosibilidadDetras() {
		return posibilidadDetras;
	}	
	public void setPosibilidadDetras(boolean pos) {
		posibilidadDetras = pos;
	}
	public boolean hasFichaNegra() {
		return fichaNegra;
	}
	public void setFichaNegra(boolean fi) {
		fichaNegra = fi;
	}
	public boolean hasFichaBlanca() {
		return fichaBlanca;
	}
	public void setFichaBlanca(boolean fi) {
		fichaBlanca = fi;
	}
	public boolean isQuesito() {
		return quesito;
	}
	public int getPosicion() {
		return posicion;
	}

}
