package view;
import view.utility.*;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;

import model.entity.Curs;
import service.ProfesorService;
public class ActivityManagementView extends OptionsView{
	private CasettePanel courseList;
	public ActivityManagementView() {
		super("Gestionare ponderi activitati");
		courseList=new CasettePanel(new Field(new JLabel("Momentan nu sunteti asignat unui curs")));
		this.add(courseList,BorderLayout.CENTER);
	}
public void addCourses(ArrayList<Curs> courses,ProfesorService service) {
	courseList.resetFields(WeightWindow.toWindows(courses,service));
}
}
