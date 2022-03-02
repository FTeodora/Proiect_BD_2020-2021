
package view.utility;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import model.entity.Profesor;
import model.entity.Student;
import model.enumeration.UserRole;

import java.sql.*;
/**
 * O clasa de caseta care retine diverse informatii despre entitati, 
 * fie pentru vizualizarea, editarea sau stergerea lor, fie pentru
 * diferite actiuni ce au legatura cu acele entitati (ex: inscriere la curs,grup etc.)
 *
 * Pentru operatiile de editare si delete se folosesc functii generale de update si delete,
 * alaturi de o lista de zone editabile denumite EXACT dupa numele atributelor din MySql
 * De asemenea, este nevoie de o conexiune la server pentru executarea lor, alaturi de numele unui tabel
 * pe care se executa operatia
 * 
 * Actiunile suplimentare sunt determinate de un parametru care retine rangul persoanei care
 * vede caseta sau eventual de alti parametri in plus din clasele mostenitoare
 * si sunt primite separat dintr-o lista de actiuni a unui panou de casete in care aceasta se afla
 * 
 * Caseta in sine este de tip CardLayout, avand in plus un panou de editare 
 * unde se vor avea campurile pentru introducerea datelor
 * 
 * JPanel-ul de display unde se retin informatiile in sine este formatat
 * la BorderLayout, avand adaugate direct JPanel-uri pentru fiecare zona
 * pentru posibilitatea afisarii mai multor informatii in aceeasi zona
 *<ul>
 *<li>header - pentru BorderLayout.NORTH
 *<li>left - pentru BorderLayout.WEST
 *<li>center - pentru BorderLayout.CENTER
 *<li>options - pentru BorderLayout.EAST. Rezervata pentru butoane de actiuni
 *<li>buttons - pentru BorderLayout.SOUTH. Rezervata butoanelor de edit/delete
 *</ul>
 */
public class Casette extends JPanel{
	protected JPanel display;
	protected ArrayList<JLabel> infos;
	protected JPanel header,left,center,options,buttons;
	protected UserRole whoSearched;
	
