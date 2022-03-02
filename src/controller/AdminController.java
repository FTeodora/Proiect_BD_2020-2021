package controller;

import javax.swing.JFrame;

import service.ContextHolder;

import service.impl.*;
import view.AdminView;

public class AdminController extends UserController{
	public AdminController(ContextHolder session,JFrame prev) {
		super(new AdminView(prev),new AdminServiceImpl(session),prev);
		
		controllers.add(new GroupsController(this));
		view.addOptionButtonListener(4,new OptionButtonListener());
		this.view.setOptionLink(4, controllers.get(4).view);
		
		controllers.add(new AccountsController(this));
		view.addOptionButtonListener(5,new OptionButtonListener());
		this.view.setOptionLink(5, controllers.get(5).view);
		
	}
	public AdminController(ContextHolder session,JFrame prev,boolean SuperAdmin) {
		super(new AdminView(prev),new SuperAdminServiceImpl(session),prev);
		
		controllers.add(new GroupsController(this));
		view.addOptionButtonListener(4,new OptionButtonListener());
		this.view.setOptionLink(4, controllers.get(4).view);
		
		controllers.add(new AccountsController(this));
		view.addOptionButtonListener(5,new OptionButtonListener());
		this.view.setOptionLink(5, controllers.get(5).view);
		
	}
}
