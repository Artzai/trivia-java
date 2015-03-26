import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;

/**
 * Clase principal del programa, que define la ventana principal de la aplicación y gestiona el juego.
 *
 * @author Artzai San José
 *
 * @version 03/06/2014 v1.0
*/
public class FramePrincipal extends JFrame implements MouseListener, MouseMotionListener, WindowListener {	
	
	private JLabel fondoTrampa, fondoReto, fondoPantalla, fondoPregunta, txtPregunta, txtRespuesta[], fondoRespuesta[], lblQuesito, lblQuesitos2[], lblQuesitos1[], fondoJugador1, fondoJugador2, lblCentral, txtSiguiente, fondoSiguiente, fondoComenzar, txtComenzar, lblWin, fondoAdmin, txtAdmin;
	private static JLabel txtJugador1, txtJugador2;
	private ImageIcon imgTrampa, imgReto, imgFondo, imgFondoPregunta, imgRespuesta, imgRespuestaCorrecta, imgRespuestaIncorrecta, quesitoAmarillo, quesitoAzul, quesitoRojo, quesitoVerde, quesitoNaranja, quesitoRosa, quesitos[], quesitosTrans[], fondoJug, imgCentralActivo, imgCentralInactivo, imgWin;
	private Dado dado;
	private Thread thread1;
	private PreguntasRespuestas preguntasRespuestas;
	private String arrayRespuestas[];
	private int jugadorActual, aciertosFinales = 0, posicionActual[] = {0, 0}, temaActual;	
	private Cursor cursorMano, cursorNormal;
	private Pregunta pregunta;
	private JSeparator separator;
	private Casilla central, arrayPoligonos[][];
	private static Jugador jugador1, jugador2;
	private boolean tiradaFinal = false, reto = false, trampa = false;
	private static ArrayList<String[]> usuarios;
	private FormAuth fAuth;	
	private GregorianCalendar calendario;
	private SimpleDateFormat formatoFecha;
	private String fechaActual;
	
	/**
     * Constructor que define las propiedades de la ventana principal y recoge todos los usuarios de la base de datos	 
    */
	public FramePrincipal() {
		this.setLayout(null);
		this.setBounds(600, 100, 600, 825);
		this.setTitle("Wendel's Trivia");
		jugador1 = new Jugador("Invitado1");
		jugador2 = new Jugador("Invitado2");
		this.colocarComponentes();
		this.crearTablero();		
		this.addMouseMotionListener(this);						
		jugadorActual = 1;
		temaActual = 0;
		GestorBD.hacerSelect("usuarios");
		usuarios = new ArrayList<String[]>(); 
		try {
			while (GestorBD.getRs().next()) {
				String user = GestorBD.getRs().getString("nombre");
				String pass = GestorBD.getRs().getString("contrasena");
				String[] us = {user, pass};
				usuarios.add(us);
			}
		}
		catch (SQLException e) {			
			e.printStackTrace();
		}	
	}	
	
	/**
     * Método donde se instancian y colocan todos los componentes que contendrá el frame principal del juego 
    */
	private void colocarComponentes() {	
		calendario = new GregorianCalendar();
		
		imgWin = new ImageIcon("imagenes/win2.png");
		lblWin = new JLabel(imgWin);
		lblWin.setBounds(200, 175, 180, 133);
		lblWin.setVisible(false);
		this.add(lblWin);
		
		quesitos = new ImageIcon[6];
		quesitos[0] = new ImageIcon("imagenes/quesitoAzul2.png");
		quesitos[1] = new ImageIcon("imagenes/quesitoNaranja2.png");
		quesitos[2] = new ImageIcon("imagenes/quesitoVerde2.png");
		quesitos[3] = new ImageIcon("imagenes/quesitoRojo2.png");
		quesitos[4] = new ImageIcon("imagenes/quesitoAmarillo2.png");
		quesitos[5] = new ImageIcon("imagenes/quesitoRosa2.png");
		
		quesitosTrans = new ImageIcon[6];
		quesitosTrans[0] = new ImageIcon("imagenes/quesitoAzul2trans.png");
		quesitosTrans[1] = new ImageIcon("imagenes/quesitoNaranja2trans.png");
		quesitosTrans[2] = new ImageIcon("imagenes/quesitoVerde2trans.png");
		quesitosTrans[3] = new ImageIcon("imagenes/quesitoRojo2trans.png");
		quesitosTrans[4] = new ImageIcon("imagenes/quesitoAmarillo2trans.png");
		quesitosTrans[5] = new ImageIcon("imagenes/quesitoRosa2trans.png");
		
		lblQuesitos1 = new JLabel[6];
		int horiz = 405, vert = 440;
		for (int i = 0; i < lblQuesitos1.length; i++) {
			lblQuesitos1[i] = new JLabel(quesitosTrans[i]);
			lblQuesitos1[i].setBounds(horiz, vert, 40, 40);
			this.add(lblQuesitos1[i]);
			horiz += 33;
			if (i == 2) {
				horiz = 405;
				vert += 30;
			}
		}
		
		lblQuesitos2 = new JLabel[6];
		int horiz2 = 405, vert2 = 700;
		for (int i = 0; i < lblQuesitos1.length; i++) {
			lblQuesitos2[i] = new JLabel(quesitosTrans[i]);
			lblQuesitos2[i].setBounds(horiz2, vert2, 40, 40);
			this.add(lblQuesitos2[i]);
			horiz2 += 33;
			if (i == 2) {
				horiz2 = 405;
				vert2 += 30;
			}
		}
		
		separator = new JSeparator();
		separator.setBounds(10, 400, 564, 2);	
		
		cursorMano = new Cursor(Cursor.HAND_CURSOR);
		cursorNormal = new Cursor(Cursor.DEFAULT_CURSOR);
		
		imgRespuesta = new ImageIcon("imagenes/botonRespuesta.png");
		imgRespuestaCorrecta = new ImageIcon("imagenes/botonRespuestaCorrecta.png");
		imgRespuestaIncorrecta = new ImageIcon("imagenes/botonRespuestaIncorrecta.png");
		imgFondo = new ImageIcon("imagenes/fondo.png");
		imgFondoPregunta = new ImageIcon("imagenes/fondoPregunta.png");
		quesitoAmarillo = new ImageIcon("imagenes/quesitoAmarillo.png");
		quesitoRosa = new ImageIcon("imagenes/quesitoRosa.png");
		quesitoVerde = new ImageIcon("imagenes/quesitoVerde.png");
		quesitoAzul = new ImageIcon("imagenes/quesitoAzul.png");
		quesitoRojo = new ImageIcon("imagenes/quesitoRojo.png");
		quesitoNaranja = new ImageIcon("imagenes/quesitoNaranja.png");	
		fondoJug = new ImageIcon("imagenes/fondoJugador.png");
		imgCentralActivo = new ImageIcon("imagenes/centralActivo.png");
		imgCentralInactivo = new ImageIcon("imagenes/centralInactivo.png");
		imgReto = new ImageIcon("imagenes/reto.png");
		imgTrampa = new ImageIcon("imagenes/trampa.png");
		
		txtAdmin = new JLabel("Administrador", SwingConstants.CENTER);
		txtAdmin.setBounds(420, 20, 150, 35);
		txtAdmin.setCursor(cursorMano);
		txtAdmin.addMouseListener(this);
		this.add(txtAdmin);
		
		fondoAdmin = new JLabel(imgRespuesta);
		fondoAdmin.setBounds(420, 20, 150, 35);
		this.add(fondoAdmin);
		
		fondoReto = new JLabel(imgReto);
		fondoReto.setBounds(215, 60, 150, 50);
		fondoReto.setVisible(false);
		this.add(fondoReto);
		
		fondoTrampa = new JLabel(imgTrampa);
		fondoTrampa.setBounds(215, 60, 150, 50);
		fondoTrampa.setVisible(false);
		this.add(fondoTrampa);
		
		lblCentral = new JLabel();
		lblCentral.setBounds(158, 567, 80, 80);
		this.add(lblCentral);
		
		fondoPantalla = new JLabel(imgFondo);
		fondoPantalla.setBounds(0, 0, 600, 800);
		
		dado = new Dado();
		dado.setLocation(410, 560);
		dado.setCursor(cursorMano);
		dado.addMouseListener(this);		
		
		txtPregunta = new JLabel("",SwingConstants.CENTER);
		txtPregunta.setBounds(110, 120, 360, 50);
		this.add(txtPregunta);
		
		fondoPregunta = new JLabel(imgFondoPregunta);
		fondoPregunta.setBounds(90, 120, 400, 50);		
		this.add(fondoPregunta);	
		
		txtSiguiente = new JLabel("Siguiente", SwingConstants.CENTER);
		txtSiguiente.setBounds(215, 315, 150, 35);
		txtSiguiente.setCursor(cursorMano);
		txtSiguiente.setVisible(false);
		this.add(txtSiguiente);
		
		fondoSiguiente = new JLabel(fondoJug);
		fondoSiguiente.setBounds(215, 315, 150, 35);
		fondoSiguiente.addMouseListener(this);	
		fondoSiguiente.setVisible(false);
		this.add(fondoSiguiente);
		
		txtComenzar = new JLabel("¡Comenzar!", SwingConstants.CENTER);
		txtComenzar.setBounds(380, 585, 150, 35);
		txtComenzar.setCursor(cursorMano);
		this.add(txtComenzar);
		
		fondoComenzar = new JLabel(imgRespuestaCorrecta);
		fondoComenzar.setBounds(380, 585, 150, 35);
		fondoComenzar.addMouseListener(this);
		this.add(fondoComenzar);
		
		txtJugador1 = new JLabel(jugador1.getNombre(), SwingConstants.CENTER);
		txtJugador1.setBounds(380, 515, 150, 35);
		txtJugador1.setCursor(cursorMano);
		this.add(txtJugador1);
		
		fondoJugador1 = new JLabel(imgRespuesta);
		fondoJugador1.setBounds(380, 515, 150, 35);
		fondoJugador1.addMouseListener(this);		
		this.add(fondoJugador1);
		
		txtJugador2 = new JLabel(jugador2.getNombre(), SwingConstants.CENTER);
		txtJugador2.setBounds(380, 660, 150, 35);
		txtJugador2.setCursor(cursorMano);
		this.add(txtJugador2);
		
		fondoJugador2 = new JLabel(imgRespuesta);
		fondoJugador2.setBounds(380, 660, 150, 35);
		fondoJugador2.addMouseListener(this);	
		this.add(fondoJugador2);
		
		txtRespuesta = new JLabel[4];
		txtRespuesta[0] = new JLabel("",SwingConstants.CENTER);
		txtRespuesta[0].setBounds(130, 200, 150, 35);		
		txtRespuesta[0].setCursor(cursorMano);
		this.add(txtRespuesta[0]);
		
		fondoRespuesta = new JLabel[4];
		fondoRespuesta[0] = new JLabel(imgRespuesta);
		fondoRespuesta[0].setBounds(130, 200, 150, 35);
		this.add(fondoRespuesta[0]);
		
		txtRespuesta[1] = new JLabel("",SwingConstants.CENTER);
		txtRespuesta[1].setBounds(300, 200, 150, 35);		
		txtRespuesta[1].setCursor(cursorMano);
		this.add(txtRespuesta[1]);
		
		fondoRespuesta[1] = new JLabel(imgRespuesta);
		fondoRespuesta[1].setBounds(300, 200, 150, 35);
		this.add(fondoRespuesta[1]);
		
		txtRespuesta[2] = new JLabel("",SwingConstants.CENTER);
		txtRespuesta[2].setBounds(130, 250, 150, 35);		
		txtRespuesta[2].setCursor(cursorMano);
		this.add(txtRespuesta[2]);
		
		fondoRespuesta[2] = new JLabel(imgRespuesta);
		fondoRespuesta[2].setBounds(130, 250, 150, 35);
		this.add(fondoRespuesta[2]);
		
		txtRespuesta[3] = new JLabel("",SwingConstants.CENTER);
		txtRespuesta[3].setBounds(300, 250, 150, 35);		
		txtRespuesta[3].setCursor(cursorMano);
		this.add(txtRespuesta[3]);
		
		fondoRespuesta[3] = new JLabel(imgRespuesta);
		fondoRespuesta[3].setBounds(300, 250, 150, 35);
		this.add(fondoRespuesta[3]);
		
		lblQuesito = new JLabel();
		lblQuesito.setBounds(250, 45, 80, 80);
		this.add(lblQuesito);	
						
		thread1 = new Thread(dado);		
		this.add(dado);
		dado.setVisible(false);
		thread1.start();
		this.add(separator);
		this.add(fondoPantalla);			
		preguntasRespuestas = new PreguntasRespuestas();		
		this.setVisibleOno(false, false);
		this.addMouseListener(this);	
		this.comprobarQuesitos(jugador1);	
		
	}
	
