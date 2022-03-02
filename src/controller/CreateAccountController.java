package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import service.AdminService;
import service.SuperAdminService;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;
import model.enumeration.UserRole;
import view.CreateAccountView;
import view.OptionsView;

public class CreateAccountController extends OptionsController {
	public CreateAccountController(AccountsController prev) {
		super((OptionsView)new CreateAccountView(prev.parent.userService.getSession().getSessionType()),prev);
		((CreateAccountView)view).addButtonListeners(new CreateListener(), new ClearFields());
	}
class CreateListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		User u=((CreateAccountView)view).retrieveUserInfo();
		boolean b=false;
		if(u!=null)
			{
			if(u instanceof Student)
				b=((AdminService)(parent.userService)).createStudent((Student)u);
			else if(u instanceof Profesor)
				b=((AdminService)(parent.userService)).createProfesor((Profesor)u);
			else
				b=((SuperAdminService)(parent.userService)).createAdmin(u);
			}
		if(b)
			JOptionPane.showMessageDialog(null, "Inserarea s-a realizat cu succes", "Succes",JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Nu s-a putut crea user-ul", "Eroare",JOptionPane.ERROR_MESSAGE);
		
	}
}
class ClearFields implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		((CreateAccountView)view).resetFields();
	}
}
}
