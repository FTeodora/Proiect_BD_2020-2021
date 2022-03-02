package view;
import view.utility.*;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

import model.entity.Curs;
import model.entity.Grup;
import model.enumeration.UserRole;
public class GroupsView extends SearchView{
	private JComboBox<Curs> possibleCourses;
	private JCheckBox enrolledGroup;
	public GroupsView(ArrayList<Curs> courses,UserRole whoViews) {
	super("Grupuri de studiu",new Field(new JLabel("Nu sunteti inca membru intr-un grup de studiu")));
	
	if(courses==null)
	{
		if(whoViews.ordinal()<=UserRole.ADMIN.ordinal())
			JOptionPane.showMessageDialog(this, "Nu exista cursuri in baza de date","Warning",JOptionPane.WARNING_MESSAGE);
		else 
			JOptionPane.showMessageDialog(this, "Nu sunteti inca inrolat la un curs","Warning",JOptionPane.WARNING_MESSAGE);
	this.remove(search);
	}
	else {
		if(whoViews.ordinal()<=UserRole.ADMIN.ordinal())
			((JLabel)(this.err.getNameLabel())).setText("Momentan nu exista grupuri in baza de date");
		possibleCourses= new JComboBox(courses.toArray());
		
		enrolledGroup=new JCheckBox("Inscris");
		enrolledGroup.setBackground(AppWindow.headerColor);
		enrolledGroup.setForeground(Color.WHITE);
		enrolledGroup.setFont(AppWindow.MAIN_FONT);
		
		possibleCourses.setBackground(Color.WHITE);
		possibleCourses.setForeground(AppWindow.darkestBlue);
		searchBar.add(possibleCourses);
		if(whoViews==UserRole.STUDENT)
			searchBar.add(enrolledGroup);
		searchBar.add(cancel);
		
		ArrayList<Field> fields=new ArrayList<Field>();
		fields.add(new Field("Nume:",new JTextField()));
		fields.get(0).getInfoField().setMaximumSize(new Dimension(400,65));
		fields.add(new Field("Materie:",new JComboBox(courses.toArray())));
		fields.get(1).getInfoField().setMaximumSize(new Dimension(400,65));
		fields.add(new Field("Descriere:",new JTextArea()));
		if(whoViews==UserRole.STUDENT) {
			
			super.addCreateForm(fields, "grup");
			searchBar.add(createNew);
		}
		else
		{
			super.addAdvancedSearch(fields, "grup");			
			this.searchBar.add(this.advanced);
		}
	}
	
	}
public String getMaterie() {
		return  possibleCourses.getSelectedItem().toString().trim();
	}
public int getIDMaterie() {
	return ((Curs)possibleCourses.getSelectedItem()).getId();
}
public boolean getEnrolled() {
	return this.enrolledGroup.isSelected();
}
public void addResults(ArrayList<Grup> grupuri,boolean isEnrolled,UserRole who) {
	this.results.resetFields(GroupWindow.toWindows(grupuri, isEnrolled,who));
}
public void setMain(ArrayList<Grup> grupuri,ArrayList<Curs> cursuri,UserRole who) {
	this.mainPage.resetFields(GroupWindow.toWindows(grupuri, true, who));
	if(possibleCourses!=null)
		resetComboBox(possibleCourses,cursuri);
	if(createNew!=null)
		resetComboBox(((JComboBox)(this.form.getField(1).getInfoField())),cursuri);
	
}
private void resetComboBox(JComboBox<Curs> source,ArrayList<Curs> cursuri) {
	source.removeAllItems();
	for(Curs i:cursuri)
		source.addItem(i);
}
}
