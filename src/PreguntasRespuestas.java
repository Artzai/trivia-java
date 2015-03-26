import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase contenedora de las preguntas y respuestas
 * 
 * @author Artzai San José
 * 
 * @version 03/06/2014 v1.0
 */
public class PreguntasRespuestas {
	
	private ArrayList<ArrayList<Pregunta>> arrayPreguntas;
	private String[] arrayResp;
	
	/**
	 * Constructor del contenedor de preguntas
	 */
	public PreguntasRespuestas() {
		arrayPreguntas = new ArrayList<ArrayList<Pregunta>>();
		GestorBD.conectar("root", "a112358132134");
		GestorBD.hacerSelect("preguntas");
		for (int i = 0; i < 6; i++) {
			arrayPreguntas.add(new ArrayList<Pregunta>());
		}
		try {
			while (GestorBD.getRs().next()) {
				arrayResp = new String[6];
				arrayResp[0] = GestorBD.getRs().getString("incorrecta1");
				arrayResp[1] = GestorBD.getRs().getString("incorrecta2");
				arrayResp[2] = GestorBD.getRs().getString("incorrecta3");
				arrayResp[3] = GestorBD.getRs().getString("incorrecta4");
				arrayResp[4] = GestorBD.getRs().getString("incorrecta5");
				arrayResp[5] = GestorBD.getRs().getString("incorrecta6");				
				arrayPreguntas.get(GestorBD.getRs().getInt("idTema")).add(new Pregunta(GestorBD.getRs().getString("pregunta"), GestorBD.getRs().getString("respuesta"), arrayResp));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Método que de devuelve una pregunta aleatoria
	 * 
	 * @param tema Tema de la pregunta que se devolverá
	 * 
	 * @return Devuelve una pregunta aleatoria
	 */
	public Pregunta getPregunta(int tema) {
		return arrayPreguntas.get(tema).get((int)(Math.random()*arrayPreguntas.get(tema).size()+0));
	}
	
}