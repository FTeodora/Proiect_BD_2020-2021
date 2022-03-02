package controller;

import java.util.ArrayList;

import javax.swing.JFrame;

import model.entity.Curs;
import service.ContextHolder;
import service.StudentService;
import service.impl.StudentServiceImpl;
import view.GradesView;
import view.StudentView;


public class StudentController extends UserController{
	
	public StudentController(ContextHolder session,JFrame prev) {
		super(new StudentView(prev),new StudentServiceImpl(session),prev);
		
		OptionsController aux2=new OptionsController(new GradesView(), this);
		view.addOptionButtonListener(4,new OptionButtonListener());
		this.view.setOptionLink(4, aux2.getView());
		
		GroupsController aux4=new GroupsController(this);
		view.addOptionButtonListener(5,new OptionButtonListener());
		this.view.setOptionLink(5, aux4.getView());

		controllers.add(aux2);
		controllers.add(aux4);
		}

}
