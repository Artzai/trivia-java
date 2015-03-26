import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

/**
 * Clase que define la ventana de inicio de sesión.
 *
 * @author Artzai San José
 *
 * @version 03/06/2014 v1.0
*/
public class FormAuth extends JFrame implements MouseListener, KeyListener {
	
	private JLabel labelUser, labelPass, fondoAcceder, fondoSalir, txtAcceder, txtSalir, fondoPantalla;
	private JTextField txtUser;
	private JPasswordField txtPass;	
	private String usuario, pass;
	private int user;
	private JCheckBox chkNuevo;
	private ImageIcon imgBoton, imgFondo;
	private Cursor cursorMano;
	private FrameAdmin fAdmin;
	
	/**
     * Constructor que define las propiedades de la ventana de inicio de sesión y recoge todos los usuarios de la base de datos
     * 
     * @param use Indica si inicia sesiñon como jugador1 o jugador2
    */
	public FormAuth(int use) {
		this.setSize(400, 210);		
		this.setLayout(null);
		this.setTitle("Inicio de sesión");
		user = use;
		this.colocarComponentes();	
		GestorBD.hacerSelect("usuarios");		
	}	
	
	/**
     * Método donde se instancian y colocan todos los componentes que contendrá esta ventana
    */
	private void colocarComponentes() {
		cursorMano = new Cursor(Cursor.HAND_CURSOR);
		imgBoton = new ImageIcon("imagenes/botonRespuesta.png");
		imgFondo = new ImageIcon("imagenes/fondoAuth.png");
		
		fondoPantalla = new JLabel(imgFondo);
		fondoPantalla.setBounds(0, 0, 400, 205);
		
		labelUser = new JLabel("Nombre de usuario:");
		labelUser.setBounds(70, 20, 150, 25);
		labelUser.setForeground(Color.WHITE);
		this.add(labelUser);
		txtUser = new JTextField("");
		txtUser.setBounds(200, 20, 120, 25);
		txtUser.setForeground(Color.WHITE);
		txtUser.setOpaque(false);
		txtUser.setCaretColor(Color.WHITE);
		this.add(txtUser);
		if (user == 3) {
			txtUser.setText("Administrador");
			txtUser.setEditable(false);
		}
		
		labelPass = new JLabel("Contraseña:");
		labelPass.setBounds(70, 60, 150, 25);
		labelPass.setForeground(Color.WHITE);
		this.add(labelPass);
		txtPass = new JPasswordField("");
		txtPass.setBounds(200, 60, 120, 25);
		txtPass.setForeground(Color.WHITE);
		txtPass.setOpaque(false);
		txtPass.setCaretColor(Color.WHITE);
		txtPass.addKeyListener(this);		
		this.add(txtPass);
		
		chkNuevo = new JCheckBox("  Nuevo usuario");
		chkNuevo.setBounds(120, 95, 150, 25);
		chkNuevo.setForeground(Color.WHITE);
		chkNuevo.setFocusPainted(false);
		chkNuevo.setOpaque(false);
		if (user != 3) {
			this.add(chkNuevo);
		}
		
		txtAcceder = new JLabel("Acceder",SwingConstants.CENTER);
		txtAcceder.setBounds(30, 135, 150, 35);			
		txtAcceder.setCursor(cursorMano);
		this.add(txtAcceder);
		
		fondoAcceder = new JLabel(imgBoton);		
		fondoAcceder.setBounds(30, 135, 150, 35);		
		fondoAcceder.addMouseListener(this);
		this.add(fondoAcceder);		
		
		txtSalir = new JLabel("Cancelar",SwingConstants.CENTER);
		txtSalir.setBounds(200, 135, 150, 35);			
		txtSalir.setCursor(cursorMano);
		this.add(txtSalir);
		
		fondoSalir = new JLabel(imgBoton);
		fondoSalir.setBounds(200, 135, 150, 35);		
		fondoSalir.addMouseListener(this);
		this.add(fondoSalir);	
		
		this.add(fondoPantalla);		
	}
	
	//GETTERS & SETTERS
	public String getUsuario() {
		return usuario;
	}	
	public FrameAdmin getFrameAdmin() {
		return fAdmin;
	}		

	public void mousePressed(MouseEvent ae) {
		if (ae.getSource() == fondoAcceder) {
			if (user != 3) {
				usuario = txtUser.getText().trim().toUpperCase();
				pass = txtPass.getText().trim();
				if (usuario.equals("") || pass.equals("")) {
					JOptionPane.showMessageDialog(this, "Introduce usuario y contraseña", "Información", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					if (usuario.equals("ADMINISTRADOR")) {
						JOptionPane.showMessageDialog(this, "No se puede jugar como administrador.");
					}
					else {					
						if (!chkNuevo.isSelected()) {
							boolean login = false;
							boolean yaLogin = false;
							if (usuario.equals(FramePrincipal.getJugador(1).getNombre()) || usuario.equals(FramePrincipal.getJugador(2).getNombre())) {
								yaLogin = true;
							}
							if (!yaLogin) {
								for (String[] s : FramePrincipal.getUsuarios()) {
									if (s[0].equals(usuario)) {
										try {
											if (s[1].equals(MD5.encriptar(pass))) {
												FramePrincipal.setUsuario(user, usuario);
												this.dispose();
												login = true;
											}
										}
										catch (Exception e) {							
											e.printStackTrace();
										}
									}
								}
								if (!login) {
									JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
								}
							}
							else {
								JOptionPane.showMessageDialog(this, "Usuario ya conectado");
							}
						}
						else {
							boolean repetido = false;
							boolean yaLogin = false;
							for (String[] s : FramePrincipal.getUsuarios()) {
								if (s[0].equals(usuario)) {
									JOptionPane.showMessageDialog(this, "Ya existe ese usuario");
									repetido = true;
								}
							}
							if (usuario.equals(FramePrincipal.getJugador(1).getNombre()) || usuario.equals(FramePrincipal.getJugador(2).getNombre())) {
								yaLogin = true;
							}
							if (!repetido) {
								if (yaLogin) {
									JOptionPane.showMessageDialog(this, "Ya hay un usuario conectado con ese nombre");
								}
								else {							
									try {
										String[] us = {usuario, pass};
										FramePrincipal.getUsuarios().add(us);
										GestorBD.hacerInsertUsuario(usuario, MD5.encriptar(pass));							
										FramePrincipal.setUsuario(user, usuario);
										this.dispose();
									}
									catch (Exception e) {							
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}	
			else {
				pass = txtPass.getText().trim();
				if (pass.equals("")) {
					JOptionPane.showMessageDialog(this, "Introduce la contraseña", "Información", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					GestorBD.hacerSelectConcreta("select contrasena from usuarios where nombre = 'ADMINISTRADOR'");
					try {
						if (GestorBD.getRs().next()) {							
							if (MD5.encriptar(pass).equals(GestorBD.getRs().getString("contrasena"))) {
								fAdmin = new FrameAdmin();
								fAdmin.setLocation(this.getX()+510, this.getY() - 307);
								fAdmin.setResizable(false);
								fAdmin.setVisible(true);								
								this.dispose();
							}
						}
					}
					catch (SQLException e) {						
						e.printStackTrace();
					}
					catch (Exception e) {					
						e.printStackTrace();
					}
				}
			}
		}
		if (ae.getSource() == fondoSalir) {
			this.dispose();
		}		
	}
	
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar() == ' ') {
			arg0.consume();
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