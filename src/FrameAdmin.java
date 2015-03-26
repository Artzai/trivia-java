import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que define la ventana de gestión del administrador
 *
 * @author Artzai San José
 *
 * @version 03/06/2014 v1.0
*/
public class FrameAdmin extends JFrame implements MouseListener, KeyListener {
	
	private ImageIcon imgFondo, imgBoton, imgBotonRojo, imgBotonVerde;
	private JLabel fondoPantalla, fondoPreguntas, fondoJugadores, fondoSalir, txtPreguntas, txtJugadores, txtSalir, fondoAtras, txtAtras, fondoGuardar, txtGuardar, fondoBorrar, txtBorrar, lblPregunta, lblRespuesta, lblIncorrectas;
	private Cursor cursorMano;
	private DefaultTableModel modelo;
	private JTable tabla;
	private JScrollPane jpane;
	private ArrayList<String[]> arrayUsuarios;
	private JTextField txtPregunta, txtRespuesta, txtIncorrectas[];
	private int ventanaActual;
	private JComboBox<String> cmbTemas;

	/**
     * Constructor que define las propiedades de la ventana de administrador
    */
	public FrameAdmin() {
		this.setSize(250, 205);
		this.setTitle("Administrador");
		this.colocarComponentes();	
		ventanaActual = 1;		
	}
	
