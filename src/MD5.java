import java.security.MessageDigest;

/**
 * Clase que encripta contraseñas antes de meterlas a la base de datos
 * 
 * @author Google.com
 */
public class MD5 {
	
	/**
	 * Método que recibe una contraseña y la devuelve encriptada
	 * 
	 * @param pass Contraseña a encriptar
	 * 
	 * @return String Devuelve la contraseña encriptada
	 * 
	 * @throws Exception
	 */
	public static String encriptar(String pass) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(pass.getBytes());
		int size = b.length;
		StringBuffer h = new StringBuffer(size);			
		for (int i = 0; i < size; i++) {
			int u = b[i] & 255;
			if (u < 16) {
				h.append("0" + Integer.toHexString(u));
			}
			else {
				h.append(Integer.toHexString(u));
			}
		}			
	return h.toString();
	}
		
}