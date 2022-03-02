package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.entity.Activitate;
import model.entity.ActivitateGrup;
import model.entity.Curs;
import model.entity.Profesor;
import model.entity.Student;
import model.enumeration.UserRole;
import service.ProfesorService;
import view.utility.*;

public class ParticipantsView extends OptionsView{
	private JComboBox<String> departamente;
	private JPanel students,teachers;
	private CasettePanel studentList,teacherList;
	private CasettePanel addTeacherList;
	private JButton download, invite,cancelInvite,search,cancelSearch;
	private ArrayList<Casette> catalog;
	public ParticipantsView(Curs c,UserRole who) {
		super("Participantii \nde la cursul "+c.getMaterie());
		
		JTabbedPane participants = new JTabbedPane(JTabbedPane.TOP);
		this.add(participants, BorderLayout.CENTER);
		participants.setBackground(AppWindow.mainColor);
		participants.setForeground(Color.WHITE);
		
		if(who.ordinal()<=UserRole.ADMIN.ordinal()) {
			
			teachers = new JPanel(new CardLayout());
			teachers.setBackground(AppWindow.mainColor);
			participants.addTab("Profesori", null, teachers, "profesorii principali si asistenti asociati acestei materii");
			
			JPanel allTeachers=new JPanel(new BorderLayout());
			teacherList=new CasettePanel(new Field("Momentan nu exista profesori asignati acestui curs",new JLabel(" ")));
			JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT));
			footer.setBackground(AppWindow.headerColor);
			departamente=new JComboBox<String>(AppWindow.departamente);
			invite=new JButton("Asigneaza profesor");
			AppWindow.formatButton(invite, new Dimension(150,30));
			footer.add(invite);
			allTeachers.add(teacherList,BorderLayout.CENTER);
			allTeachers.add(footer,BorderLayout.SOUTH);
			teachers.add(allTeachers,"prof");
			
			JPanel newTeachers=new JPanel(new BorderLayout());
			addTeacherList=new CasettePanel(new Field("Momentan nu exista sugestii pentru aceasta actiune",new JLabel(" ")));
			JPanel footer1=new JPanel(new FlowLayout(FlowLayout.RIGHT));
			footer1.setBackground(AppWindow.headerColor);
			search=new JButton(new ImageIcon(CourseView.class.getResource("\\icons\\searchIcon.png")));
			search.setBackground(AppWindow.headerColor);
			search.setBorder(new EmptyBorder(0,0,0,0));
			footer1.add(search);
			
			
			cancelSearch=new JButton(new ImageIcon(CourseView.class.getResource("\\icons\\cancel.png")));
			cancelSearch.setBackground(AppWindow.headerColor);
			cancelSearch.setBorder(new EmptyBorder(0,0,0,0));
			cancelSearch.setEnabled(false);
			footer1.add(cancelSearch);
			
			cancelInvite=new JButton("Anuleaza");
			AppWindow.formatButton(cancelInvite, new Dimension(80,30));
			
