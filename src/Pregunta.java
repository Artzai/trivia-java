/**
 * Clase que define las preguntas del juego
 * 
 * @author Artzai San José
 *
 *@version 03/06/2014 v1.0
 */
public class Pregunta {
	
	private String pregunta, respuesta, arrayIncorrectas[];
	private boolean realizada;
	
	/**
	 * Constructor de la clase Pregunta
	 * 
	 * @param preg Enunciado de la pregunta
	 * @param resp Respuesta correcta a la pregunta
	 * @param arrayIncor array de seis respuestas incorrectas
	 */
	public Pregunta(String preg, String resp, String arrayIncor[]) {
		pregunta = preg;
		respuesta = resp;
		arrayIncorrectas = arrayIncor;		
	}

	//GETTERS & SETTERS
	public boolean isRealizada() {
		return realizada;
	}
	public void setRealizada(boolean realizada) {
		this.realizada = realizada;
	}
	public String getPregunta() {
		return pregunta;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public String getRespuestaRandom() {		
		return arrayIncorrectas[(int)(Math.random()*arrayIncorrectas.length+0)];
	}
	
	/**
	 * Método que devuelve tres respuestas incorrectas aleatorias de las 6 posibles llamando al método getRespuestasRandom
	 * 
	 * @return array de tres respuestas incorrectas
	 */
	public String[] getRespuestas() {
		String arrayRespuestas[] = new String[4];
		boolean repetida = false;
		arrayRespuestas[(int)(Math.random()*arrayRespuestas.length+0)] = respuesta;
		for (int i = 0; i < arrayRespuestas.length; i++) {
			if (arrayRespuestas[i] == null) {
				String resp = this.getRespuestaRandom();
				for (int j = 0; j < arrayRespuestas.length; j++) {
					if (arrayRespuestas[j] != null) {
						if (arrayRespuestas[j].equals(resp)) {
							repetida = true;
						}
					}
				}
				if (!repetida) {
					arrayRespuestas[i] = resp;
				}
				else {
					i--;
				}
			}
			repetida = false;
		}		
		return arrayRespuestas;
	}

}