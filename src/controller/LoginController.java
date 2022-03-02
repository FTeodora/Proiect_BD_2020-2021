package controller;

import repository.impl.JDBConnectionWrapper;
import view.LoginView;
import view.UserView;
import model.entity.User;
import service.ContextHolder;
import service.UserService;
import service.impl.ContextHolderImpl;
import service.impl.UserServiceImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
   

	private final LoginView view;
    private final UserService userService;

    public LoginController(LoginView view, JDBConnectionWrapper connection) {
        this.view = view;
        this.userService = new UserServiceImpl(connection);
        
        view.addLoginButtonListener(new LoginButtonListener());
        view.addCancelButtonListener(new CancelButtonListener());
        
    }

    private class LoginButtonListener implements ActionListener{
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsernameFieldText();
            String password = view.getPasswordFieldText();
            User user = userService.login(username,password);
            if(user!=null){
            	view.setVisible(false);
            	ContextHolder session=new ContextHolderImpl(user);
            	session.setJdbConnectionWrapper(userService.getSession().getJdbConnectionWrapper());
            	view.setUsernameFieldText(""); 
            	view.setPasswordFieldText("");
            	switch(session.getSessionType()) {
            	case STUDENT:{
            	new StudentController(session,view);
            		break;
            	}
            	case PROFESOR:{
            	new ProfesorController(session,view);
            	
            		break;
            	}
            	case ADMIN:{
            	new AdminController(session,view);
            		break;
            	}
            	case SUPERADMIN:{
            		new AdminController(session,view,true);
            		break;
            	}
            	default:new UserController(new UserView(view),new UserServiceImpl(session),view);
            	}
            	 
            }
            else{
                LoginView.ErrorMessage();
                view.setPasswordFieldText("");
            }  
            
        }
    }
    private class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.setUsernameFieldText("");
            view.setPasswordFieldText("");
        }
    }
    public LoginView getView() {
		return view;
	}
    
}