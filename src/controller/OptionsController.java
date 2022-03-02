package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import service.ProfesorService;
import service.StudentService;
import service.UserService;
import view.OptionsView;
import view.PersonalInfoView;
import view.UserView;
import view.ActivityManagementView;
import view.GradesView;

public class OptionsController {
	protected OptionsView view;
	protected final UserController parent;
	/**
	 * Constructorul pentru cazul in care fereastra anterioara este de asemenea o fereastra de optiuni
	 * @param view	Partea de vizualizare pentru acest controller
	 * @param parent Fereastra de optiuni din care s-a ajuns la fereastra noua
	 */
	OptionsController(OptionsView view,OptionsController parent){
		this.parent=parent.parent;
		this.view=view;
		this.view.setBackLink(parent.view);
		this.view.addBackButtonListener(new GetBackToMainOption());
		this.view.addRefreshButtonListener(new Refresh());
	}
	/**
	 * Constructorul pentru cazul in care fereastra este deschisa dintr-un meniu principal(optiuni de utilizator)
	 * In cazul acesta, frame-ul doar se face invizibil, facandu-se dispose din butonul de delogare
	 * @param view	Partea de vizualizare pentru acest controller
	 * @param parent Meniul de utilizator din care s-a ajuns la fereastra noua
	 */
	OptionsController(OptionsView view, UserController parent){
		this.parent=parent;
		this.view=view;
		this.view.setBackLink(parent.getView());
		this.view.addBackButtonListener(new GetBack());
		this.view.addRefreshButtonListener(new Refresh());
	}
class Refresh implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		refresh();
	}

}
class GetBack implements ActionListener{
	@Override
    public void actionPerformed(ActionEvent e) {
		view.getBackLink().setVisible(true);
		view.getBackLink().setSize(view.getSize());
		view.getBackLink().setLocationRelativeTo(view);
		view.dispose();
	}
}
class GetBackToMainOption implements ActionListener{
	@Override
    public void actionPerformed(ActionEvent e) {
		view.getBackLink().setVisible(true);
		view.getBackLink().setSize(view.getSize());
		view.getBackLink().setLocationRelativeTo(view);
		view.dispose();
	}
}
public OptionsView getView() {
	return view;
}
public UserView getParentView() {
	return this.parent.view;
}
/**
 * Initializeaza/Actualizeaza informatiile din baza de date afisate in interfata grafica
 * In functie de situatie, se apeleaza serviciile corespunzatoare informatiilor de afisat
 */
public void refresh() {
	if(view instanceof PersonalInfoView)
		((PersonalInfoView)(view)).addInfos(((UserService)parent.getService()).viewPersonalData());
	if(view instanceof GradesView)
		((GradesView)(view)).addGrades((parent.getService()).viewCourses(),(StudentService)parent.getService());		
	if(view instanceof ActivityManagementView)
		((ActivityManagementView)view).addCourses(((ProfesorService)parent.userService).viewManagedCourses(),(ProfesorService)this.parent.userService);

}
}