	/**
     * Método que devuelve los usuarios que están conectados en ese momento en el juego
	 * 
     * @return ArrayList<String[]> Devuelve un ArrayList con los datos de los dos jugadores
    */
	public static ArrayList<String[]> getUsuarios() {
		return usuarios;
	}
	
	/**
	 * Método para guardar lineas de texto en el log con la fecha actual
	 * 
	 * @param linea La línea que se guardará en el log
	*/
	public void guardarLog(String linea) {
		try {
			formatoFecha = new SimpleDateFormat("dd/MM/yyyy,HH:mm:ss");
			fechaActual = formatoFecha.format(calendario.getTime());
			BufferedWriter fS=new BufferedWriter(new FileWriter("Documentos/log.txt",true));			
			fS.write(fechaActual+", "+linea);	
			fS.newLine();			
			fS.close();
		}
		catch (IOException ioE) {
			JOptionPane.showMessageDialog(this, "Error de escritura del log");
		}
	}
	
	/**
     * Método que devuelve el usuario que que tiene el turno en ese momento del juego
	 * 
     * @return Jugador Devuelve un objeto Jugador con los datos del jugador en turno
    */
	public static Jugador getJugador(int jug) {
		if (jug == 1) {
			return jugador1;
		}
		else {
			return jugador2;
		}
	}	
	
	/**
     * Método para organizar la pantalla de tal forma que puede empezar la partida
    */
	private void comenzarPartida() {
		fondoJugador1.removeMouseListener(this);
		fondoJugador2.removeMouseListener(this);
		txtJugador1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		txtJugador2.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		fondoComenzar.setVisible(false);
		txtComenzar.setVisible(false);
		dado.setVisible(true);
		fondoJugador1.setIcon(fondoJug);
		arrayPoligonos[(int)(Math.random()*41+0)][0].setFichaNegra(true);
		arrayPoligonos[(int)(Math.random()*41+0)][0].setFichaBlanca(true);	
		txtAdmin.setVisible(false);
		fondoAdmin.setVisible(false);
		this.repaint();
	}
	
	/**
     * Método para volver a poner los componentes a cero, igual que al ejecutar el programa
    */
	private void nuevaPartida() {
		fondoJugador1.addMouseListener(this);
		fondoJugador2.addMouseListener(this);
		txtJugador1.setCursor(cursorMano);
		txtJugador2.setCursor(cursorMano);
		fondoComenzar.setVisible(true);
		txtComenzar.setVisible(true);
		dado.setVisible(false);
		fondoJugador1.setIcon(imgRespuesta);
		fondoJugador2.setIcon(imgRespuesta);
		fondoSiguiente.setVisible(false);
		txtSiguiente.setVisible(false);
		fondoPregunta.setVisible(false);
		txtPregunta.setVisible(false);
		this.addMouseListener(this);
		thread1.resume();
		dado.setCursor(cursorMano);
		central.setFichaBlanca(false);
		central.setFichaNegra(false);
		for (int i = 0; i < arrayPoligonos.length; i++) {
			for (int j = 0; j < arrayPoligonos[i].length; j++) {
				arrayPoligonos[i][j].setFichaBlanca(false);
				arrayPoligonos[i][j].setFichaNegra(false);
			}
		}
		for (int i = 0; i < 6; i++) {
			jugador1.setQuesito(false, i);
			jugador2.setQuesito(false, i);
		}
		this.setQuesitos();
		this.repaint();			
		dado.addMouseListener(this);
		lblWin.setVisible(false);
	}
	
	/**
     * Método para cambiar el turno de un jugador al otro
    */
	private void cambiarJugador() {		
		if (jugadorActual == 1) {
			jugadorActual = 2;
			fondoJugador1.setIcon(imgRespuesta);
			fondoJugador2.setIcon(fondoJug);
			this.comprobarQuesitos(jugador2);
		}
		else {
			jugadorActual = 1;
			fondoJugador2.setIcon(imgRespuesta);
			fondoJugador1.setIcon(fondoJug);
			this.comprobarQuesitos(jugador1);
		}
		
	}
	
	/**
	 * Método estatico para cambiar los usuarios en juego
	 * 
	 * @param user Integer que indica cual es el jugador a coger el turno
	 * @param usuario Nombre del jugador
	*/
	public static void setUsuario(int user, String usuario) {
		if (user == 1) {
			txtJugador1.setText(usuario);
			jugador1.setNombre(usuario);
		}
		else {
			txtJugador2.setText(usuario);
			jugador2.setNombre(usuario);
		}
	}
	
