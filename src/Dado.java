import javax.swing.*;

/**
 * Clase que define el dado del juego
 *
 * @author Artzai San José
 *
 * @version 03/06/2014 v1.0
*/
public class Dado extends JLabel implements Runnable {
	
	private ImageIcon[] arrayImagenes = {new ImageIcon("imagenes/dado1.png"), 
										new ImageIcon("imagenes/dado2.png"), 
										new ImageIcon("imagenes/dado3.png"), 
										new ImageIcon("imagenes/dado4.png"), 
										new ImageIcon("imagenes/dado5.png"), 
										new ImageIcon("imagenes/dado6.png") };
	private int valor;
	
	/**
     * Constructor que define las propiedades del dado
    */
	public Dado() {	
		this.setSize(90, 90);
		this.cambiarImagen(arrayImagenes[0]);
		valor = 1;
	}	

	/**
     * Método que cambia la imagen del dado
     * 
     * @param img La imagen que se le asigna al dado
    */
	private void cambiarImagen(Icon img) {
		int imagen = (int)(Math.random()*arrayImagenes.length+0);
		if (arrayImagenes[imagen] != img) {
			this.setIcon(arrayImagenes[imagen]);
			valor = imagen;
		}
		else {
			this.cambiarImagen(img);
		}
	}
	//GETTERS & SETTERS
	public int getValor() {
		return valor;
	}
	
	/**
     * Método run del thread, que llama a cambiarImagen de forma aleatoria para que funcione el dado      
    */
	public void run() {
		while (true) {
			this.cambiarImagen(this.getIcon());
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

}