			footer1.add(departamente);
			footer1.add(cancelInvite);
			departamente.setBackground(Color.WHITE);
			departamente.setForeground(AppWindow.headerColor);
			departamente.setPreferredSize(new Dimension(300,30));
			newTeachers.add(addTeacherList,BorderLayout.CENTER);
			newTeachers.add(footer1,BorderLayout.SOUTH);
			teachers.add(newTeachers,"add");
			
		}
		 
		students = new JPanel(new BorderLayout());
		students.setBackground(AppWindow.mainColor);
		studentList=new CasettePanel(new Field("Momentan nu exista studenti inscrisi la acest curs",new JLabel(" ")));	
		participants.addTab("Studenti", null, students, "studentii inscrisi la aceasta meterie");
		
		if(who==UserRole.PROFESOR)
		{
			JPanel footer2=new JPanel(new FlowLayout(FlowLayout.RIGHT));
			footer2.setBackground(AppWindow.headerColor);
			download=new JButton("Descarcare");
			AppWindow.formatButton(download, new Dimension(80,30));
			footer2.add(download);
			students.add(footer2,BorderLayout.SOUTH);
		}
		students.add(studentList,BorderLayout.CENTER);
		}
	public ParticipantsView(Activitate a) {
		super("Participantii de la activitatea\n "+a.getTipActivitate());
		
		studentList=new CasettePanel(new Field("Momentan nu exista studenti inscrisi la aceasta activitate",new JLabel(" ")));	
		this.add(studentList,BorderLayout.CENTER);
		
		JPanel footer = new JPanel();
		footer.setBackground(AppWindow.mainColor);
		download=new JButton("Descarca situatie scolara");
		footer.add(download);
		}
	public ParticipantsView(ActivitateGrup g,UserRole who) {
		super("Participanti activitate\n pe grupul "+g.getGrupName());
		
		JTabbedPane participants = new JTabbedPane(JTabbedPane.TOP);
		this.add(participants, BorderLayout.CENTER);
		participants.setBackground(AppWindow.mainColor);
		participants.setForeground(Color.WHITE);
		
			teachers = new JPanel(new CardLayout());
			teachers.setBackground(AppWindow.mainColor);
			participants.addTab("Profesori", null, teachers, "profesorii care participa la aceasta activitate");
			
			JPanel allTeachers=new JPanel(new BorderLayout());
			teacherList=new CasettePanel(new Field("Momentan nu exista profesori care participa",new JLabel(" ")));
			JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT));
			footer.setBackground(AppWindow.headerColor);
			
			invite=new JButton("Invita");
			AppWindow.formatButton(invite, new Dimension(150,30));
			if(!g.getTimpAnuntare().equals("00:00:00"))
			footer.add(invite);
			allTeachers.add(teacherList,BorderLayout.CENTER);
			if(who==UserRole.STUDENT)
				allTeachers.add(footer,BorderLayout.SOUTH);
			teachers.add(allTeachers,"prof");
			if(who==UserRole.STUDENT)
			{
				JPanel newTeachers=new JPanel(new BorderLayout());
				addTeacherList=new CasettePanel(new Field("Momentan nu exista sugestii pentru aceasta actiune",new JLabel(" ")));
				JPanel footer1=new JPanel(new FlowLayout(FlowLayout.RIGHT));
				footer1.setBackground(AppWindow.headerColor);
				
				cancelInvite=new JButton("Anuleaza");
				AppWindow.formatButton(cancelInvite, new Dimension(80,30));
				
				
				footer1.add(cancelInvite);
				newTeachers.add(addTeacherList,BorderLayout.CENTER);
				newTeachers.add(footer1,BorderLayout.SOUTH);
				teachers.add(newTeachers,"add");
			}	
		 
		students = new JPanel(new BorderLayout());
		students.setBackground(AppWindow.mainColor);
		studentList=new CasettePanel(new Field("Momentan nimeni nu participa la aceasta activitate",new JLabel(" ")));	
		participants.addTab("Studenti", null, students, "membrii ce participa la aceasta activitate");
		students.add(studentList,BorderLayout.CENTER);
		}
public void addParticipants(ArrayList<Profesor> teachers,ArrayList<Student> students,UserRole who) {
	if(teacherList!=null)
		teacherList.resetFields(ParticipantWindow.toWindows(teachers,true,who));
	if(studentList!=null)
		studentList.resetFields(ParticipantWindow.toWindows(students,who));
}
public String getDepartament() {
	return (String)this.departamente.getSelectedItem();
}
public void addGradeStatus(ArrayList<Student> students,UserRole who,ProfesorService service) {
	this.catalog=ParticipantWindow.toWindows(students,who,service);
	studentList.resetFields(catalog);
}
public void addSuggestedTeachers(ArrayList<Profesor> teachers) {
	addTeacherList.resetFields(ParticipantWindow.toWindows(teachers,false,UserRole.ADMIN));
}
public void removeSuggestedTeacher(Field f) {
	addTeacherList.removeField(f);
}
public JPanel getTeacherPanel() {
	return this.teachers;
}
public JButton getCancel() {
	return this.cancelSearch;
}
public ArrayList<Casette> getCatalog(){
	return this.catalog;
}
public void addPossibleActions(ArrayList<ActionListener> e) {
	if(teacherList!=null)
		teacherList.setActions(e);
	if(studentList!=null)
		studentList.setActions(e);
	if(addTeacherList!=null)
		addTeacherList.setActions(e);
}
public void setConnection(Connection c) {
	if(teacherList!=null)
		teacherList.setConnection(c);
	if(studentList!=null)
		studentList.setConnection(c);;
}
public void addSearchButtonListener(ActionListener e) {
	this.search.addActionListener(e);
}
public void addCancelSearchListener(ActionListener e) {
	this.cancelSearch.addActionListener(e);
}
public void addAssignListener(ActionListener e) {
	this.invite.addActionListener(e);
}
public void addCancelAssignListener(ActionListener e) {
	this.cancelInvite.addActionListener(e);
}
public void addDownloadListener(ActionListener e) {
	this.download.addActionListener(e);
}
}