	/**
     * Método para actualizar las imagenes de los quesitos de cada jugador
    */
	private void setQuesitos() {
		for (int i = 0; i < jugador1.getQuesitos().length; i++) {
			if (jugador1.getQuesitos()[i]) {
				lblQuesitos1[i].setIcon(quesitos[i]);
			}
			else {
				lblQuesitos1[i].setIcon(quesitosTrans[i]);
			}
		}
		for (int i = 0; i < jugador2.getQuesitos().length; i++) {
			if (jugador2.getQuesitos()[i]) {
				lblQuesitos2[i].setIcon(quesitos[i]);
			}
			else {
				lblQuesitos2[i].setIcon(quesitosTrans[i]);
			}
		}
	}	
	
	/**
	 * Método para dar un quesito aleatorio a un jugador
	 * 
	 * @param jugador Integer que indica cual es el jugador que recibirá el quesito
	 *   
	 * @throws StackOverflowError Si por mala suerte el random sale mal demasiadas veces
	*/
	private void darQuesito(int jugador) throws StackOverflowError {
		int tema = (int)(Math.random()*5+0);		
		if (jugador == 1) {			
			if (jugador1.faltaQuesito()) {
				if (!jugador1.getQuesitos()[tema]) {
					jugador1.setQuesito(true, tema);
				}
				else {
					this.darQuesito(jugador);
				}
			}
		}
		else {			
			if (jugador2.faltaQuesito()) {
				if (!jugador2.getQuesitos()[tema]) {
					jugador2.setQuesito(true, tema);
				}
				else {
					this.darQuesito(jugador);
				}
			}
		}
		this.setQuesitos();
	}
	
	/**
	 * Método para quitar un quesito aleatorio a un jugador
	 * 
	 * @param jugador Integer que indica cual es el jugador que perderá el quesito
	 *   
	 * @throws StackOverflowError Si por mala suerte el random sale mal demasiadas veces
	*/
	private void quitarQuesito(int jugador) throws StackOverflowError {
		int tema = (int)(Math.random()*5+0);		
		if (jugador == 1) {					
			if (jugador1.hasQuesito()) {
				if (jugador1.getQuesitos()[tema]) {
					jugador1.setQuesito(false, tema);
				}
				else {
					this.quitarQuesito(jugador);
				}
			}
		}
		else {			
			if (jugador2.hasQuesito()) {
				if (jugador2.getQuesitos()[tema]) {
					jugador2.setQuesito(false, tema);
				}
				else {
					this.quitarQuesito(jugador);
				}
			}
		}
		this.setQuesitos();
	}
	
	/**
	 * Método para comprobar si el jugador tiene todos los quesitos e iluminar la casilla central
	 * 
	 * @param jugador Objeto jugador al que se le comprobaran los quesitos
	 * 
	 * @return boolean tieneTodos Devuelve true si tiene los 6 quesitos
	*/
	private boolean comprobarQuesitos(Jugador jugador) {
		boolean tieneTodos = true;
		for (int i = 0; i < jugador.getQuesitos().length; i++) {
			if (!jugador.getQuesitos()[i]) {
				tieneTodos = false;
			}
		}		
		if (tieneTodos) {
			lblCentral.setIcon(imgCentralActivo);			
		}
		else {
			lblCentral.setIcon(imgCentralInactivo);
		}
		return tieneTodos;
	}
	
	/**
	 * Método para hacer una pregunta del tema elegido
	 * 
	 * @param tema El tema sobre el que se hará la pregunta
	*/
	private void hacerPregunta(int tema) {
		pregunta = preguntasRespuestas.getPregunta(tema);
		arrayRespuestas = pregunta.getRespuestas();
		txtPregunta.setText(pregunta.getPregunta());
		for (int e = 0; e < txtRespuesta.length; e++) {
			txtRespuesta[e].setText(arrayRespuestas[e]);
		}				
		this.setVisibleOno(true, false);
		dado.removeMouseListener(this);	
		for (int e = 0; e < txtRespuesta.length; e++) {
			txtRespuesta[e].addMouseListener(this);
		}
		for (int e = 0; e < fondoRespuesta.length; e++) {
			fondoRespuesta[e].setIcon(imgRespuesta);
		}			
	}	
	
