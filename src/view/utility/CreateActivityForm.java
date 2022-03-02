package view.utility;

import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.entity.Activitate;
import model.entity.Organizator;
import model.entity.Profesor;
import model.enumeration.ActivityType;
import model.enumeration.Frequency;
import service.ProfesorService;
import service.UserService;

public class CreateActivityForm extends CreateNewForm {
	private JComboBox<Frequency>frecvente;
	private JComboBox<Activitate>types;
	private int courseID;
	private JComboBox principali;
	private JComboBox secundar;
	private int teacherID=0;
	public CreateActivityForm(int courseID, ProfesorService service,ArrayList<Profesor> profesor){
		super( new ArrayList<Field>(),"activitate");
		this.courseID=courseID;
		if(service!=null)
			teacherID=((UserService)service).getSession().getUserID();
		types=new JComboBox<Activitate>();

		if(service==null)
		{
			Activitate lab=new Activitate();
			lab.setTipActivitate(ActivityType.LABORATOR);
			types.addItem(lab);
			Activitate seminar=new Activitate();
			seminar.setTipActivitate(ActivityType.SEMINAR);
			types.addItem(seminar);
			Activitate curs=new Activitate();
			curs.setTipActivitate(ActivityType.CURS);
			types.addItem(curs);
		}
		else {
			ArrayList<Activitate> activitati=service.getActivityTypes(courseID);
			for(Activitate i:activitati)
				if(i.getTipActivitate()!=ActivityType.CURS)
					types.addItem(i);
		}
		datas.add(new Field("Tip: ",types));
		datas.add(new Field("Data inceperii: ",new CalendarProgramare(LocalDate.now().plusDays(1))));
		datas.add(new Field("Data incheierii: ",new CalendarProgramare(LocalDate.now().plusDays(1))));
		frecvente =new JComboBox<Frequency>();
		frecvente.addItem(Frequency.SAPTAMANAL);
		frecvente.addItem(Frequency.SAPTAMANAL_ALTERNANT);
		datas.add(new Field("Frecventa: ",frecvente));
		datas.add(new Field("Durata: ",new UnitLabel(5,60)));
		datas.add(new Field("Nr maxim participanti:",new UnitLabel(200)));
		//Object[] prof=(Profesor[])profesori.toArray();
		if(service==null)
		{
			try {
				principali=new JComboBox(profesor.toArray());
				datas.add(new Field("Profesor principal:",principali));
			}catch(NullPointerException e) {
				JOptionPane.showMessageDialog(null, "Nu exista profesori asignati acestui curs", "Eroare", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	
			secundar=new JComboBox(profesor.toArray());
			datas.add(new Field("Profesor activitate:",secundar));
			for(Field i: datas) 
				i.add(this.content);
		Field.formatFields(datas, AppWindow.darkestBlue, AppWindow.TEXT_FONT);
		 for(Field i:datas)
		 {
			 if(i.getInfoField() instanceof UnitLabel)
				 ((UnitLabel)i.getInfoField()).setBackground(AppWindow.lightestBlue);
			 i.getNameLabel().setAlignmentX(Component.LEFT_ALIGNMENT);
			 i.getInfoField().setAlignmentX(Component.LEFT_ALIGNMENT);
			 if( i.getInfoField() instanceof JTextField)
				 i.getInfoField().setPreferredSize(new Dimension(400,40));
		 }
	}
	public Activitate retrieveInfo() {
		ArrayList<String> Infos=new ArrayList<String>();
		try {
			Infos=this.getDatas();
			datas.get(1).checkDateRelativeTo(datas.get(2));
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
		}
		Activitate a=new Activitate();
		a.setCursId(courseID);
		a.setTipActivitate(((Activitate)types.getSelectedItem()).getTipActivitate());
		a.setDataInceperii(Infos.get(1));
		a.setDataIncheierii(Infos.get(2));
		a.setFrecventa((Frequency)frecvente.getSelectedItem());
		a.setDurata(Infos.get(4));
		a.setNrMaxParticipanti(Integer.parseInt(Infos.get(5)));
		a.setPondere(((Activitate)types.getSelectedItem()).getPondere());
		a.setOrganizator(new Organizator());
		if(teacherID!=0)
			a.getOrganizator().setProfesorPrincipal(this.teacherID);
		else
			a.getOrganizator().setProfesorPrincipal(((Profesor)(principali.getSelectedItem())).getId());
		a.getOrganizator().setUserId(((Profesor)(secundar.getSelectedItem())).getId());
		return a;
	}
}
