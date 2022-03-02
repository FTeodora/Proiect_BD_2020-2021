package view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.*;

import model.entity.Curs;
import service.StudentService;
import view.utility.*;

public class GradesView extends OptionsView{
	private CasettePanel gradesList;
	public GradesView() {
		super("Note");
		gradesList=new CasettePanel(new Field(new JLabel("Momentan nu sunteti inscris la un curs")));
		this.add(gradesList,BorderLayout.CENTER);
		
	}
	public void addGrades(ArrayList<Curs> courses,StudentService service) {
		gradesList.resetFields(GradesWindow.toWindows(courses,service));
	}
}