	/**
	 * Método que crea el tablero invisible, el utilizado por las fichas
	*/
	private void crearTablero() {		
		int cordsCentralX[] = {188, 162, 162, 188, 214, 214};
		int cordsCentralY[] = {602, 618, 648, 664, 648, 618};
		central = new Casilla(cordsCentralX, cordsCentralY, 6, false, 3, 6);
		arrayPoligonos = new Casilla[42][];
		for (int i = 0; i < arrayPoligonos.length; i++) {
			if (i == 6 || i == 13 || i == 20 || i == 27 || i == 34 || i == 41) {
				arrayPoligonos[i] = new Casilla[7];
			}
			else {
				arrayPoligonos[i] = new Casilla[1];	
			}
		}
		int cordsX[] = {143, 156, 156, 143};
		int cordsY[] = {488, 488, 520, 520};		
		arrayPoligonos[0][0] = new Casilla(cordsX, cordsY, 4, false, 0, 0);		
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 15;
		}
		arrayPoligonos[1][0] = new Casilla(cordsX, cordsY, 4, false, 0, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 15;
		}
		arrayPoligonos[2][0] = new Casilla(cordsX, cordsY, 4, false, 0, 7);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 15;
		}
		arrayPoligonos[3][0] = new Casilla(cordsX, cordsY, 4, false, 0, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 16;
		}
		arrayPoligonos[4][0] = new Casilla(cordsX, cordsY, 4, false, 0, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 16;
		}
		arrayPoligonos[5][0] = new Casilla(cordsX, cordsY, 4, false, 0, 4);		
		cordsX[0] = 291;
		cordsX[1] = 297;
		cordsX[2] = 270;
		cordsX[3] = 263;
		cordsY[0] = 520;
		cordsY[1] = 532;
		cordsY[2] = 547;
		cordsY[3] = 535;		
		arrayPoligonos[7][0] = new Casilla(cordsX, cordsY, 4, false, 1, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 14;
		}
		arrayPoligonos[8][0] = new Casilla(cordsX, cordsY, 4, false, 1, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[9][0] = new Casilla(cordsX, cordsY, 4, false, 1, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 14;
		}
		arrayPoligonos[10][0] = new Casilla(cordsX, cordsY, 4, false, 1, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[11][0] = new Casilla(cordsX, cordsY, 4, false, 1, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 14;
		}
		arrayPoligonos[12][0] = new Casilla(cordsX, cordsY, 4, false, 1, 1);		
		cordsX[0] = 336;
		cordsX[1] = 330;
		cordsX[2] = 303;
		cordsX[3] = 309;
		cordsY[0] = 665;
		cordsY[1] = 677;
		cordsY[2] = 662;
		cordsY[3] = 650;
		arrayPoligonos[14][0] = new Casilla(cordsX, cordsY, 4, false, 2, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[15][0] = new Casilla(cordsX, cordsY, 4, false, 2, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[16][0] = new Casilla(cordsX, cordsY, 4, false, 2, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 7;
			cordsY[i] += 14;
		}
		arrayPoligonos[17][0] = new Casilla(cordsX, cordsY, 4, false, 2, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[18][0] = new Casilla(cordsX, cordsY, 4, false, 2, 7);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[19][0] = new Casilla(cordsX, cordsY, 4, false, 2, 1);
		cordsX[0] = 220;
		cordsX[1] = 233;
		cordsX[2] = 233;
		cordsX[3] = 220;
		cordsY[0] = 745;
		cordsY[1] = 745;
		cordsY[2] = 776;
		cordsY[3] = 776;
		arrayPoligonos[21][0] = new Casilla(cordsX, cordsY, 4, false, 0, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 15;
		}
		arrayPoligonos[22][0] = new Casilla(cordsX, cordsY, 4, false, 0, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 15;
		}
		arrayPoligonos[23][0] = new Casilla(cordsX, cordsY, 4, false, 0, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 16;
		}
		arrayPoligonos[24][0] = new Casilla(cordsX, cordsY, 4, false, 0, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 15;
		}
		arrayPoligonos[25][0] = new Casilla(cordsX, cordsY, 4, false, 0, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 16;
		}
		arrayPoligonos[26][0] = new Casilla(cordsX, cordsY, 4, false, 0, 3);
		cordsX[0] = 106;
		cordsX[1] = 111;
		cordsX[2] = 85;
		cordsX[3] = 78;
		cordsY[0] = 716;
		cordsY[1] = 728;
		cordsY[2] = 743;
		cordsY[3] = 732;
		arrayPoligonos[28][0] = new Casilla(cordsX, cordsY, 4, false, 1, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[29][0] = new Casilla(cordsX, cordsY, 4, false, 1, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 7;
			cordsY[i] -= 13;
		}
		arrayPoligonos[30][0] = new Casilla(cordsX, cordsY, 4, false, 1, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 7;
			cordsY[i] -= 14;
		}
		arrayPoligonos[31][0] = new Casilla(cordsX, cordsY, 4, false, 1, 7);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 9;
			cordsY[i] -= 13;
		}
		arrayPoligonos[32][0] = new Casilla(cordsX, cordsY, 4, false, 1, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[33][0] = new Casilla(cordsX, cordsY, 4, false, 1, 5);
		cordsX[0] = 40;
		cordsX[1] = 46;
		cordsX[2] = 73;
		cordsX[3] = 66;
		cordsY[0] = 599;
		cordsY[1] = 588;
		cordsY[2] = 604;
		cordsY[3] = 614;
		arrayPoligonos[35][0] = new Casilla(cordsX, cordsY, 4, false, 2, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 7;
			cordsY[i] -= 14;
		}
		arrayPoligonos[36][0] = new Casilla(cordsX, cordsY, 4, false, 2, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 14;
		}
		arrayPoligonos[37][0] = new Casilla(cordsX, cordsY, 4, false, 2, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[38][0] = new Casilla(cordsX, cordsY, 4, false, 2, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[39][0] = new Casilla(cordsX, cordsY, 4, false, 2, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 14;
		}
		arrayPoligonos[40][0] = new Casilla(cordsX, cordsY, 4, false, 2, 1);
		
		int cordsQuesoX[] = {139, 139, 133, 119, 91, 85, 89, 113};
		int cordsQuesoY[] = {519, 490, 482, 476, 489, 505, 519, 532};
		arrayPoligonos[41][0] = new Casilla(cordsQuesoX, cordsQuesoY, 8, true, 3, 5);
		cordsQuesoX[0] = 235;
		cordsQuesoX[1] = 261;
		cordsQuesoX[2] = 283;
		cordsQuesoX[3] = 289;
		cordsQuesoX[4] = 274;
		cordsQuesoX[5] = 253;
		cordsQuesoX[6] = 236;
		cordsQuesoX[7] = 236;
		cordsQuesoY[0] = 519;
		cordsQuesoY[1] = 534;
		cordsQuesoY[2] = 521;
		cordsQuesoY[3] = 503;
		cordsQuesoY[4] = 483;
		cordsQuesoY[5] = 478;
		cordsQuesoY[6] = 489;
		cordsQuesoY[7] = 489;
		arrayPoligonos[6][0] = new Casilla(cordsQuesoX, cordsQuesoY, 8, true, 3, 1);
		cordsQuesoX[0] = 310;
		cordsQuesoX[1] = 310;
		cordsQuesoX[2] = 336;
		cordsQuesoX[3] = 352;
		cordsQuesoX[4] = 361;
		cordsQuesoX[5] = 357;
		cordsQuesoX[6] = 345;
		cordsQuesoX[7] = 336;
		cordsQuesoY[0] = 619;
		cordsQuesoY[1] = 646;
		cordsQuesoY[2] = 661;
		cordsQuesoY[3] = 656;
		cordsQuesoY[4] = 634;
		cordsQuesoY[5] = 615;
		cordsQuesoY[6] = 605;
		cordsQuesoY[7] = 604;
		arrayPoligonos[13][0] = new Casilla(cordsQuesoX, cordsQuesoY, 8, true, 3, 2);
		cordsQuesoX[0] = 262;
		cordsQuesoX[1] = 236;
		cordsQuesoX[2] = 236;
		cordsQuesoX[3] = 251;
		cordsQuesoX[4] = 271;
		cordsQuesoX[5] = 284;
		cordsQuesoX[6] = 291;
		cordsQuesoX[7] = 288;
		cordsQuesoY[0] = 731;
		cordsQuesoY[1] = 745;
		cordsQuesoY[2] = 774;
		cordsQuesoY[3] = 786;
		cordsQuesoY[4] = 784;
		cordsQuesoY[5] = 774;
		cordsQuesoY[6] = 756;
		cordsQuesoY[7] = 748;
		arrayPoligonos[20][0] = new Casilla(cordsQuesoX, cordsQuesoY, 8, true, 3, 3);
		cordsQuesoX[0] = 114;
		cordsQuesoX[1] = 140;
		cordsQuesoX[2] = 140;
		cordsQuesoX[3] = 129;
		cordsQuesoX[4] = 111;
		cordsQuesoX[5] = 98;
		cordsQuesoX[6] = 86;
		cordsQuesoX[7] = 90;
		cordsQuesoY[0] = 731;
		cordsQuesoY[1] = 745;
		cordsQuesoY[2] = 773;
		cordsQuesoY[3] = 784;
		cordsQuesoY[4] = 785;
		cordsQuesoY[5] = 779;
		cordsQuesoY[6] = 762;
		cordsQuesoY[7] = 746;
		arrayPoligonos[27][0] = new Casilla(cordsQuesoX, cordsQuesoY, 8, true, 3, 0);
		cordsQuesoX[0] = 65;
		cordsQuesoX[1] = 65;
		cordsQuesoX[2] = 38;
		cordsQuesoX[3] = 25;
		cordsQuesoX[4] = 15;
		cordsQuesoX[5] = 16;
		cordsQuesoX[6] = 25;
		cordsQuesoX[7] = 38;
		cordsQuesoY[0] = 619;
		cordsQuesoY[1] = 646;
		cordsQuesoY[2] = 660;
		cordsQuesoY[3] = 656;
		cordsQuesoY[4] = 639;
		cordsQuesoY[5] = 621;
		cordsQuesoY[6] = 607;
		cordsQuesoY[7] = 603;
		arrayPoligonos[34][0] = new Casilla(cordsQuesoX, cordsQuesoY, 8, true, 3, 4);
		cordsX[0] = 261;
		cordsX[1] = 254;
		cordsX[2] = 228;
		cordsX[3] = 234;
		cordsY[0] = 537;
		cordsY[1] = 548;
		cordsY[2] = 533;
		cordsY[3] = 522;
		arrayPoligonos[6][1] = new Casilla(cordsX, cordsY, 4, false, 2, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[6][2] = new Casilla(cordsX, cordsY, 4, false, 2, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 14;
		}
		arrayPoligonos[6][3] = new Casilla(cordsX, cordsY, 4, false, 2, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[6][4] = new Casilla(cordsX, cordsY, 4, false, 2, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[6][5] = new Casilla(cordsX, cordsY, 4, false, 2, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] += 14;
		}
		arrayPoligonos[6][6] = new Casilla(cordsX, cordsY, 4, false, 2, 5);
		cordsX[0] = 294;
		cordsX[1] = 307;
		cordsX[2] = 307;
		cordsX[3] = 294;
		cordsY[0] = 617;
		cordsY[1] = 617;
		cordsY[2] = 648;
		cordsY[3] = 648;
		arrayPoligonos[13][1] = new Casilla(cordsX, cordsY, 4, false, 0, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 15;			
		}
		arrayPoligonos[13][2] = new Casilla(cordsX, cordsY, 4, false, 0, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 16;			
		}
		arrayPoligonos[13][3] = new Casilla(cordsX, cordsY, 4, false, 0, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 15;			
		}
		arrayPoligonos[13][4] = new Casilla(cordsX, cordsY, 4, false, 0, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 16;			
		}
		arrayPoligonos[13][5] = new Casilla(cordsX, cordsY, 4, false, 0, 7);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 16;			
		}
		arrayPoligonos[13][6] = new Casilla(cordsX, cordsY, 4, false, 0, 1);
		cordsX[0] = 254;
		cordsX[1] = 261;
		cordsX[2] = 234;
		cordsX[3] = 227;
		cordsY[0] = 716;
		cordsY[1] = 727;
		cordsY[2] = 743;
		cordsY[3] = 732;
		arrayPoligonos[20][1] = new Casilla(cordsX, cordsY, 4, false, 1, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[20][2] = new Casilla(cordsX, cordsY, 4, false, 1, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] -= 14;
		}
		arrayPoligonos[20][3] = new Casilla(cordsX, cordsY, 4, false, 1, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] -= 14;
		}
		arrayPoligonos[20][4] = new Casilla(cordsX, cordsY, 4, false, 1, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[20][5] = new Casilla(cordsX, cordsY, 4, false, 1, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] -= 7;
			cordsY[i] -= 13;
		}
		arrayPoligonos[20][6] = new Casilla(cordsX, cordsY, 4, false, 1, 2);
		cordsX[0] = 149;
		cordsX[1] = 142;
		cordsX[2] = 115;
		cordsX[3] = 121;
		cordsY[0] = 732;
		cordsY[1] = 743;
		cordsY[2] = 727;
		cordsY[3] = 716;
		arrayPoligonos[27][1] = new Casilla(cordsX, cordsY, 4, false, 2, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 7;
			cordsY[i] -= 14;
		}
		arrayPoligonos[27][2] = new Casilla(cordsX, cordsY, 4, false, 2, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 14;
		}
		arrayPoligonos[27][3] = new Casilla(cordsX, cordsY, 4, false, 2, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[27][4] = new Casilla(cordsX, cordsY, 4, false, 2, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[27][5] = new Casilla(cordsX, cordsY, 4, false, 2, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] -= 13;
		}
		arrayPoligonos[27][6] = new Casilla(cordsX, cordsY, 4, false, 2, 3);
		cordsX[0] = 69;
		cordsX[1] = 82;
		cordsX[2] = 82;
		cordsX[3] = 69;
		cordsY[0] = 617;
		cordsY[1] = 617;
		cordsY[2] = 648;
		cordsY[3] = 648;
		arrayPoligonos[34][1] = new Casilla(cordsX, cordsY, 4, false, 0, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 16;			
		}
		arrayPoligonos[34][2] = new Casilla(cordsX, cordsY, 4, false, 0, 4);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 15;			
		}
		arrayPoligonos[34][3] = new Casilla(cordsX, cordsY, 4, false, 0, 2);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 16;			
		}
		arrayPoligonos[34][4] = new Casilla(cordsX, cordsY, 4, false, 0, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 15;			
		}
		arrayPoligonos[34][5] = new Casilla(cordsX, cordsY, 4, false, 0, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 16;			
		}
		arrayPoligonos[34][6] = new Casilla(cordsX, cordsY, 4, false, 0, 0);		
		cordsX[0] = 142;
		cordsX[1] = 148;
		cordsX[2] = 121;
		cordsX[3] = 115;
		cordsY[0] = 520;
		cordsY[1] = 532;
		cordsY[2] = 548;
		cordsY[3] = 536;
		arrayPoligonos[41][1] = new Casilla(cordsX, cordsY, 4, false, 1, 3);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 14;
		}
		arrayPoligonos[41][2] = new Casilla(cordsX, cordsY, 4, false, 1, 1);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[41][3] = new Casilla(cordsX, cordsY, 4, false, 1, 5);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 7;
			cordsY[i] += 13;
		}
		arrayPoligonos[41][4] = new Casilla(cordsX, cordsY, 4, false, 1, 7);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 14;
		}
		arrayPoligonos[41][5] = new Casilla(cordsX, cordsY, 4, false, 1, 0);
		for (int i = 0; i < 4; i++) {
			cordsX[i] += 8;
			cordsY[i] += 13;
		}
		arrayPoligonos[41][6] = new Casilla(cordsX, cordsY, 4, false, 1, 4);		
	}
	
	/**
	 * Método para quitar un quesito aleatorio a un jugador
	 * 
	 * @param g Graphics que se pintarán en la ventana
	*/
	public void paint(Graphics g) {		
		super.paint(g);		
		g.setColor(Color.BLACK);		
		for (int i = 0; i < arrayPoligonos.length; i++) {
			for (int j = 0; j < arrayPoligonos[i].length; j++) {
				if ((arrayPoligonos[i][j].isPosibilidad() || arrayPoligonos[i][j].isPosibilidadDelante() || arrayPoligonos[i][j].isPosibilidadDetras() || arrayPoligonos[i][j].hasFichaNegra()) && jugadorActual == 1) {
					if (arrayPoligonos[i][j].isPosibilidad() || arrayPoligonos[i][j].isPosibilidadDetras() || arrayPoligonos[i][j].isPosibilidadDelante()) {
						g.setColor(Color.GRAY);
					}					
					switch (arrayPoligonos[i][j].getPosicion()) {
						case 0: g.fillOval(arrayPoligonos[i][j].getBounds().x + 1, arrayPoligonos[i][j].getBounds().y + 2, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 1, arrayPoligonos[i][j].getBounds().y + 2, 10, 10);
							break;
						case 1: g.fillOval(arrayPoligonos[i][j].getBounds().x + 20, arrayPoligonos[i][j].getBounds().y + 4, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 20, arrayPoligonos[i][j].getBounds().y + 4, 10, 10);
							break;
						case 2: g.fillOval(arrayPoligonos[i][j].getBounds().x + 4, arrayPoligonos[i][j].getBounds().y + 5, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 4, arrayPoligonos[i][j].getBounds().y + 5, 10, 10);
							break;
						case 3: g.fillOval(arrayPoligonos[i][j].getBounds().x + 10, arrayPoligonos[i][j].getBounds().y + 15, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 10, arrayPoligonos[i][j].getBounds().y + 15, 10, 10);
							break;
					}	
					g.setColor(Color.BLACK);							
				}				
				if ((arrayPoligonos[i][j].isPosibilidad() || arrayPoligonos[i][j].isPosibilidadDelante() || arrayPoligonos[i][j].isPosibilidadDetras() || arrayPoligonos[i][j].hasFichaBlanca()) && jugadorActual == 2) {
					g.setColor(Color.WHITE);
					if (arrayPoligonos[i][j].isPosibilidad() || arrayPoligonos[i][j].isPosibilidadDetras() || arrayPoligonos[i][j].isPosibilidadDelante()) {
						g.setColor(Color.GRAY);
					}					
					switch (arrayPoligonos[i][j].getPosicion()) {
						case 0: g.fillOval(arrayPoligonos[i][j].getBounds().x + 1, arrayPoligonos[i][j].getBounds().y + 18, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 1, arrayPoligonos[i][j].getBounds().y + 18, 10, 10);
							break;
						case 1: g.fillOval(arrayPoligonos[i][j].getBounds().x + 5, arrayPoligonos[i][j].getBounds().y + 12, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 5, arrayPoligonos[i][j].getBounds().y + 12, 10, 10);
							break;
						case 2: g.fillOval(arrayPoligonos[i][j].getBounds().x + 19, arrayPoligonos[i][j].getBounds().y + 12, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 19, arrayPoligonos[i][j].getBounds().y + 12, 10, 10);
							break;
						case 3: g.fillOval(arrayPoligonos[i][j].getBounds().x + 30, arrayPoligonos[i][j].getBounds().y + 25, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 30, arrayPoligonos[i][j].getBounds().y + 25, 10, 10);
							break;
					}	
					g.setColor(Color.BLACK);
				}	
				if (arrayPoligonos[i][j].hasFichaNegra()) {
					switch (arrayPoligonos[i][j].getPosicion()) {
						case 0: g.fillOval(arrayPoligonos[i][j].getBounds().x + 1, arrayPoligonos[i][j].getBounds().y + 2, 10, 10);
							break;
						case 1: g.fillOval(arrayPoligonos[i][j].getBounds().x + 20, arrayPoligonos[i][j].getBounds().y + 4, 10, 10);
							break;
						case 2: g.fillOval(arrayPoligonos[i][j].getBounds().x + 4, arrayPoligonos[i][j].getBounds().y + 6, 10, 10);
							break;
						case 3: g.fillOval(arrayPoligonos[i][j].getBounds().x + 10, arrayPoligonos[i][j].getBounds().y + 15, 10, 10);
							break;
					}	
				}
				if (arrayPoligonos[i][j].hasFichaBlanca()) {
					g.setColor(Color.WHITE);
					switch (arrayPoligonos[i][j].getPosicion()) {
						case 0: g.fillOval(arrayPoligonos[i][j].getBounds().x + 1, arrayPoligonos[i][j].getBounds().y + 18, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 1, arrayPoligonos[i][j].getBounds().y + 18, 10, 10);
							break;
						case 1: g.fillOval(arrayPoligonos[i][j].getBounds().x + 5, arrayPoligonos[i][j].getBounds().y + 12, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 5, arrayPoligonos[i][j].getBounds().y + 12, 10, 10);
							break;
						case 2: g.fillOval(arrayPoligonos[i][j].getBounds().x + 19, arrayPoligonos[i][j].getBounds().y + 13, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 19, arrayPoligonos[i][j].getBounds().y + 13, 10, 10);
							break;
						case 3: g.fillOval(arrayPoligonos[i][j].getBounds().x + 30, arrayPoligonos[i][j].getBounds().y + 25, 10, 10);
								g.setColor(Color.BLACK);
								g.drawOval(arrayPoligonos[i][j].getBounds().x + 30, arrayPoligonos[i][j].getBounds().y + 25, 10, 10);
							break;
					}	
					
				}
				g.setColor(Color.BLACK);
			}
		}
		if (central.isPosibilidadDetras() || central.isPosibilidadDelante() || central.hasFichaNegra() || central.hasFichaBlanca()) {
			if (central.isPosibilidadDetras() || central.isPosibilidadDelante()) {
				g.setColor(Color.GRAY);	
				if (jugadorActual == 1) {
					g.fillOval(central.getBounds().x + 20, central.getBounds().y + 10, 10, 10);	
					g.setColor(Color.BLACK);
					g.drawOval(central.getBounds().x + 20, central.getBounds().y + 10, 10, 10);	
				}
				else {
					g.fillOval(central.getBounds().x + 20, central.getBounds().y + 40, 10, 10);	
					g.setColor(Color.BLACK);
					g.drawOval(central.getBounds().x + 20, central.getBounds().y + 40, 10, 10);
				}
			}	
			if (central.hasFichaNegra()) {
				g.setColor(Color.BLACK);
				g.fillOval(central.getBounds().x + 20, central.getBounds().y + 10, 10, 10);	
			}			
			if (central.hasFichaBlanca()) {
				g.setColor(Color.WHITE);
				g.fillOval(central.getBounds().x + 20, central.getBounds().y + 40, 10, 10);
				g.setColor(Color.BLACK);
				g.drawOval(central.getBounds().x + 20, central.getBounds().y + 40, 10, 10);	
			}						
			g.setColor(Color.BLACK);
		}
	}
	
	/**
	 * Método para quitar un quesito aleatorio a un jugador
	 * 
	 * @param jugador Integer que indica cual es el jugador que perderá el quesito
	 *   
	 * @throws StackOverflowError Si por mala suerte el random sale mal demasiadas veces
	*/
	private void setVisibleOno(boolean opcion, boolean quesito) {
		txtPregunta.setVisible(opcion);
		fondoPregunta.setVisible(opcion);
		for (int i = 0; i < fondoRespuesta.length; i++) {
			fondoRespuesta[i].setVisible(opcion);
			txtRespuesta[i].setVisible(opcion);
		}			
		if (quesito) {	
			lblQuesito.setVisible(true);
		}
		else {
			lblQuesito.setVisible(false);
		}
	}
	
	/**
	 * Método para calcular el movimiento de la ficha en una dirección
	 * 
	 * @param posicion0 Integer que indica la primera parte de la posicion de la ficha actual
	 * @param posicion1 Integer que indica la primera parte de la posicion de la ficha actual
	 * @param num Cantidad de casillas que se moverá la ficha
	*/
	private void moverFichaDelante(int posicion0, int posicion1, int num) {		
		if (posicion0 == -1) {			
			central.setPosibilidadDelante(false);
			for (int i = 0; i < arrayPoligonos.length; i++) {
				if (arrayPoligonos[i][0].isQuesito() && i != posicion0) {	
					for (int j = 0; j < arrayPoligonos[i].length; j++) {						
						arrayPoligonos[i][j].setPosibilidadDelante(false);						
					}
					arrayPoligonos[i][arrayPoligonos[i].length - (num)].setPosibilidadDelante(true);
					posicion0 = i;
					posicion1 = arrayPoligonos[i].length - (num);
				}
			}
		}
		else {
			if (num > 0) {						
				for (int i = 0; i < arrayPoligonos.length; i++) {						
					arrayPoligonos[i][0].setPosibilidadDelante(false);						
				}
				if (posicion1 > 0) {	
					for (int j = 0; j < arrayPoligonos[posicion0].length; j++) {						
						arrayPoligonos[posicion0][j].setPosibilidadDelante(false);						
					}
					if (posicion1 == 6) {
						if (num == 1) {							
							central.setPosibilidadDelante(true);							
							arrayPoligonos[posicion0][posicion1].setPosibilidadDelante(false);
							posicion0 = -1;
						}
						else {							
							for (int i = 0; i < arrayPoligonos.length; i++) {								
								if (arrayPoligonos[i][0].isQuesito() && i != posicion0) {									
									for (int j = 0; j < arrayPoligonos[i].length; j++) {						
										arrayPoligonos[i][j].setPosibilidadDelante(false);						
									}
									arrayPoligonos[i][arrayPoligonos[i].length - (num - 1)].setPosibilidadDelante(true);
									//posicion0 = i;
									posicion1 = arrayPoligonos[i].length - (num - 1);
								}
							}							
						}					
					}				
					else {						
						for (int i = 0; i < arrayPoligonos[posicion0].length; i++) {						
							arrayPoligonos[posicion0][i].setPosibilidadDelante(false);						
						}
						arrayPoligonos[posicion0][posicion1 + 1].setPosibilidadDelante(true);							
						posicion1 += 1;
						num--;
						if (num > 0) {					
							this.moverFichaDelante(posicion0, posicion1, num);							
						}
					}
				}
				else {
					if (posicion0 == 41) {
						if (posicion1 == 0) {
							arrayPoligonos[41][num].setPosibilidadDelante(true);
							arrayPoligonos[41][0].setPosibilidadDelante(false);
							arrayPoligonos[num - 1][0].setPosibilidadDelante(true);
						}						
					}
					else {			
						if (arrayPoligonos[posicion0][posicion1].isQuesito()) {						
							arrayPoligonos[posicion0][posicion1 + num].setPosibilidadDelante(true);
						}		
						arrayPoligonos[posicion0 + 1][0].setPosibilidadDelante(true);
						num--;			
						posicion0 += 1;							
						this.moverFichaDelante(posicion0, posicion1, num);						
					}
				}	
			}
		}
	}	
	
	/**
	 * Método para calcular el movimiento de la ficha en la otra dirección
	 * 
	 * @param posicion0 Integer que indica la primera parte de la posicion de la ficha actual
	 * @param posicion1 Integer que indica la primera parte de la posicion de la ficha actual
	 * @param num Cantidad de casillas que se moverá la ficha
	 * @param anteriorCentral Indica si el jugador viene de una de las casillas centrales del tablero
	*/
	private void moverFichaDetras(int posicion0, int posicion1, int num, boolean anteriorCentral) {		
		if (posicion0 == -1) {			
			central.setPosibilidadDetras(false);
			for (int i = 0; i < arrayPoligonos.length; i++) {
				if (arrayPoligonos[i][0].isQuesito() && i != posicion0) {	
					for (int j = 0; j < arrayPoligonos[i].length; j++) {						
						arrayPoligonos[i][j].setPosibilidadDetras(false);					
					}
					arrayPoligonos[i][arrayPoligonos[i].length - (num)].setPosibilidadDetras(true);
					posicion0 = i;
					posicion1 = arrayPoligonos[i].length - (num);
				}
			}
		}
		else {
			if (num > 0) {						
				for (int i = 0; i < arrayPoligonos.length; i++) {						
					arrayPoligonos[i][0].setPosibilidadDetras(false);						
				}
				if (posicion1 > 0) {	
					for (int j = 0; j < arrayPoligonos[posicion0].length; j++) {						
						arrayPoligonos[posicion0][j].setPosibilidadDetras(false);						
					}	
					
					arrayPoligonos[posicion0][posicion1 - 1].setPosibilidadDetras(true);	
					anteriorCentral = false;
					if (posicion1 != 0) {
						anteriorCentral = true;
					}
					posicion1 -= 1;
					num--;
					if (num > 0) {						
						this.moverFichaDetras(posicion0, posicion1, num, anteriorCentral);
					}					
				}
				else {
					if (posicion0 == 0) {						
						arrayPoligonos[41][num - 1].setPosibilidadDetras(true);
						arrayPoligonos[41][0].setPosibilidadDetras(false);
						arrayPoligonos[41 - (num - 1)][0].setPosibilidadDetras(true);												
					}
					else {			
						if (!anteriorCentral) {
							if (arrayPoligonos[posicion0][posicion1].isQuesito()) {						
								arrayPoligonos[posicion0][posicion1 + num].setPosibilidadDetras(true);
							}		
						}	
						else {
							if (posicion0 != 41) {
								arrayPoligonos[posicion0 + num][0].setPosibilidad(true);
							}
							else {
								arrayPoligonos[num - 1][0].setPosibilidad(true);
							}
						}
						arrayPoligonos[posicion0 - 1][0].setPosibilidadDetras(true);
						num--;			
						posicion0 -= 1;								
						this.moverFichaDetras(posicion0, posicion1, num, false);
					}
				}	
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() == 1) {
			
			//clic en el dado
			if (arg0.getSource() == dado) {	
				boolean anteriorCentral = false;
				fondoTrampa.setVisible(false);
				trampa = false;
				tiradaFinal = false;
				aciertosFinales = 0;
				reto = false;
				thread1.suspend();
				dado.setCursor(cursorNormal);
				this.setVisibleOno(false, false);
				int actual[] = new int[2];
				actual[0] = -1;
				for (int i = 0; i < arrayPoligonos.length; i++) {
					for (int j = 0; j < arrayPoligonos[i].length; j++) {
						arrayPoligonos[i][j].setPosibilidadDetras(false);
						arrayPoligonos[i][j].setPosibilidadDelante(false);
						arrayPoligonos[i][j].setPosibilidad(false);
						this.repaint();
						if (jugadorActual == 1) {
							if (arrayPoligonos[i][j].hasFichaNegra()) {
								actual[0] = i;
								actual[1] = j;
							}
						}
						else {
							if (arrayPoligonos[i][j].hasFichaBlanca()) {
								actual[0] = i;
								actual[1] = j;
							}
						}
					}
				}		
				central.setPosibilidadDelante(false);
				central.setPosibilidadDetras(false);
				if (actual[1] != 0) {
					anteriorCentral = true;
				}					
				this.moverFichaDetras(actual[0], actual[1], dado.getValor() + 1, anteriorCentral);			
				this.moverFichaDelante(actual[0], actual[1], dado.getValor() + 1);
				this.repaint();
			}	
			else {
				
				//clic en una de las 4 respuestas
				for (int i = 0; i < txtRespuesta.length; i++) {
					if(arg0.getSource() == txtRespuesta[i]) {
						if (txtRespuesta[i].getText().equals(pregunta.getRespuesta())) {							
							fondoRespuesta[i].setIcon(imgRespuestaCorrecta);
							if (!tiradaFinal && !reto) {
								dado.addMouseListener(this);
								thread1.resume();
								dado.setCursor(cursorMano);
								if (arrayPoligonos[posicionActual[0]][posicionActual[1]].isQuesito()) {
									if (jugadorActual == 1) {
										if (jugador1.getQuesitos()[arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema()]) {
											if (jugador2.getQuesitos()[arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema()]) {
												jugador2.setQuesito(false, arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema());
											}
										}
										else {
											jugador1.setQuesito(true, arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema());
										}
									}
									else {
										if (jugador2.getQuesitos()[arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema()]) {
											if (jugador1.getQuesitos()[arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema()]) {
												jugador1.setQuesito(false, arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema());
											}
										}
										else {
											jugador2.setQuesito(true, arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema());
										}
									}
								}
								this.setQuesitos();
								if (jugadorActual == 1) {
									this.comprobarQuesitos(jugador1);
								}
								else {
									this.comprobarQuesitos(jugador2);
								}
								aciertosFinales = 0;
							}
							else {
								aciertosFinales++;
								if (!reto) {
									if (aciertosFinales == 4) {
										this.removeMouseListener(this);
										this.setVisibleOno(false, false);
										fondoSiguiente.setIcon(imgRespuestaCorrecta);
										txtSiguiente.setText("Nueva partida");
										fondoSiguiente.setVisible(true);
										txtSiguiente.setVisible(true);								
										if (jugadorActual == 1) {
											txtPregunta.setText("¡"+jugador1.getNombre()+" ha ganado la partida!");									
											this.guardarLog(jugador1.getNombre()+" ha ganado a "+jugador2.getNombre());
											if (!jugador1.getNombre().equals("Invitado1")) {
												GestorBD.hacerUpdate(true, jugador1.getNombre());
											}
											if (!jugador2.getNombre().equals("Invitado2")) {
												GestorBD.hacerUpdate(false, jugador2.getNombre());
											}
										}
										else {
											txtPregunta.setText("¡"+jugador2.getNombre()+" ha ganado la partida!");
											this.guardarLog(jugador2.getNombre()+" ha ganado a "+jugador1.getNombre());
											if (!jugador1.getNombre().equals("Invitado1")) {
												GestorBD.hacerUpdate(false, jugador1.getNombre());
											}
											if (!jugador2.getNombre().equals("Invitado2")) {
												GestorBD.hacerUpdate(true, jugador2.getNombre());
											}
										}
										lblWin.setVisible(true);
										txtPregunta.setVisible(true);
										fondoPregunta.setVisible(true);		
										txtAdmin.setVisible(true);
										fondoAdmin.setVisible(true);
									}
								}	
								if (reto) {
									if (temaActual == 6) {
										if (aciertosFinales < 3) {	
											try {
												this.quitarQuesito(jugadorActual);	
											}
											catch (StackOverflowError s) {
												this.guardarLog("StackOverflow");
												JOptionPane.showMessageDialog(this, "Se ha producido un error y se ha guardado en el log");
											}
											dado.addMouseListener(this);
											thread1.resume();
											dado.setCursor(cursorMano);
											aciertosFinales = 0;
											tiradaFinal = false;
											reto = false;
											fondoReto.setVisible(false);
											fondoTrampa.setVisible(false);
											this.cambiarJugador();
											this.repaint();
										}
										else {
											if (aciertosFinales == 6) {
												this.darQuesito(jugadorActual);
												try {
													if (jugadorActual == 1) {
														this.quitarQuesito(2);
													}
													else {
														this.quitarQuesito(1);
													}
												}
												catch (StackOverflowError s) {
													this.guardarLog("StackOverflow");
													JOptionPane.showMessageDialog(this, "Se ha producido un error y se ha guardado en el log");
												}
												dado.addMouseListener(this);
												thread1.resume();
												dado.setCursor(cursorMano);
												aciertosFinales = 0;
												tiradaFinal = false;
												reto = false;
												fondoReto.setVisible(false);	
												fondoTrampa.setVisible(false);
												this.repaint();
											}
											else {
												dado.addMouseListener(this);
												thread1.resume();
												dado.setCursor(cursorMano);
												aciertosFinales = 0;
												tiradaFinal = false;
												reto = false;
												fondoReto.setVisible(false);
												fondoTrampa.setVisible(false);
											}
										}
									}
								}
								else {
									if (!reto && temaActual == 6 && aciertosFinales < 4) {	
										dado.addMouseListener(this);
										thread1.resume();
										dado.setCursor(cursorMano);
										aciertosFinales = 0;
										tiradaFinal = false;
										reto = false;
										fondoReto.setVisible(false);
										fondoTrampa.setVisible(false);
										this.cambiarJugador();
										this.repaint();
									}			
								}
							}
						}
						else {
							fondoRespuesta[i].setIcon(imgRespuestaIncorrecta);
							for (int j = 0; j < fondoRespuesta.length; j++) {
								if (txtRespuesta[j].getText().equals(pregunta.getRespuesta())) {
									fondoRespuesta[j].setIcon(imgRespuestaCorrecta);
								}
							}
							if (!tiradaFinal && !reto) {
								if (trampa) {
									try {
										this.quitarQuesito(jugadorActual);
									}
									catch (StackOverflowError s) {
										this.guardarLog("StackOverflow");
										JOptionPane.showMessageDialog(this, "Se ha producido un error y se ha guardado en el log");
									}
								}
								dado.addMouseListener(this);
								thread1.resume();
								dado.setCursor(cursorMano);
								if (arrayPoligonos[posicionActual[0]][posicionActual[1]].isQuesito()) {
									if (jugadorActual == 1) {
										jugador1.setQuesito(false, arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema());
									}
									else {
										jugador2.setQuesito(false, arrayPoligonos[posicionActual[0]][posicionActual[1]].getTema());
									}
								}
								this.setQuesitos();
								if (jugadorActual == 1) {
									this.comprobarQuesitos(jugador2);
								}
								else {
									this.comprobarQuesitos(jugador1);
								}							
								this.cambiarJugador();
								this.repaint();
								aciertosFinales = 0;							
							}	
							else {
								if (reto) {
									if (temaActual == 6) {
										if (aciertosFinales < 3) {
											try {
												this.quitarQuesito(jugadorActual);	
											}
											catch (StackOverflowError s) {
												this.guardarLog("StackOverflow");
												JOptionPane.showMessageDialog(this, "Se ha producido un error y se ha guardado en el log");
											}
											dado.addMouseListener(this);
											thread1.resume();
											dado.setCursor(cursorMano);
											aciertosFinales = 0;
											tiradaFinal = false;
											reto = false;
											fondoReto.setVisible(false);
											fondoTrampa.setVisible(false);
											this.cambiarJugador();
											this.repaint();
										}
										else {
											if (aciertosFinales == 6) {
												this.darQuesito(jugadorActual);
												try {
													if (jugadorActual == 1) {
														this.quitarQuesito(2);
													}
													else {
														this.quitarQuesito(1);
													}
												}
												catch (StackOverflowError s) {
													this.guardarLog("StackOverflow");
													JOptionPane.showMessageDialog(this, "Se ha producido un error y se ha guardado en el log");
												}
												dado.addMouseListener(this);
												thread1.resume();
												dado.setCursor(cursorMano);
												aciertosFinales = 0;
												tiradaFinal = false;
												reto = false;
												fondoReto.setVisible(false);	
												fondoTrampa.setVisible(false);
											}
											else {
												dado.addMouseListener(this);
												thread1.resume();
												dado.setCursor(cursorMano);
												aciertosFinales = 0;
												tiradaFinal = false;
												reto = false;
												fondoReto.setVisible(false);
												fondoTrampa.setVisible(false);
											}
										}
									}
								}
								else {
									if (!reto && temaActual == 6 && aciertosFinales < 4) {	
										dado.addMouseListener(this);
										thread1.resume();
										dado.setCursor(cursorMano);
										aciertosFinales = 0;
										tiradaFinal = false;
										reto = false;
										fondoReto.setVisible(false);
										fondoTrampa.setVisible(false);
										this.cambiarJugador();
										this.repaint();
									}			
								}
							}
						}
						for (int j = 0; j < txtRespuesta.length; j++) {
							txtRespuesta[j].removeMouseListener(this);
						}
						if (!reto) {
							if (tiradaFinal && temaActual < 6 && aciertosFinales < 4) {
								fondoSiguiente.setVisible(true);
								txtSiguiente.setVisible(true);
							}	
							else {
								reto = false;
								tiradaFinal = false;
							}
						}
						else {
							if (temaActual < 6) {
								fondoSiguiente.setVisible(true);
								txtSiguiente.setVisible(true);
							}
							else {
								reto = false;
								tiradaFinal = false;
							}
						}
					}			
				}
				
				//clic en una de las casillas que son posibilidad de movimiento
				boolean encontrado = false;
				for (int i = 0; i < arrayPoligonos.length; i++) {
					for (int j = 0; j < arrayPoligonos[i].length; j++) {
						if ((arrayPoligonos[i][j].isPosibilidad() || arrayPoligonos[i][j].isPosibilidadDetras() || arrayPoligonos[i][j].isPosibilidadDelante()) && arrayPoligonos[i][j].contains(arg0.getPoint())) {
							for (int e = 0; e < arrayPoligonos.length; e++) {
								for (int h = 0; h < arrayPoligonos[e].length; h++) {								
									if (jugadorActual == 1) {
										central.setFichaNegra(false);
										arrayPoligonos[e][h].setFichaNegra(false);
										arrayPoligonos[e][h].setPosibilidadDelante(false);
										arrayPoligonos[e][h].setPosibilidadDetras(false);
										arrayPoligonos[e][h].setPosibilidad(false);
									}
									else {
										central.setFichaBlanca(false);
										arrayPoligonos[e][h].setFichaBlanca(false);
										arrayPoligonos[e][h].setPosibilidadDelante(false);
										arrayPoligonos[e][h].setPosibilidadDetras(false);
										arrayPoligonos[e][h].setPosibilidad(false); 
									}
									encontrado = true;
									central.setPosibilidadDelante(false);
									central.setPosibilidadDetras(false);
								}
							}	
							try {
								if (jugadorActual == 1) {
									if (arrayPoligonos[i][j].isQuesito() && jugador1.getQuesitos()[arrayPoligonos[i][j].getTema()]) {
										throw new YaTienesElQuesitoException();
									}
									arrayPoligonos[i][j].setFichaNegra(true);
								}
								else {
									if (arrayPoligonos[i][j].isQuesito() && jugador2.getQuesitos()[arrayPoligonos[i][j].getTema()]) {
										throw new YaTienesElQuesitoException();
									}
									arrayPoligonos[i][j].setFichaBlanca(true);
								}	
							}
							catch (YaTienesElQuesitoException y) {
								JOptionPane.showMessageDialog(this, "¡Más te vale acertar!");								
							}
							int posibleTrampa = (int)(Math.random()*10+0);							
							if (arrayPoligonos[i][j].getTema() == 7) {
								reto = true;
								fondoReto.setVisible(true);
							}
							if (posibleTrampa == 7  && !arrayPoligonos[i][j].isQuesito()) {
								trampa = true;
								fondoTrampa.setVisible(true);
							}
							posicionActual[0] = i;
							posicionActual[1] = j;
							this.repaint();
							if (!reto) {
								if (encontrado) {
									pregunta = preguntasRespuestas.getPregunta(arrayPoligonos[i][j].getTema());
								}						
								arrayRespuestas = pregunta.getRespuestas();
								txtPregunta.setText(pregunta.getPregunta());
								for (int e = 0; e < txtRespuesta.length; e++) {
									txtRespuesta[e].setText(arrayRespuestas[e]);
								}				
								if (arrayPoligonos[i][j].isQuesito()) {
									switch (arrayPoligonos[i][j].getTema()) {
										case 0: lblQuesito.setIcon(quesitoAzul);
												break;
										case 1: lblQuesito.setIcon(quesitoNaranja);
												break;
										case 2: lblQuesito.setIcon(quesitoVerde);
												break;
										case 3: lblQuesito.setIcon(quesitoRojo);
												break;
										case 4: lblQuesito.setIcon(quesitoAmarillo);
												break;
										case 5: lblQuesito.setIcon(quesitoRosa);
												break;
									}
									this.setVisibleOno(true, true);
								}
								else {
									this.setVisibleOno(true, false);
								}
								dado.removeMouseListener(this);	
								for (int e = 0; e < txtRespuesta.length; e++) {
									txtRespuesta[e].addMouseListener(this);
								}
								for (int e = 0; e < fondoRespuesta.length; e++) {
									fondoRespuesta[e].setIcon(imgRespuesta);
								}
							}
							else {
								this.hacerPregunta(0);
								temaActual = 1;
							}
						}
					}
				}
				
				//clic en la casilla central, si es posibilidad
				if ((central.isPosibilidadDelante() || central.isPosibilidadDetras()) && central.contains(arg0.getPoint())) {				
					for (int e = 0; e < arrayPoligonos.length; e++) {
						for (int h = 0; h < arrayPoligonos[e].length; h++) {
							if (jugadorActual == 1) {
								arrayPoligonos[e][h].setFichaNegra(false);
							}
							else {
								arrayPoligonos[e][h].setFichaBlanca(false);
							}
							arrayPoligonos[e][h].setPosibilidadDelante(false);
							arrayPoligonos[e][h].setPosibilidadDetras(false);
							arrayPoligonos[e][h].setPosibilidad(false);
						}
					}
					if (jugadorActual == 1) {
						central.setFichaNegra(true);
					}
					else {
						central.setFichaBlanca(true);
					}				
					central.setPosibilidadDelante(false);
					central.setPosibilidadDetras(false);
					if (jugadorActual == 1) {
						if (this.comprobarQuesitos(jugador1)) {
							tiradaFinal = true;
						}
						else {
							tiradaFinal = false;
						}
					}
					else {					
						if (this.comprobarQuesitos(jugador2)) {
							tiradaFinal = true;
						}	
						else {
							tiradaFinal = false;
						}
					}		
					if (!tiradaFinal) {
						this.hacerPregunta((int)(Math.random()*6+0));
					}
					else {
						this.hacerPregunta(0);
						temaActual = 1;
					}
					this.repaint();				
				}
				
				//clic en el boton de jugador1, para iniciar sesión
				if (arg0.getSource() == fondoJugador1) {
					FormAuth form1 = new FormAuth(1);
					form1.setLocationRelativeTo(this);
					form1.setVisible(true);
					form1.setResizable(false);				
				}
				
				//clic en el boton de jugador2, para iniciar sesión
				if (arg0.getSource() == fondoJugador2) {
					FormAuth form1 = new FormAuth(2);
					form1.setLocationRelativeTo(this);
					form1.setVisible(true);
					form1.setResizable(false);				
				}
				if (arg0.getSource() == fondoSiguiente) {
					if (txtSiguiente.getText().equals("Siguiente")) {
						this.hacerPregunta(temaActual);
						temaActual++;
						fondoSiguiente.setVisible(false);
						txtSiguiente.setVisible(false);					
					}
					else {
						this.nuevaPartida();						
					}
				}
				if (arg0.getSource() == fondoComenzar) {
					try {
						fAuth.getFrameAdmin().dispose();
					}
					catch (NullPointerException n) {						
					}
					this.comenzarPartida();
				}
				if (arg0.getSource() == txtAdmin) {
					fAuth = new FormAuth(3);
					fAuth.setLocationRelativeTo(this);
					fAuth.setVisible(true);
					fAuth.setResizable(false);						
				}
			}
		}
	}
	
	public void mouseMoved(MouseEvent arg0) {
		boolean dentro = false;
		for (int i = 0; i < arrayPoligonos.length; i++) {
			for (int j = 0; j < arrayPoligonos[i].length; j++) {
				if (arrayPoligonos[i][j] != null) {
					if (((central.contains(arg0.getPoint()) && (central.isPosibilidadDelante() || central.isPosibilidadDetras()))) || ((arrayPoligonos[i][j].contains(arg0.getPoint()) && (arrayPoligonos[i][j].isPosibilidad() || arrayPoligonos[i][j].isPosibilidadDelante() || arrayPoligonos[i][j].isPosibilidadDetras())))) {
						dentro = true;
					}
				}
			}
		}
		if (dentro) {
			this.setCursor(cursorMano);
		}
		else {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}	
	
	public void windowClosing(WindowEvent e) {
		GestorBD.cerrarRs();
		GestorBD.cerrarConexion();		
	}

	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mouseClicked(MouseEvent arg0) {			
	}	
	public void mouseReleased(MouseEvent arg0) {
	}	
	public void mouseDragged(MouseEvent arg0) {		
	}
	public void windowActivated(WindowEvent e) {
	}
	public void windowClosed(WindowEvent e) {
	}
	public void windowDeactivated(WindowEvent e) {
	}
	public void windowDeiconified(WindowEvent e) {
	}
	public void windowIconified(WindowEvent e) {
	}
	public void windowOpened(WindowEvent e) {		
	}		
	
}