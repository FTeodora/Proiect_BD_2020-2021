package view.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.entity.Curs;
import model.enumeration.UserRole;

public class CourseWindow extends Casette {
	private Curs c; //informatia din spatele cursului
	private boolean isEnrolled; //indica daca este inrolat sau nu

	public CourseWindow(Curs c) {
		this.tabel="curs";
		this.c=c;
		infos.add(new JLabel(c.getMaterie()));
		infos.add(new JLabel(c.getTipCurs().toString()));
		if(c.getDescriere()==null)
			c.setDescriere("");
		infos.add(new JLabel("<html>"+c.getDescriere().replaceAll("\n", "<br>")+"</html>"));
		infos.add(new JLabel("An "+c.getAn()));
		header.add(infos.get(0));
		left.add(infos.get(1));
		center.add(infos.get(2));
		left.add(infos.get(3));
		
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		infos.get(0).setFont(AppWindow.HIGHLIGHTED_INFO);
		infos.get(2).setFont(AppWindow.SMALLER_FONT);
	}
	
public CourseWindow(Curs c,UserRole whoViews,boolean isEnrolled){
	
		this(c);
		whoSearched=whoViews;
		this.isEnrolled=isEnrolled;
		if(whoViews==UserRole.ADMIN||whoViews==UserRole.SUPERADMIN)
			makeEditable();
		}

/**
* Extrage informatia retinuta in caseta
* 
*
* @returns Cursul retinut in caseta
*/	
public Curs getCurs() {
	return this.c;
}

public void makeEditable() {
	ArrayList<Field> fields=new ArrayList<Field>();
	String[] tip= {"OBLIGATORIU","OPTIONAL"};
	fields.add(new Field("ID_curs",new JLabel(Integer.toString(c.getId()))));
	fields.add(new Field("Materie:",new JTextField(c.getMaterie())));
	fields.add(new Field("Tip:",new JComboBox<String>(tip)));
	((JComboBox<String>)(fields.get(2).getInfoField())).setSelectedIndex(c.getTipCurs().ordinal());
	fields.add(new Field("Descriere:",new JTextArea(c.getDescriere())));
	((JTextArea)(fields.get(3).getInfoField())).setBorder(new LineBorder(Color.WHITE));
	
	fields.add(new Field("An:",new JComboBox<String>(AppWindow.ani)));
	
	
	super.makeEditableCasette(fields);
	
	JPanel north=new JPanel(),center=new JPanel(),left=new JPanel();
	left.setLayout(new GridLayout(2,2));
	left.setPreferredSize(new Dimension(200,100));
	north.setBackground(AppWindow.mainColor);
	center.setBackground(AppWindow.mainColor);
	north.setBorder(new EmptyBorder(0,0,0,0));
	center.setBorder(new EmptyBorder(0,0,0,0));
	fields.get(1).add(north);
	fields.get(1).getInfoField().setPreferredSize(new Dimension(400,30));
	editpane.add(north,BorderLayout.NORTH);
	fields.get(2).add(left);
	fields.get(4).add(left);
	
	fields.get(3).add(center);
	editpane.add(left,BorderLayout.WEST);
	fields.get(3).getInfoField().setPreferredSize(new Dimension(225,85));
	editpane.add(center,BorderLayout.CENTER);
	edit.setValue(c.getId());
	delete.setValue(c.getId());
	
	buttons.add(edit);
	buttons.add(delete);
	
	this.setBackground(AppWindow.mainColor);
	this.colorFields(Color.WHITE, AppWindow.MAIN_FONT);
	for(Field i:fields) 
		i.setBackground(AppWindow.mainColor);
	((JTextArea)(fields.get(3).getInfoField())).setFont(AppWindow.SMALLER_FONT);;
	this.tabel="curs";
	
	
	
}
@Override
public void changeDatas() {
	infos.get(0).setText(this.editableDatas.get(1).getInfo());
	infos.get(1).setText(this.editableDatas.get(2).getInfo());
	infos.get(2).setText("<html>"+this.editableDatas.get(3).getInfo().replaceAll("\n", "<br>")+"</html>");
	infos.get(3).setText("An "+this.editableDatas.get(4).getInfo());
}
@Override
public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
	
		if(whoSearched==UserRole.STUDENT){
			if(isEnrolled) {
				InsertionButton activities=new InsertionButton(c.getId(),"Activitati");
				activities.addActionListener(actions.get(0));
				activities.setValue(this.c.getId());
				activities.setSource(source);
				options.add(activities);
				
				InsertionButton leave=new InsertionButton(c.getId(),"Paraseste");
				leave.addActionListener(actions.get(1));
				leave.setValue(this.c.getId());
				leave.setSource(source);
				options.add(leave);
				
				
			}
			else {
				InsertionButton join=new InsertionButton(c.getId(),"Alatura-te");
				join.addActionListener(actions.get(1));
				join.setValue(this.c.getId());
				join.setSource(source);
				options.add(join);
				
			}

		}
		else{
			
				InsertionButton activities=new InsertionButton(c.getId(),"Activitati");
				activities.addActionListener(actions.get(0));
				activities.setSource(source);
				options.add(activities);
			
				InsertionButton participants=new InsertionButton(c.getId(),"Participanti");
				participants.addActionListener(actions.get(1));
				participants.setSource(source);
				options.add(participants);

		}
}	

/**
* Transforma o lista de cursuri in casete pentru curs. UserRole indica din ce situatie este vizualizata caseta 
*      	- un student care vrea sa-si vada cursurile la care e inrolat/cauta cursuri la care sa se inroleze
*	  	- un profesor care vrea sa vada la ce cursuri e asignat
*		- un admin/superadmin care vrea sa vada cursuri
*
* @returns O lista de casete ce contin informatiile din lista de cursuri
*/
public static ArrayList<Casette> toWindows(ArrayList<Curs> courses,UserRole who,boolean isEnrolled){
	ArrayList<Casette> rez=null;
	if(courses!=null) {
		rez=new ArrayList<Casette>();
		for(Curs i:courses)
			rez.add(new CourseWindow(i,who,isEnrolled));	
	}
	else 
		System.out.println("NULL");
	return rez;
}
}

