package view.utility;

import model.entity.Curs;
import service.StudentService;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

public class GradesWindow extends Casette{
	private Curs course;
	private float[] grades;
	public GradesWindow(Curs course,StudentService getGrades) {
		this.course=course;
		this.grades=getGrades.viewGrades(course.getId());
		infos.add(new JLabel("Nota curs:"));
		infos.add(new JLabel("Nota seminar:"));
		infos.add(new JLabel("Nota laborator:"));
		infos.add(new JLabel("Nota finala:"));
		for(float i:grades)
		{
			String nota=Float.toString(i);
			if(nota.equals("0.0"))
				nota="N/A";
			infos.add(new JLabel(nota));
		}
		
		infos.add(new JLabel(course.getMaterie()));
		center.setLayout(new GridLayout(2,4));
		center.add(infos.get(0));
		center.add(infos.get(1));
		center.add(infos.get(2));
		center.add(infos.get(3));
		center.add(infos.get(4));
		center.add(infos.get(5));
		center.add(infos.get(6));
		center.add(infos.get(7));
		
		this.remove(left);
		this.remove(options);
		header.add(infos.get(8));
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		this.setPreferredSize(new Dimension(700,120));
		this.setMaximumSize(new Dimension(700,120));
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		infos.get(8).setFont(AppWindow.HIGHLIGHTED_INFO);
		
	}
	public static ArrayList<Casette>  toWindows(ArrayList<Curs> courses,StudentService connection){
		ArrayList<Casette> rez=null;
		if(courses!=null&&courses.size()>0)
		{
			rez=new ArrayList<Casette>();
			for(Curs i:courses)
				rez.add(new GradesWindow(i,connection));
				
		}
		return rez;
	}
}
