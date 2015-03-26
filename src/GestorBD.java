import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Clase que gestiona las operaciones que se realizan contra la base de datos.
 *
 * @author Artzai San José
 *
 * @version 03/06/2014 v1.0
*/
public class GestorBD {
	
	private static Connection conexionMYSQL = null;
	private static Statement stmt = null;
	private static String bd = "jdbc:mysql://localhost/trivialbd";
	private static ResultSet rs;
	
	/**
     * Método necesario para conectarse al Driver y poder usar MySQL.
    */
	public static void conectar(String user, String pass) {
		try {
			conexionMYSQL = DriverManager.getConnection(bd, user, pass);
			stmt = (Statement) conexionMYSQL.createStatement();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
    }    
    
	/**
     * Método para hacer el commit en la base de datos
    */
    public static void hacerCommit() {
    	try {
			stmt.execute("commit");
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Método para hacer un update en la base de datos
     * 
     * @param ganada Indica si se le suba uno a las partidas ganadas o a las perdidas
     * @param usuario Indica el usuario al que se le modifica la entrada
     */
    public static void hacerUpdate(boolean ganada, String usuario) {
    	try {
    		if (ganada) {
    			stmt.executeUpdate("update usuarios set p_ganadas = p_ganadas + 1 where nombre = '"+usuario+"'");
    		}
    		else {
    			stmt.executeUpdate("update usuarios set p_perdidas = p_perdidas + 1 where nombre = '"+usuario+"'");
    		}
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Método para hacer un update en la base de datos desde la ventana de administrador 
     * 
     * @param usuario Indica el usuario al que se le modifica la entrada
     * @param ganadas Indica las partidas ganadas que tiene
     * @param perdidas Indica las partidas perdidas que tiene
     */
    public static void hacerUpdateCompleta(String usuario, int ganadas, int perdidas) {
    	try {    		
    		stmt.executeUpdate("update usuarios set p_ganadas = '"+ganadas+"', p_perdidas = '"+perdidas+"' where nombre = '"+usuario+"'");    		
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Método que devuelve el ResultSet
     * 
     * @return Devuelve el ResultSet con los datos de la última select
    */
    public static ResultSet getRs() {
		return rs;
	}    
    
    /**
     * Método para insertar nuevos usuarios a la tabla de usuarios
     * 
     * @param user Nombre del usuario que se inserta a la tabla
     * @param pass Contraseña encriptada del usuario
     */
	public static void hacerInsertUsuario(String user, String pass) {
		String sql = "insert into usuarios (nombre, p_ganadas, p_perdidas, contrasena)";
		sql += "values(";
		sql += "'"+user+"',";
		sql += "'"+0+"',";
		sql += "'"+0+"',";
		sql += "'"+pass+"')";
		try {
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {			
			e.printStackTrace();
		}
    }
	
	/**
	 * Método para insertar nuevas preguntas a la base de datos
	 * 
	 * @param pregunta Enunciado de la pregunta
	 * @param respuesta Respuesta correcta a la pregunta
	 * @param inc1 Respuesta incorrecta
	 * @param inc2 Respuesta incorrecta
	 * @param inc3 Respuesta incorrecta
	 * @param inc4 Respuesta incorrecta
	 * @param inc5 Respuesta incorrecta
	 * @param inc6 Respuesta incorrecta
	 * @param tema Tema al que pertenece la pregunta
	 */
	public static void hacerInsertPregunta(String pregunta, String respuesta, String inc1, String inc2, String inc3, String inc4, String inc5, String inc6, int tema) {
		String sql = "insert into preguntas (pregunta, idTema,  respuesta, incorrecta1, incorrecta2, incorrecta3, incorrecta4, incorrecta5, incorrecta6)";
		sql += "values(";
		sql += "'"+pregunta+"',";
		sql += "'"+tema+"',";
		sql += "'"+respuesta+"',";
		sql += "'"+inc1+"',";
		sql += "'"+inc2+"',";
		sql += "'"+inc3+"',";
		sql += "'"+inc4+"',";
		sql += "'"+inc5+"',";
		sql += "'"+inc6+"')";
		try {
			stmt.executeUpdate(sql);
		}
		catch (SQLException e) {			
			e.printStackTrace();
		}
    }
    	
	/**
	 * Método para hacer select de una tabla completa
	 * 
	 * @param tabla Tabla en la que se hará el select
	 */
    public static void hacerSelect(String tabla){
    	try {	    	
			rs = stmt.executeQuery("SELECT * FROM "+tabla);			
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}
    }   
    
    /**
     * Método para hacer selects con sentencias concretas
     * 
     * @param select Sentencia de select que se ejecutará
     */
    public static void hacerSelectConcreta(String select){
    	try {	    	
			rs = stmt.executeQuery(select);			
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}    	
    }   
        
    /**
     * Método para hacer delete en la base de datos
     * 
     * @param sql Sentencia de delete que se ejecutará
     */
    public static void hacerDelete(String sql){
    	try {	    	
			stmt.executeUpdate(sql);			
		} 
    	catch (SQLException e) {
			e.printStackTrace();
		}    	
    } 
    
    /**
     * Método para cerrar el ResultSet
     */
    public static void cerrarRs() {
        if (rs != null) {
            try {
                rs.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Método que cierra la conexión con la base de datos
     */
    public static void cerrarConexion() {
        try {
        	conexionMYSQL.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}