	/**
     * Método donde se instancian y colocan todos los componentes que contendrá esta ventana
    */
	private void colocarComponentes() {
		cursorMano = new Cursor(Cursor.HAND_CURSOR);
		imgFondo = new ImageIcon("imagenes/fondoAdmin.png");
		imgBoton = new ImageIcon("imagenes/botonRespuesta.png");
		imgBotonRojo = new ImageIcon("imagenes/botonRespuestaIncorrecta.png");
		imgBotonVerde = new ImageIcon("imagenes/botonRespuestaCorrecta.png");
		
		fondoPantalla = new JLabel(imgFondo);
		fondoPantalla.setBounds(0, 0, 400, 205);
		
		txtPreguntas = new JLabel("Añadir preguntas", SwingConstants.CENTER);
		txtPreguntas.setBounds(45, 20, 150, 35);
		txtPreguntas.setCursor(cursorMano);
		txtPreguntas.addMouseListener(this);
		this.add(txtPreguntas);
		
		fondoPreguntas = new JLabel(imgBoton);
		fondoPreguntas.setBounds(45, 20, 150, 35);
		this.add(fondoPreguntas);
		
		txtJugadores = new JLabel("Gestionar usuarios", SwingConstants.CENTER);
		txtJugadores.setBounds(45, 70, 150, 35);
		txtJugadores.setCursor(cursorMano);
		txtJugadores.addMouseListener(this);
		this.add(txtJugadores);
		
		fondoJugadores = new JLabel(imgBoton);
		fondoJugadores.setBounds(45, 70, 150, 35);
		this.add(fondoJugadores);
		
		txtSalir = new JLabel("Salir", SwingConstants.CENTER);
		txtSalir.setBounds(45, 120, 150, 35);
		txtSalir.setCursor(cursorMano);
		txtSalir.addMouseListener(this);
		this.add(txtSalir);
		
		fondoSalir = new JLabel(imgBotonRojo);
		fondoSalir.setBounds(45, 120, 150, 35);
		this.add(fondoSalir);
		
		txtAtras = new JLabel("Atras", SwingConstants.CENTER);
		txtAtras.setBounds(30, 270, 150, 35);
		txtAtras.setCursor(cursorMano);
		txtAtras.addMouseListener(this);
		txtAtras.setVisible(false);
		this.add(txtAtras);
		
		fondoAtras = new JLabel(imgBotonRojo);
		fondoAtras.setBounds(30, 270, 150, 35);
		fondoAtras.setVisible(false);
		this.add(fondoAtras);
		
		txtGuardar = new JLabel("Guardar", SwingConstants.CENTER);
		txtGuardar.setBounds(210, 270, 150, 35);
		txtGuardar.setCursor(cursorMano);
		txtGuardar.addMouseListener(this);
		txtGuardar.setVisible(false);
		this.add(txtGuardar);
		
		fondoGuardar = new JLabel(imgBotonVerde);
		fondoGuardar.setBounds(210, 270, 150, 35);
		fondoGuardar.setVisible(false);
		this.add(fondoGuardar);
		
		txtBorrar = new JLabel("Eliminar usuario", SwingConstants.CENTER);
		txtBorrar.setBounds(120, 215, 150, 35);
		txtBorrar.setCursor(cursorMano);
		txtBorrar.addMouseListener(this);
		txtBorrar.setVisible(false);
		this.add(txtBorrar);
		
		fondoBorrar = new JLabel(imgBoton);
		fondoBorrar.setBounds(120, 215, 150, 35);
		fondoBorrar.setVisible(false);
		this.add(fondoBorrar);
		
		modelo = new DefaultTableModel() {
			
			//hago que la columna del nombre no sea editable
			public boolean isCellEditable(int row, int col) {  
				if (col != 0) {  
					return true;  
				} 
				else {  
		            return false;  
		        }         
		    }  		
		};
		modelo.addColumn("Nombre");
		modelo.addColumn("P. ganadas");	
		modelo.addColumn("P. perdidas");
		tabla = new JTable(modelo);		
		jpane = new JScrollPane(tabla);
		jpane.setBounds(20, 20, 350, 182);	
		jpane.setVisible(false);
		this.add(jpane);
		
		lblPregunta = new JLabel("Pregunta:");
		lblPregunta.setBounds(40, 30, 55, 25);
		lblPregunta.setForeground(Color.WHITE);
		lblPregunta.setVisible(false);
		this.add(lblPregunta);
		
		txtPregunta = new JTextField();		
		txtPregunta.setBounds(120, 30, 230, 25);
		txtPregunta.addKeyListener(this);
		txtPregunta.setForeground(Color.WHITE);
		txtPregunta.setOpaque(false);
		txtPregunta.setVisible(false);
		this.add(txtPregunta);
		
		lblRespuesta = new JLabel("Respuesta:");
		lblRespuesta.setBounds(40, 70, 100, 25);
		lblRespuesta.setForeground(Color.WHITE);
		lblRespuesta.setVisible(false);
		this.add(lblRespuesta);
		
		txtRespuesta = new JTextField();		
		txtRespuesta.setBounds(120, 70, 150, 25);
		txtRespuesta.addKeyListener(this);
		txtRespuesta.setForeground(Color.WHITE);
		txtRespuesta.setOpaque(false);
		txtRespuesta.setVisible(false);
		this.add(txtRespuesta);
		
		lblIncorrectas = new JLabel("Incorrectas");
		lblIncorrectas.setBounds(160, 110, 100, 25);
		lblIncorrectas.setForeground(Color.WHITE);
		lblIncorrectas.setVisible(false);
		this.add(lblIncorrectas);
		
		txtIncorrectas = new JTextField[6];
		int horizontal = 30, vertical = 140;
		for (int i = 0; i < txtIncorrectas.length; i++) {
			txtIncorrectas[i] = new JTextField();		
			txtIncorrectas[i].setBounds(horizontal, vertical, 150, 25);
			txtIncorrectas[i].addKeyListener(this);
			txtIncorrectas[i].setForeground(Color.WHITE);
			txtIncorrectas[i].setOpaque(false);
			txtIncorrectas[i].setVisible(false);
			this.add(txtIncorrectas[i]);
			vertical += 40;
			if (i == 2) {
				horizontal = 210;
				vertical = 140;
			}
		}
		
		String[] temas = {"- Tema -", "Juegos", "Deportes", "Ciencia/Tec.", "Geografía", "Cine", "Música"};
		cmbTemas = new JComboBox<String>(temas);
		cmbTemas.setBounds(250, 105, 100, 25);
		cmbTemas.setForeground(Color.WHITE);
		cmbTemas.setBackground(Color.GRAY);
		cmbTemas.setOpaque(false);
		cmbTemas.setVisible(false);
		this.add(cmbTemas);
		
		
		this.add(fondoPantalla);
	}
	
