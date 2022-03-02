package view.utility;
import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;
import repository.impl.JDBConnectionWrapper;
import service.AdminService;
import service.UserService;
import service.impl.UserServiceImpl;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PersonalInfo extends Casette{
	private final User user;
	

	public PersonalInfo(User u){
		super();
		this.setPreferredSize(new Dimension(700,400));
		this.setMaximumSize(new Dimension(700,400));
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		user=u;
		infos.add(new JLabel("ID: "+u.getId())); //0
		infos.add(new JLabel("Username: "+u.getUsername())); //1
		infos.add(new JLabel("Parola: "+u.getUsername())); //2
		infos.add(new JLabel("CNP: "+u.getCNP())); //3
		infos.add(new JLabel("Nume: "+u.getNume())); //4
		infos.add(new JLabel("Prenume: "+u.getPrenume())); //5
		infos.add(new JLabel("Adresa: "+u.getAdresa())); //6
		infos.add(new JLabel("Telefon: "+u.getTelefon())); //7
		infos.add(new JLabel("e-mail: "+u.getEmail())); //8
		infos.add(new JLabel("Numar contract: "+u.getNrContract())); //9
		infos.add(new JLabel("IBAN: "+u.getIBAN())); //10
		infos.add(new JLabel("Rol: "+u.getTipUser().toString())); //11
		if(u instanceof Student) {
			infos.add(new JLabel("An: "+((Student)u).getAn())); //12
			infos.add(new JLabel("Numar minim ore: "+((Student)u).getNrOre())); //13
		}
		if(u instanceof Profesor)
		{
			infos.add(new JLabel("Departament: "+((Profesor)u).getDepartament())); //12 
			infos.add(new JLabel("Numar minim ore: "+((Profesor)u).getMinOre())); //13
			infos.add(new JLabel("Numar maxim ore: "+((Profesor)u).getMaxOre())); //14
		}
		for(JLabel i: infos)
		{
			i.setBorder(new EmptyBorder(2,0,2,0));
			i.setFont(AppWindow.MAIN_FONT);
			i.setAlignmentX(JComponent.LEFT_ALIGNMENT);
			i.setForeground(Color.WHITE);
			this.left.add(i);
		}
		
		this.tabel="users";
		this.editableDatas=new ArrayList<Field>();
		editableDatas.add(new Field("ID: ",new JLabel(Integer.toString(user.getId()))));
		editableDatas.add(new Field("Username: ",new JTextField(user.getUsername())));
		editableDatas.add(new Field("Parola: ",new JTextField(user.getParola())));
		editableDatas.add(new Field("CNP: ",new JLabel(user.getCNP())));
		editableDatas.add(new Field("Nume: ",new JTextField(user.getNume())));
		editableDatas.add(new Field("Prenume: ",new JTextField(user.getPrenume())));
		editableDatas.add(new Field("Adresa: ",new JTextField(user.getAdresa())));
		editableDatas.add(new Field("Telefon: ",new JTextField(user.getTelefon())));
		editableDatas.add(new Field("e-mail: ",new JTextField(user.getEmail())));
		editableDatas.add(new Field("Numar contract: ",new JTextField(u.getNrContract())));
		editableDatas.add(new Field("IBAN: ",new JTextField(user.getIBAN())));
		editableDatas.add(new Field("Rol: ",new JLabel(user.getTipUser().toString())));
		if(u instanceof Student) {
			editableDatas.add(new Field("An: ",new JComboBox<String>(AppWindow.ani)));
			editableDatas.add(new Field("Nr ore: ",new JSpinner(new SpinnerNumberModel(((Student)u).getNrOre(),1,90,1))));
		}
		if(u instanceof Profesor)
		{
			editableDatas.add(new Field("Departament: ",new JComboBox<String>(AppWindow.departamente)));
			editableDatas.add(new Field("Numar minim ore: ",new JSpinner(new SpinnerNumberModel(((Profesor)u).getMinOre(),1,90,1))));
			editableDatas.add(new Field("Numar maxim ore: ",new JSpinner(new SpinnerNumberModel(((Profesor)u).getMaxOre(),1,90,1))));
		}
		this.makeEditableCasette(editableDatas);
		ScrollablePanel aux=new ScrollablePanel(editableDatas);
		
		aux.getContent().setBorder(new EmptyBorder(0,0,0,0));
		aux.setLabelFont(AppWindow.MAIN_FONT);
		aux.setFieldFont(AppWindow.MAIN_FONT);
		aux.setBg(AppWindow.mainColor);
		aux.setFg(Color.WHITE);
		aux.unifyFields();
		this.editpane.add(aux);
		JPanel btns=new JPanel();
		btns.setBackground(AppWindow.mainColor);
		edit.setValue(user.getId());
		submit.removeActionListener(submit.getActionListeners()[0]);
		submit.setValue(user.getId());
		options.add(edit);
		delete.removeActionListener(delete.getActionListeners()[0]);
		delete.setValue(user.getId());
		options.add(delete);
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		
	}
	public static ArrayList<Casette> toWindows(ArrayList<User> source){
		ArrayList<Casette> rez=null;
		if(source!=null)
		{
			rez=new ArrayList<Casette>();
			for(User i:source)
				rez.add(new PersonalInfo(i));
		}
		return rez;
	}
	@Override
	public void changeDatas() {
		ScrollablePanel source=(ScrollablePanel)this.editpane.getComponent(1);
		if(!source.getField(1).getInfo().trim().equals("")) 
			this.user.setUsername(this.editableDatas.get(1).getInfo());
		if(!source.getField(2).getInfo().trim().equals(""))
			this.user.setParola(this.editableDatas.get(2).getInfo());
		if(!source.getField(4).getInfo().trim().equals(""))
			this.user.setNume(this.editableDatas.get(4).getInfo());
		if(!source.getField(5).getInfo().trim().equals(""))
			this.user.setPrenume(this.editableDatas.get(5).getInfo());
		if(!source.getField(6).getInfo().trim().equals(""))
			this.user.setAdresa(this.editableDatas.get(6).getInfo());
		if(!source.getField(7).getInfo().trim().equals(""))
			this.user.setTelefon(this.editableDatas.get(7).getInfo());
		if(!source.getField(8).getInfo().trim().equals(""))
			this.user.setEmail(this.editableDatas.get(8).getInfo());
		if(!source.getField(9).getInfo().trim().equals(""))
			this.user.setNrContract(this.editableDatas.get(9).getInfo());
		if(!source.getField(10).getInfo().trim().equals(""))
			this.user.setIBAN(this.editableDatas.get(10).getInfo());
		if(user instanceof Student) {
			((Student)this.user).setAn(Integer.parseInt(this.editableDatas.get(12).getInfo()));
			((Student)this.user).setNrOre(Integer.parseInt(this.editableDatas.get(13).getInfo()));
		}
		if(user instanceof Profesor)
		{
			((Profesor)this.user).setDepartament(this.editableDatas.get(12).getInfo());
			((Profesor)this.user).setMinOre(Integer.parseInt(this.editableDatas.get(13).getInfo()));
			((Profesor)this.user).setMaxOre(Integer.parseInt(this.editableDatas.get(14).getInfo()));
		}
		infos.get(1).setText("Username: "+user.getUsername());
		infos.get(2).setText("Parola: "+user.getParola());
		infos.get(4).setText("Nume: "+user.getNume());
		infos.get(5).setText("Prenume: "+user.getPrenume());
		infos.get(6).setText("Adresa: "+user.getAdresa());
		infos.get(7).setText("Telefon: "+user.getTelefon());
		infos.get(8).setText("email: "+user.getEmail());
		infos.get(9).setText("Numar contract: "+user.getNrContract());
		infos.get(10).setText("IBAN: "+user.getIBAN());
		if(user instanceof Student) {
			infos.get(12).setText("An: "+((Student)user).getAn());
			infos.get(13).setText("Numar minim ore: "+((Student)user).getNrOre());
		}
		if(user instanceof Profesor)
		{
			infos.get(12).setText("Departament: "+((Profesor)user).getDepartament());
			infos.get(13).setText("Numar minim ore: "+((Profesor)user).getMinOre());
			infos.get(14).setText("Numar minim ore: "+((Profesor)user).getMaxOre());
		}
		this.revalidate();
		this.repaint();
	}
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
		submit.setSource(source);
		submit.addActionListener(actions.get(0));
		delete.setSource(source);
		delete.addActionListener(actions.get(1));
	}
	public User getUser() {
		return user;
	}
	public User retrieveInfo() {
		User u;
		ScrollablePanel source=(ScrollablePanel)this.editpane.getComponent(1);
		if(user instanceof Student) {
			u=new Student();
			((Student)u).setAn(Integer.parseInt(this.editableDatas.get(12).getInfo()));
			((Student)u).setNrOre(Integer.parseInt(this.editableDatas.get(13).getInfo()));
		}
		else if(user instanceof Profesor)
		{
			u=new Profesor();
			((Profesor)u).setDepartament(this.editableDatas.get(12).getInfo());
			((Profesor)u).setMinOre(Integer.parseInt(this.editableDatas.get(13).getInfo()));
			((Profesor)u).setMaxOre(Integer.parseInt(this.editableDatas.get(14).getInfo()));
		}else
			u=new User();
		u.setId(user.getId());
		if(!source.getField(1).getInfo().trim().equals("")) 
			u.setUsername(this.editableDatas.get(1).getInfo());
		u.setCNP(user.getCNP());
		if(!source.getField(2).getInfo().trim().equals(""))
			u.setParola(this.editableDatas.get(2).getInfo());
		if(!source.getField(4).getInfo().trim().equals(""))
			u.setNume(this.editableDatas.get(4).getInfo());
		if(!source.getField(5).getInfo().trim().equals(""))
			u.setPrenume(this.editableDatas.get(5).getInfo());
		if(!source.getField(6).getInfo().trim().equals(""))
			u.setAdresa(this.editableDatas.get(6).getInfo());
		if(!source.getField(7).getInfo().trim().equals(""))
			u.setTelefon(this.editableDatas.get(7).getInfo());
		if(!source.getField(8).getInfo().trim().equals(""))
			u.setEmail(this.editableDatas.get(8).getInfo());
		if(!source.getField(9).getInfo().trim().equals(""))
			u.setNrContract(this.editableDatas.get(9).getInfo());
		if(!source.getField(10).getInfo().trim().equals(""))
			u.setIBAN(this.editableDatas.get(10).getInfo());
		return u;
	}
}