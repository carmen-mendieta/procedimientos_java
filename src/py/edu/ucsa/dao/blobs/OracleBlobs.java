
package py.edu.ucsa.dao.blobs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
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


public class OracleBlobs extends JFrame{

	private static final long serialVersionUID = 1L;
	File foto; 
	JScrollPane sp;
	Connection con;
	JTextField textField;
	
	class FileChooser implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser();
			int valor = filechooser.showOpenDialog(OracleBlobs.this);
			if (valor == JFileChooser.APPROVE_OPTION) {
				foto = filechooser.getSelectedFile();
				System.out.println(foto.getAbsolutePath());
				JLabel label = new JLabel(new ImageIcon(foto.getAbsolutePath()));
				sp.setViewportView(label);
				pack();
			}

		}
	}
	
	class ButtonGuardarAction implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
    		String val = textField.getText();	
			insertBlob(con, val);
		}
	}
	
	
	class ButtonLimpiarAction implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
    		textField.setText("");
			JLabel  label = new JLabel("NO HAY FOTO");
			sp.setViewportView(label);
			pack();			
		}
	}
	
	/**
	 * @throws ClassNotFoundException 
	 * 
	 */
	public OracleBlobs () throws SQLException, ClassNotFoundException {
		
	
		con = ConexionBD.getConexionORCL();
		        
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();

        JButton b = new JButton("Guardar");
        toolBar.add(b);
        b.addActionListener(new ButtonGuardarAction());//usamos una clase interna que maneje los eventos
             
        b = new JButton("Limpiar");
        toolBar.add(b);
        b.addActionListener(new ButtonLimpiarAction());
        
        
        b = new JButton("Recuperar");
        toolBar.add(b);        
        //Creamos un anonymous inner class que maneje los eventos
        b.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0)  {
        		String nombre = textField.getText();

        		Blob image =getBlob(con, nombre);
				JLabel label;
				try {
					if (image == null)
						label = new JLabel("NO HAY FOTO");
					else
						label = new JLabel(new ImageIcon(image.getBytes(1, (int) image.length())));
					sp.setViewportView(label);
					pack();

				} catch (SQLException e) {
					e.printStackTrace();
				}
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
	
	public void insertBlob(Connection con,  String nombre) {
		
		try {
			FileInputStream fis = new FileInputStream(foto);
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO album_fotos (id, foto, nombre) VALUES (SEQ_FOTOS.NEXTVAL,  ? , ? )");
				
			ps.setBlob(1, fis);
			
			ps.setString(2, nombre);
			ps.executeUpdate();
			ps.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Blob getBlob(Connection con, String nombre) {
		Blob img = null;
		try {
			
			PreparedStatement ps = con.prepareStatement("SELECT foto FROM album_fotos WHERE nombre like ?");
			nombre = nombre + "%";
			ps.setString(1, nombre);
			
			ResultSet rs =  ps.executeQuery(); 
			if (rs.next()) {
				img = rs.getBlob("foto");
			}
			rs.close();			
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			OracleBlobs b = new OracleBlobs ();
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