	protected Connection connection;
	protected JPanel editpane;
	protected ArrayList<Field> editableDatas;
	protected String tabel;
	protected InsertionButton edit,delete,submit;
	/**
	 * Constructorul care initalizeaza caseta, fara a o face editabila
	 */
	Casette(){
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLineBorder(AppWindow.headerColor)));
		this.setPreferredSize(new Dimension(700,200));
		this.setMaximumSize(new Dimension(700,200));
		display=new JPanel();
		display.setLayout(new BorderLayout());
		display.setBorder(new EmptyBorder(20, 20, 20, 20));
		display.setPreferredSize(this.getSize());
		
		infos=new ArrayList<JLabel>();
		
		header=new JPanel();
		display.add(header,BorderLayout.NORTH);
		
		left=new JPanel();
		display.add(left,BorderLayout.WEST);
		
		center=new JPanel();
		display.add(center,BorderLayout.CENTER);
		
		options=new JPanel();
		options.setBorder(new EmptyBorder(0,0,0,0));
		display.add(options,BorderLayout.EAST);
		
		buttons=new JPanel();
		buttons.setBorder(new EmptyBorder(0,0,0,0));
		display.add(buttons,BorderLayout.SOUTH);
		
		this.setLayout(new CardLayout());
		this.add(display,"main");
	}

	/**
	 * Face caseta editabila, adaugand actiuni pentru edit si delete, dar nu adauga 
	 * campuri pe panoul editabil sau butoanele de edit si delete in sine
	 * @param editableFields campurile care se pot edita, avand pe prima pozitie. Mai mult pentru cand se face din afara clasei
	 * atributul identificator needitabil (avand un JLabel la zona de informatie)
	 */
	public void makeEditableCasette(ArrayList<Field> editableFields){
		
		editableDatas=editableFields;
		
		this.editpane=new JPanel();
		this.editpane.setLayout(new BorderLayout());
		this.editpane.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		Casette t=this;
		edit=new InsertionButton("Editeaza");
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl=(CardLayout)t.getLayout();
				cl.last(t);
			}
		});
		
		
		
		submit=new InsertionButton("Confirma");
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(updateDatas())
					{
					JOptionPane.showMessageDialog(t, "Actualizarea s-a realizat cu succes", "Succes",JOptionPane.INFORMATION_MESSAGE);
					changeDatas();
					CardLayout cl=(CardLayout)t.getLayout();
					cl.first(t);
					}
			}
		});
		
		JButton cancel=new JButton("Anulare");
		cancel.setFont(AppWindow.SMALLER_FONT);
		cancel.setBorder(new LineBorder(Color.WHITE));
		cancel.setBackground(AppWindow.headerColor);
		cancel.setForeground(Color.WHITE);
		cancel.setPreferredSize(new Dimension(80,30));
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl=(CardLayout)t.getLayout();
				cl.first(t);
			}
		});
		
		delete=new InsertionButton("Sterge");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(deleteEntity())
				{
				JOptionPane.showMessageDialog(t, "Stergerea s-a realizat cu succes!. Reimprospatati pagina pentru actualizare", "Succes",JOptionPane.INFORMATION_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(null, "Nu s-a putut sterge entitatea dorita", "Eroare",JOptionPane.ERROR_MESSAGE);
			}
		});
		JPanel editbtns=new JPanel();
		editbtns.setBackground(AppWindow.mainColor);
		editbtns.add(submit);
		editbtns.add(cancel);
		editpane.add(editbtns,BorderLayout.SOUTH);
		this.add(editpane,"edit");
		buttons.setBorder(new EmptyBorder(0,0,0,0));
		buttons.setPreferredSize(new Dimension(500,35));
		
	}
	/**
	 * Formateaza lista de valori editabile dupa sintaxa de valori de actualizat in MySQL
	 * [UPDATE tabel SET] coloana1='valoare1', coloana2='valoare2' etc.
	 * Metoda e statica pentru a se apela in mai multe situatii
	 * @param editableDatas datele editabile dupa care se obtin rezultatele
	 * @return rezultatul formatat intr-un singur string
	 */
	public static String toUpdateValues(ArrayList<Field> editableDatas) {
		String rez="";
		for(Field i:editableDatas) {
			if(!(i.getInfoField() instanceof JLabel))
			{
				if(!i.getInfo().replace(":","").trim().equals(""))
					rez+=i.getName()+"='"+i.getInfo()+"',";
			}
		}
		if(rez.endsWith(","))
			rez=rez.substring(0, rez.length()-1);
		return rez;
	}
	/**
	 * Funcie generala de UPDATE dupa sintaxa
	 * UPDATE tabel 
	 * SET coloana1='valoare1', coloana2='valoare2' ... 
	 * WHERE coloana_de_identificare=valoare_de_identificare
	 * @return o valoare booleana care reprezinta daca editarea s-a realizat cu succes
	 */
	public boolean updateDatas()
	{
		boolean succes=true;
		try {
			Statement st=connection.createStatement();
			System.out.println("UPDATE "+tabel+" SET "+Casette.toUpdateValues(editableDatas)+" WHERE "+editableDatas.get(0).getName()+"="+edit.getValue());
			st.execute("UPDATE "+tabel+" SET "+Casette.toUpdateValues(editableDatas)+" WHERE "+editableDatas.get(0).getName()+"="+edit.getValue());
			
		}catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Nu s-au putut actualiza datele", "Eroare",JOptionPane.ERROR_MESSAGE);
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}
	/**
	 * Sterge dintr-un tabel dupa o cheie de identificare
	 * Foloseste sintaxa generala 
	 * DELETE FROM tabel WHERE coloana_identificare=valoare_identificare
	 * @return o valoare booleana care reprezinta daca stergerea s-a realizat cu succes
	 */
	public boolean deleteEntity() {
		boolean succes=true;
		try {
			Statement st=connection.createStatement();
			System.out.println("DELETE FROM "+tabel+" WHERE "+editableDatas.get(0).getName()+"="+delete.getValue());
			st.execute("DELETE FROM "+tabel+" WHERE "+editableDatas.get(0).getName()+"="+delete.getValue());
			
		}catch(SQLException e) {
			
			succes=false;
			e.printStackTrace();
		}
		return succes;
	}
	public void setSource(Field f) {
		if(edit!=null)
			edit.setSource(f);
		if(delete!=null)
			delete.setSource(f);
	}
	
	public void changeDatas() {
		
	}
	public void resetOptions() {
		this.options.removeAll();
	}
	public void setConnection(Connection c){
		this.connection=c;
	}
	/**
	 * Adauga optiuni de butoanele casetelor in functie de situatiile determinate de cine vede sau alte variabile auxiliare
	 * @param source	campul sursa unde se afla caseta
	 * @param actions	actiunile(ActionListener-uri) posibile ce le poate avea caseta
	 */
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		for(Component i:this.getComponents())
		{
			i.setBackground(bg);
			if(i instanceof JPanel)
				for(Component j:((JPanel) i).getComponents())
					j.setBackground(bg);
		}
		
		
	}
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		for(Component i:this.getComponents())
		{
			i.setForeground(fg);	
			if(i instanceof JPanel)
				for(Component j:((JPanel) i).getComponents())
					j.setForeground(fg);
		}

	}
	@Override
	public void setFont(Font font) {
	//	super.setFont(font);
		for(Component i:this.getComponents())
			i.setFont(font);
	}
	public void colorLabels(Color c,Font f) {
		for(JLabel i:infos)
		{
			i.setForeground(c);
			i.setFont(f);
		}
	}
	public void colorFields(Color c,Font f) {
		for(Field i:this.editableDatas) {
			i.setForeground(c);
			i.getNameLabel().setFont(f);
			i.getInfoField().setFont(f);
		}
	}
	public String getInfo(int n) {
		return this.infos.get(n).getText();
	}
}
