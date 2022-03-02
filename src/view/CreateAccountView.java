package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.utility.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;
import model.enumeration.UserRole;
public class CreateAccountView extends OptionsView{
	private final ScrollablePanel fields;
	private final JButton submit;
	private final JButton clear;
	public CreateAccountView(UserRole who) {
		super("Creare cont");
		ArrayList<Field> infoFields=new ArrayList<Field>();
		infoFields.add(new Field("Username:",new JTextField()));
		infoFields.add(new Field("Parola:",new JPasswordField()));
		infoFields.add(new Field("CNP:",new JFormattedTextField()));
		infoFields.add(new Field("Nume:",new JTextField()));
		infoFields.add(new Field("Prenume:",new JTextField()));
		infoFields.add(new Field("Adresa:",new JTextField()));
		infoFields.add(new Field("Telefon:",new JFormattedTextField()));
		infoFields.add(new Field("E-mail:",new JTextField()));
		infoFields.add(new Field("Numar contract:",new JTextField()));
		infoFields.add(new Field("IBAN:",new JTextField()));
		for(Field i:infoFields) {
			i.getInfoField().setPreferredSize(new Dimension(400,45));
		}
		String[] R= {"STUDENT","PROFESOR"};
		JComboBox<String> roles=new JComboBox<String>(R);
		if(who==UserRole.SUPERADMIN)
			roles.addItem("ADMIN");
		infoFields.add(new Field("Rol:",roles));
		JPanel role=new JPanel();
		role.setLayout(new CardLayout());
		role.setBorder(new EmptyBorder(0,0,0,0));
		role.setMaximumSize(new Dimension(200,200));
		CardLayout cl=(CardLayout)role.getLayout();
		roles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(role, roles.getSelectedItem().toString());
			}
		});
		role.setBorder(new EmptyBorder(0,0,0,0));
		
		ArrayList<Field> studentFields=new ArrayList<Field>();
		studentFields.add(new Field("An",new JComboBox<String>(AppWindow.ani)));
		studentFields.add(new Field("Nr ore",new JSpinner(new SpinnerNumberModel(60,1,90,1))));
		ScrollablePanel p1=new ScrollablePanel(studentFields);
		p1.formatFields(AppWindow.darkestBlue, AppWindow.TEXT_FONT);
		p1.setBorder(new EmptyBorder(0,0,0,0));
		p1.getContent().setBorder(new EmptyBorder(0,0,0,0));
		
		ArrayList<Field> profesorFields=new ArrayList<Field>();
		profesorFields.add(new Field("Departament",new JComboBox<String>(AppWindow.departamente)));
		profesorFields.add(new Field("Numar minim ore",new JSpinner(new SpinnerNumberModel(60,1,90,1))));
		profesorFields.add(new Field("Numar maxim ore",new JSpinner(new SpinnerNumberModel(60,1,90,1))));
		ScrollablePanel p2=new ScrollablePanel(profesorFields);
		p2.formatFields(AppWindow.darkestBlue, AppWindow.TEXT_FONT);
		p2.setBorder(new EmptyBorder(0,0,0,0));
		p2.getContent().setBorder(new EmptyBorder(0,0,0,0));
		
		JPanel p3=new JPanel();
		p3.setBorder(new EmptyBorder(0,0,0,0));
		role.add(p1,"STUDENT");
		role.add(p2,"PROFESOR");
		role.add(p3,"ADMIN");
		
		infoFields.add(new Field(role));
		
		fields=new ScrollablePanel(infoFields);
		fields.formatFields(AppWindow.darkestBlue, AppWindow.TEXT_FONT);
		fields.unifyFields();
		this.add(fields,BorderLayout.CENTER);
		submit=new JButton("Inserare");
		clear=new JButton("Reseteaza informatii");
		
		AppWindow.formatButton(submit,new Dimension(80,30));
		AppWindow.formatButton(clear, new Dimension(160,30));
		JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btns.add(submit);
		btns.add(clear);
		btns.setBackground(AppWindow.headerColor);
		this.add(btns,BorderLayout.SOUTH);
		
		
	}
public void addButtonListeners(ActionListener e1,ActionListener e2) {
this.submit.addActionListener(e1);
this.clear.addActionListener(e2);
}
public User retrieveUserInfo() {
	ArrayList<String> infos=null;
	ArrayList<String> auxInfos=null;
	try {
		infos=fields.getDatas();
		JPanel aux=(JPanel)fields.getField(11).getInfoField();
		ScrollablePanel p=(ScrollablePanel)aux.getComponent(0);
		auxInfos=p.getDatas();
		fields.getField(2).checkNumericConstraint();
		fields.getField(2).checkExactCharacters(13);
		fields.getField(6).checkNumericConstraint();
		fields.getField(6).checkExactCharacters(10);
	}
	catch(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
		return null;
	}
	User rez=new User();
	if(infos.get(10).equals("STUDENT"))
	{
		rez=new Student();
		try {
			JPanel aux=(JPanel)fields.getField(11).getInfoField();
			ScrollablePanel p=(ScrollablePanel)aux.getComponent(0);
			auxInfos=p.getDatas();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		((Student)rez).setAn(Integer.parseInt(auxInfos.get(0)));
		((Student)rez).setNrOre(Integer.parseInt(auxInfos.get(1)));
		rez.setTipUser(UserRole.STUDENT);
	}
	if(infos.get(10).equals("PROFESOR"))
	{
		rez=new Profesor();
		try {
			JPanel aux=(JPanel)fields.getField(11).getInfoField();
			ScrollablePanel p=(ScrollablePanel)aux.getComponent(1);
			auxInfos=p.getDatas();
			p.getField(1).checkNumberRelativeTo(p.getField(2));
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		((Profesor)rez).setDepartament(auxInfos.get(0));
		((Profesor)rez).setMinOre(Integer.parseInt(auxInfos.get(1)));
		((Profesor)rez).setMaxOre(Integer.parseInt(auxInfos.get(2)));
		rez.setTipUser(UserRole.PROFESOR);
	}
	if(infos.get(10).equals("ADMIN"))
		rez.setTipUser(UserRole.ADMIN);
	
		rez.setUsername(infos.get(0));
		rez.setParola(infos.get(1));
		rez.setCNP(infos.get(2));
		rez.setNume(infos.get(3));
		rez.setPrenume(infos.get(4));
		rez.setAdresa(infos.get(5));
		rez.setTelefon(infos.get(6));
		rez.setEmail(infos.get(7));
		rez.setNrContract(infos.get(8));
		rez.setIBAN(infos.get(9));
		
	return rez;
}
public void resetFields() {
	fields.resetDatas();
	JPanel aux=(JPanel)fields.getField(11).getInfoField();
	ScrollablePanel p=(ScrollablePanel)aux.getComponent(0);
	p.resetDatas();
	p=(ScrollablePanel)aux.getComponent(1);
	p.resetDatas();
}
}