	/**
	 * Método que recoge todos los usuarios de la base de datos y los vuelca en una JTable
	 */
	public void llenarTabla() {		
		arrayUsuarios = new ArrayList<String[]>();
		GestorBD.hacerSelect("usuarios");
		try {
			while (GestorBD.getRs().next()) {
				String[] arrayString = {GestorBD.getRs().getString("nombre"), GestorBD.getRs().getString("p_ganadas"), GestorBD.getRs().getString("p_perdidas")};
				if (!arrayString[0].equals("ADMINISTRADOR")) {
					arrayUsuarios.add(arrayString);
				}
			}
		}
		catch (SQLException e) {			
			e.printStackTrace();
		}		
		for (String[] s : arrayUsuarios) {
			modelo.addRow(s);
		}	
		if (tabla.getRowCount() > 1) {
			tabla.setRowSelectionInterval(0, 0);
			tabla.setColumnSelectionInterval(0, 0);
		}
		
	}
	
	/**
	 * Método que borra todos los datos de la JTable de usuarios
	 */
	private void borrarDatos() {
		int count = modelo.getRowCount();
		for (int i = count - 1; i >= 0; i--) {
			modelo.removeRow(i);
		}
	}
	
	/**
	 * Método que muestra u oculta la ventana de opciones de administrador
	 * 
	 * @param bool Indica si la ventana se muestra o se oculta
	 */
	private void ventanaOpciones(boolean bool) {
		if (bool) {
			this.setSize(250, 205);
			ventanaActual = 1;
		}
		txtPreguntas.setVisible(bool);
		fondoPreguntas.setVisible(bool);
		txtJugadores.setVisible(bool);
		fondoJugadores.setVisible(bool);
		txtSalir.setVisible(bool);
		fondoSalir.setVisible(bool);
	}
	
	/**
	 * Método que muestra u oculta la ventana de añadir preguntas de administrador
	 * 
	 * @param bool Indica si la ventana se muestra o se oculta
	 */
	private void ventanaPreguntas(boolean bool) {
		if (bool) {
			this.setSize(400, 350);
			ventanaActual = 2;
		}
		txtAtras.setVisible(bool);
		fondoAtras.setVisible(bool);			
		txtGuardar.setVisible(bool);
		fondoGuardar.setVisible(bool);
		lblPregunta.setVisible(bool);
		txtPregunta.setVisible(bool);
		lblRespuesta.setVisible(bool);
		txtRespuesta.setVisible(bool);
		lblIncorrectas.setVisible(bool);
		cmbTemas.setVisible(bool);
		for (int i = 0; i < txtIncorrectas.length; i++) {
			txtIncorrectas[i].setVisible(bool);
			
		}
	}
	
	/**
	 * Método que muestra u oculta la ventana de gestion de usuarios
	 *  
	 * @param bool Indica si la ventana se muestra o se oculta
	 */
	private void ventanaJugadores(boolean bool) {
		if (bool) {
			this.setSize(400, 350);
			this.borrarDatos();
			this.llenarTabla();	
			ventanaActual = 3;
		}
		txtAtras.setVisible(bool);
		fondoAtras.setVisible(bool);		
		jpane.setVisible(bool);
		txtGuardar.setVisible(bool);
		fondoGuardar.setVisible(bool);
		txtBorrar.setVisible(bool);
		fondoBorrar.setVisible(bool);
	}	

