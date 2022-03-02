package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import model.entity.Activitate;
import model.entity.Curs;
import model.entity.Profesor;
import model.enumeration.UserRole;
import service.ProfesorService;
import view.utility.*;

public class CourseActivitiesView extends OptionsView{
	private JPanel panels;
	private CasettePanel activities;
	private CreateActivityForm newActivity;
	private JButton createActivity,cancelCreate;
	public CourseActivitiesView(Curs c,UserRole who){
		super("Activitatile \nde la cursul "+c.getMaterie());
		activities=new CasettePanel(new Field(new JLabel("Momentan nu exista activitati la acest curs")));
		this.add(activities,BorderLayout.CENTER);
	}
	public CourseActivitiesView(Curs c,UserRole who,ProfesorService service,ArrayList<Profesor> profesori){
		super("Activitatile de la cursul "+c.getMaterie());
		panels=new JPanel(new CardLayout());
		
		activities=new CasettePanel(new Field(new JLabel("Momentan nu exista activitati la acest curs")));
		panels.add(activities,"activitati");
		
		JPanel createActivityForm=new JPanel(new BorderLayout());
		newActivity= new CreateActivityForm(c.getId(),service,profesori);
		createActivityForm.add(newActivity,BorderLayout.CENTER);
		panels.add(newActivity,"creare");
		
		JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		createActivity=new JButton("Creare");
		AppWindow.formatButton(createActivity, new Dimension(80,30));
		footer.setBackground(AppWindow.headerColor);
		footer.add(createActivity);
		activities.add(footer,BorderLayout.SOUTH);
		this.add(panels,BorderLayout.CENTER);
		
		cancelCreate=new JButton("Anulare");
		AppWindow.formatButton(cancelCreate, new Dimension(80,30));
	}
public void setMain(ArrayList<Activitate> activitati,UserRole who) {
	activities.resetFields(ActivityWindow.toWindows(activitati, who));
}
public void addCreateListener(ActionListener e) {
	this.createActivity.addActionListener(e);
}
public void addCancelListener(ActionListener e) {
	this.newActivity.addButton(cancelCreate, e);
}
public void addSubmitListener(ActionListener e) {
	this.newActivity.addSubmitListener(e);
}
public void addActions(ArrayList<ActionListener> e) {
	activities.setActions(e);
}
public Activitate getGeneratedActivity() {
	return newActivity.retrieveInfo();
}
public JPanel getPanels() {
	return this.panels;
}
}
