package view;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;
import model.enumeration.UserRole;
import view.utility.*;
public class PersonalInfoView extends OptionsView{
	private ArrayList<Field> infoFields;
	public PersonalInfoView(UserRole rank){
		super("Date Personale");
		
		infoFields=new ArrayList<Field>();
		infoFields.add(new Field("Username: ", new JLabel()));
		infoFields.add(new Field("CNP: ", new JLabel()));
		infoFields.add(new Field("Nume: ",new JLabel()));
		infoFields.add(new Field("Prenume: ",new JLabel()));
		infoFields.add(new Field("Adresa: ",new JLabel()));
		infoFields.add(new Field("Telefon: ",new JLabel()));
		infoFields.add(new Field("e-mail: ",new JLabel()));
		infoFields.add(new Field("Numar contract: ",new JLabel()));
		infoFields.add(new Field("Cod IBAN: ",new JLabel()));
		infoFields.add(new Field("Rol: ",new JLabel()));
		if(rank==UserRole.STUDENT) {
			infoFields.add(new Field("An: ",new JLabel()));
			infoFields.add(new Field("Numar ore: ",new JLabel()));
		}
		if(rank==UserRole.PROFESOR) {
			infoFields.add(new Field("Departament: ",new JLabel()));
			infoFields.add(new Field("Numar minim ore: ",new JLabel()));
			infoFields.add(new Field("Numar maxim ore: ",new JLabel()));
		}
		for(Field i:infoFields) {
			i.getInfoField().setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 2));
		}
		ScrollablePanel infos=new ScrollablePanel(infoFields);
		infos.getContent().setBorder(new EmptyBorder(50,40,50,40));
		infos.setLabelFont(AppWindow.TEXT_FONT);
		infos.setFieldFont(AppWindow.TEXT_FONT);
		//infos.setBg(Color.WHITE);
		infos.setFg(AppWindow.darkestBlue);
		add(infos,BorderLayout.CENTER);
		
	}
public void addInfos(User u) {
	((JLabel)(this.infoFields.get(0).getInfoField())).setText(u.getUsername());
	((JLabel)(this.infoFields.get(1).getInfoField())).setText(u.getCNP());
	((JLabel)(this.infoFields.get(2).getInfoField())).setText(u.getNume());
	((JLabel)(this.infoFields.get(3).getInfoField())).setText(u.getPrenume());
	((JLabel)(this.infoFields.get(4).getInfoField())).setText(u.getAdresa());
	((JLabel)(this.infoFields.get(5).getInfoField())).setText(u.getTelefon());
	((JLabel)(this.infoFields.get(6).getInfoField())).setText(u.getEmail());
	((JLabel)(this.infoFields.get(7).getInfoField())).setText(u.getNrContract());
	((JLabel)(this.infoFields.get(8).getInfoField())).setText(u.getIBAN());
	((JLabel)(this.infoFields.get(9).getInfoField())).setText(u.getTipUser().toString());
	if(u instanceof Student) {
		((JLabel)(this.infoFields.get(10).getInfoField())).setText(Integer.toString(((Student)u).getAn()));
		((JLabel)(this.infoFields.get(11).getInfoField())).setText(Integer.toString(((Student)u).getNrOre()));
	}
	if(u instanceof Profesor) {
		((JLabel)(this.infoFields.get(10).getInfoField())).setText(((Profesor)u).getDepartament());
		((JLabel)(this.infoFields.get(11).getInfoField())).setText(Integer.toString(((Profesor)u).getMinOre()));
		((JLabel)(this.infoFields.get(12).getInfoField())).setText(Integer.toString(((Profesor)u).getMaxOre()));
	}
	
}
}
