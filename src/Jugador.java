/**
 * Clase que define a los jugadores del juego
 * 
 * @author Artzai San José
 *
 * @version 03/06/2014 v1.0
 */
public class Jugador {
	
	private String nombre;
	private boolean[] quesitos;
	
	/**
	 * Método para crear jugadores
	 * 
	 * @param nom Nombre del jugador
	 */
	public Jugador(String nom) {
		nombre = nom;
		quesitos = new boolean[6];
		for (int i = 0; i < quesitos.length; i++) {
			quesitos[i] = false;
		}
	}
	//GETTERS & SETTERS
	public String getNombre() {
		return nombre;
	}	
	public void setNombre(String nom) {
		nombre = nom;
	}
	public boolean[] getQuesitos() {
		return quesitos;
	}	
	public void setQuesito(boolean quesito, int tema) {
		quesitos[tema] = quesito;
	}	
	
	/**
	 * Método para comprobar si el jugador tiene almenos un quesito
	 * 
	 * @return boolean Devuelve true si tiene almenos 1 quesito
	 */
	public boolean hasQuesito() {
		boolean tiene = false;
		for (int i = 0; i < quesitos.length; i++) {
			if (quesitos[i]) {
				tiene = true;
				break;
			}
		}
		return tiene;
	}	
	
	/**
	 * Método para comprobar que al jugador le falta almenos un quesito
	 * 
	 * @return boolean Devuelve true si le falta almenos un quesito
	 */
	public boolean faltaQuesito() {
		boolean falta = false;
		for (int i = 0; i < quesitos.length; i++) {
			if (!quesitos[i]) {
				falta = true;
				break;
			}
		}
		return falta;
	}

}