	public void mousePressed(MouseEvent arg0) {
		if (arg0.getButton() == 1) {
			if (arg0.getSource() == txtPreguntas) {
				this.ventanaOpciones(false);
				this.ventanaPreguntas(true);
			}
			if (arg0.getSource() == txtJugadores) {
				this.ventanaOpciones(false);
				this.ventanaJugadores(true);
			}
			if (arg0.getSource() == txtSalir) {
				int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres salir?", "Confirmación", JOptionPane.YES_NO_OPTION);
				if (opcion == 0) {
					this.dispose();
				}
			}
			if (arg0.getSource() == txtAtras) {
				int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres volver atras?", "Confirmación", JOptionPane.YES_NO_OPTION);
				if (opcion == 0) {
					this.ventanaJugadores(false);
					this.ventanaPreguntas(false);
					this.ventanaOpciones(true);
				}
			}
			if (arg0.getSource() == txtGuardar) {	
				if (ventanaActual == 3) { 
					int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres guardar los datos?", "Confirmación", JOptionPane.YES_NO_OPTION);
					if (opcion == 0) {
						for (int i = 0; i < modelo.getRowCount(); i++) {
							GestorBD.hacerUpdateCompleta(String.valueOf(modelo.getValueAt(i, 0)), Integer.valueOf(String.valueOf(modelo.getValueAt(i, 1))), Integer.valueOf(String.valueOf(modelo.getValueAt(i, 2))));								
						}
						this.ventanaJugadores(false);
						this.ventanaPreguntas(false);
						this.ventanaOpciones(true);
					}
				}				
				else {
					if (txtPregunta.getText().trim().equals("") || txtRespuesta.getText().trim().equals("") || txtIncorrectas[0].getText().trim().equals("") || txtIncorrectas[1].getText().trim().equals("") || txtIncorrectas[2].getText().trim().equals("") || txtIncorrectas[3].getText().trim().equals("") || txtIncorrectas[4].getText().trim().equals("") || txtIncorrectas[5].getText().trim().equals("")) {
						JOptionPane.showMessageDialog(this, "Rellena todos los campos");
					}
					else {
						if (cmbTemas.getSelectedIndex() == 0) {
							JOptionPane.showMessageDialog(this, "Elige un tema");
						}
						else {
							int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres guardar la pregunta?", "Confirmación", JOptionPane.YES_NO_OPTION);
							if (opcion == 0) {
								GestorBD.hacerInsertPregunta(txtPregunta.getText().trim(), txtRespuesta.getText().trim(), txtIncorrectas[0].getText().trim(), txtIncorrectas[1].getText().trim(), txtIncorrectas[2].getText().trim(), txtIncorrectas[3].getText().trim(), txtIncorrectas[4].getText().trim(), txtIncorrectas[5].getText().trim(), cmbTemas.getSelectedIndex() - 1);
							}
							txtPregunta.setText("");
							txtRespuesta.setText("");
							for (int i = 0; i < txtIncorrectas.length; i++) {
								txtIncorrectas[i].setText("");
							}
							cmbTemas.setSelectedIndex(0);
						}
					}					
				}
			}
			if (arg0.getSource() == txtBorrar) {				
				int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres borrar el usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
				if (opcion == 0) {					
					GestorBD.hacerDelete("delete from usuarios where nombre = '"+modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn())+"'");
					modelo.removeRow(tabla.getSelectedRow());
				}
			}				
		}			
	}
	
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getSource() == txtPregunta) {
			if (txtPregunta.getText().length() == 50) {
				arg0.consume();
			}
		}
		if (arg0.getSource() == txtRespuesta) {
			if (txtRespuesta.getText().length() == 17) {
				arg0.consume();
			}
		}
		for (int i = 0; i < txtIncorrectas.length; i++) {
			if (arg0.getSource() == txtIncorrectas[i]) {
				if (txtIncorrectas[i].getText().length() == 17) {
					arg0.consume();
				}
			}
		}		
	}
	
	public void mouseClicked(MouseEvent arg0) {		
	}
	public void mouseEntered(MouseEvent arg0) {	
	}
	public void mouseExited(MouseEvent arg0) {		
	}
	public void mouseReleased(MouseEvent arg0) {	
	}	
	public void keyPressed(KeyEvent arg0) {		
	}
	public void keyReleased(KeyEvent arg0) {	
	}	
	
}