
package py.edu.ucsa.dao.blobs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import py.edu.ucsa.jdbc01.conexion.ConexionBD;

/**
 * Clase que maneja el tipo de dato que maneja im�genes
 * @author Pablo
 *
 */
public class Blobs extends JFrame{

	private static final long serialVersionUID = 7385902858300475916L;

	private File foto; 
	private JScrollPane sp;
	private Connection con;
	private JTextField textField;

	class FileChooser implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser();
			int valor = filechooser.showOpenDialog(Blobs.this);
			if (valor == JFileChooser.APPROVE_OPTION) {
				foto = filechooser.getSelectedFile();
				System.out.println(foto.getAbsolutePath());
				JLabel label = new JLabel(new ImageIcon(foto.getAbsolutePath()));
				sp.setViewportView(label);
				pack();
			}

		}
	}

	class ButtonGuardar implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			String val = textField.getText();
			int cedula = Integer.parseInt(val);			
			insertBlob(con, cedula);
		}
	}


	/**
	 * Constructor sin argumentos 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Blobs() throws SQLException, ClassNotFoundException {


		con = ConexionBD.getConexion();

		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();

		JButton b = new JButton("Guardar");
		toolBar.add(b);
		b.addActionListener(new ButtonGuardar());//usamos una clase interna que maneje los eventos

		b = new JButton("Recuperar");
		toolBar.add(b);

		//Creamos un anonymous inner class que maneje los eventos
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String val = textField.getText();
				int legajo = Integer.parseInt(val);
				byte [] image =getBlob(con, legajo);
				JLabel label = new JLabel(new ImageIcon(image));
				sp.setViewportView(label);
				pack();
			}
		});

		textField = new JTextField(5);
		toolBar.add(textField);

		panel.add(toolBar,BorderLayout.NORTH);
		sp = new JScrollPane();
		panel.add(new JScrollPane(sp),BorderLayout.CENTER);
		getContentPane().add(panel);


		JMenuBar menuBar=new JMenuBar();
		JMenu menuArchivo=new JMenu("Archivo");
		JMenuItem mi = new JMenuItem("Abrir");
		mi.addActionListener(new FileChooser());
		menuArchivo.add(mi);
		menuBar.add(menuArchivo);

		setJMenuBar(menuBar);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

	}

	public void insertBlob(Connection con, int pk) {

		try {
			FileInputStream fis = new FileInputStream(foto);
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO album_fotos (id, foto) VALUES (?, ?)");
			ps.setInt(1, pk);
			ps.setBinaryStream(2, fis, (int) foto.length());
			ps.executeUpdate();
			ps.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public byte [] getBlob(Connection con, int pk) {
		byte[] imgBytes = null;
		try {
			PreparedStatement ps = con.prepareStatement("SELECT foto FROM album_fotos WHERE id = ?");
			ps.setInt(1, pk);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					imgBytes = rs.getBytes(1);
				}
				rs.close();
			}
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imgBytes;
	}


	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
	
			Blobs b = new Blobs ();
			b.setSize(600, 400);
			b.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
