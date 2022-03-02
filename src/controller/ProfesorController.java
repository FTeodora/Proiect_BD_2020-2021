package controller;

import javax.swing.JFrame;

import controller.UserController.OptionButtonListener;
import service.ContextHolder;
import service.impl.ProfesorServiceImpl;
import view.ActivityManagementView;
import view.ProfesorView;

public class ProfesorController extends UserController{
	public ProfesorController(ContextHolder session,JFrame prev) {
		super(new ProfesorView(prev),new ProfesorServiceImpl(session),prev);
		
		controllers.add(new OptionsController(new  ActivityManagementView(),this));
		view.addOptionButtonListener(4,new OptionButtonListener());
		this.view.setOptionLink(4, controllers.get(4).getView());
		
	}
}
