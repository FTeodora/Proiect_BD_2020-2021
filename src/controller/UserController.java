package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import controller.UserController.OptionButtonListener;
import service.UserService;
import view.PersonalInfoView;
import view.UserView;
import view.utility.NavigationButton;
public class UserController{
	protected ArrayList<OptionsController> controllers;
	protected final UserView view;
    protected final UserService userService;
    public UserController(UserView view,UserService service,JFrame prev) {
       this.view = view;
       view.setSessionMessage(service.getSession());
       view.setLocationRelativeTo(prev);
       
       this.userService = service;
       view.addLogOutButtonListener(new LogOutButtonListener());
       view.addOptionButtonListener(0,new OptionButtonListener());
       view.addOptionButtonListener(1,new OptionButtonListener());
       view.addOptionButtonListener(2,new OptionButtonListener());
       view.addOptionButtonListener(3,new OptionButtonListener());
       
       controllers=new ArrayList<OptionsController>();
       controllers.add(new OptionsController(new PersonalInfoView(userService.getSession().getSessionType()),this));
       view.setOptionLink(0, controllers.get(0).getView());
       controllers.add(new NotificationsController(this));
       view.setOptionLink(1, controllers.get(1).getView());
       controllers.add(new TimeTableController(this));
       view.setOptionLink(2, controllers.get(2).getView());
       controllers.add(new CourseController(this));
       view.setOptionLink(3, controllers.get(3).getView());
   }
protected class LogOutButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
		     ArrayList<NavigationButton> buttons=view.getOptions();
		     for(NavigationButton i:buttons)
		     {
		    	 if(i.getLink()!=null)
		    		 i.getLink().dispose();
		     }
		     view.getLogOutLink().setVisible(true);
		     view.dispose();
		  }
		
   }
 protected class OptionButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
		    view.setVisible(false);
		    JFrame source=((NavigationButton)e.getSource()).getLink();
		    source.setSize(view.getSize());
		    source.setLocationRelativeTo(view);
		    source.setVisible(true);
		    for(OptionsController i:controllers) {
		    	i.refresh();
		    }
		  }
		
}
 	public UserService getService() {
 		return this.userService;
 	}
 	public JFrame getView() {
 		return this.view;
 	}
}

