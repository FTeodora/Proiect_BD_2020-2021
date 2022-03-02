package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.AccountsView;
import view.utility.NavigationButton;

public class AccountsController extends OptionsController {
	public AccountsController(AdminController parent){
		super(new AccountsView(),parent);
		((AccountsView)(this.view)).addNewAccListener(new GoToCreate());
		((AccountsView)(this.view)).addModifyAccListener(new GoToModify());
	}
AccountsController t=this;
class GoToCreate implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		NavigationButton src=(NavigationButton)e.getSource();
		CreateAccountController aux=new CreateAccountController(t);
		src.setLink(aux.getView());
		src.getLink().setSize(view.getSize());
		src.getLink().setLocationRelativeTo(view);
		src.getLink().setVisible(true);
		view.setVisible(false);
	}
}
class GoToModify implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		NavigationButton src=(NavigationButton)e.getSource();
		SearchUserController aux=new SearchUserController(t);
		src.setLink(aux.getView());
		src.getLink().setSize(view.getSize());
		src.getLink().setLocationRelativeTo(view);
		src.getLink().setVisible(true);
		view.setVisible(false);
	}
}
}
