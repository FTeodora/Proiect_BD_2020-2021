package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import controller.GroupsController.AdvancedSearchListener;
import controller.SearchController.AdvancedButtonListener;
import controller.SearchController.SearchListener;
import model.entity.Curs;
import model.entity.Grup;
import model.entity.Profesor;
import model.enumeration.UserRole;
import service.AdminService;
import service.ProfesorService;
import service.StudentService;
import view.CourseView;
import view.GroupsView;
import view.SearchView;
import view.utility.CourseWindow;
import view.utility.InsertionButton;

public class CourseController extends SearchController{
	public CourseController(UserController parent) {
		super(new CourseView(parent.userService.getSession().getSessionType()),parent);
		((CourseView)this.view).addSearchButtonListener(new SearchCoursesListener());
		casetteEvents.add(new ActivityButtonListener());
		if(this.parent.userService instanceof StudentService) {
			casetteEvents.add(new CourseButtonListener());
		}
		else {
			casetteEvents.add(new ParticipantsButtonListener());
		}
		
		((SearchView)this.view).setListeners(casetteEvents);
		if(parent.userService.getSession().getSessionType().ordinal()<=UserRole.ADMIN.ordinal())
		{
			((CourseView)this.view).addAdvancedListener(new AdvancedButtonListener());
			((CourseView)this.view).addAdvancedSearchListener(new AdvancedSearchListener());
		}
		
		
}
	
class SearchCoursesListener extends SearchListener{
	@Override
	 public void actionPerformed(ActionEvent e) {
		CourseView v=((CourseView)view);
		ArrayList<Curs> rez=null;
		if(parent.userService instanceof StudentService)
			rez=((StudentService)parent.getService()).searchCourse(v.getSearchedText(),v.getEnrolled());
		if(parent.userService instanceof ProfesorService)
			rez=((ProfesorService)parent.getService()).searchCourse(v.getSearchedText());
		if(parent.userService instanceof AdminService)
			rez=((AdminService)parent.getService()).searchCourse(v.getSearchedText());
		v.addResults(rez,parent.getService().getSession().getSessionType());
			super.actionPerformed(e);
	}
}
class CourseButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		InsertionButton source=(InsertionButton)e.getSource();
		if(source.getText().equals("Alatura-te"))
		{
			if(((StudentService)parent.getService()).enrollInCourse(source.getValue()))
			{
				source.setText("Paraseste");
				((CourseView)view).removeResult(source.getSource());
			}
			else
				JOptionPane.showMessageDialog(null, "Nu ati fost inscris la acest curs!", "Eroare", JOptionPane.ERROR_MESSAGE);
			
		}
		else
		{
			if(((StudentService)parent.getService()).leaveCourse(source.getValue()))
			{
				source.setText("Alatura-te");
				((CourseView)view).removeCasette(source.getSource());
			}
			else
				JOptionPane.showMessageDialog(null, "Nu s-a putut parasi cursul!", "Eroare", JOptionPane.ERROR_MESSAGE);
			
		}
	}
}
CourseController t=this;
class ActivityButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		InsertionButton source=(InsertionButton)e.getSource();
		CourseActivitiesController aux=null;
		if(parent.userService instanceof StudentService)
		{
			aux=new CourseActivitiesController(((CourseWindow)(source.getSource().getInfoField())).getCurs(),t);
			
		}
		else {
			ArrayList<Profesor>profesori=(parent.userService).viewCourseTeachers(((CourseWindow)(source.getSource().getInfoField())).getCurs().getId());
			if(parent.userService instanceof ProfesorService)
				aux=new CourseActivitiesController(((CourseWindow)(source.getSource().getInfoField())).getCurs(),t,(ProfesorService)(parent.userService),profesori);
			else
				aux=new CourseActivitiesController(((CourseWindow)(source.getSource().getInfoField())).getCurs(),t,null,profesori);
			
		}
		((CourseActivitiesController)aux).refresh();
		t.view.setVisible(false);
		aux.view.setSize(t.view.getSize());
		aux.view.setLocationRelativeTo(t.view);
		aux.view.setVisible(true);
	}
}
class ParticipantsButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		InsertionButton source=(InsertionButton)e.getSource();
		ParticipantsController aux=new ParticipantsController(t,((CourseWindow)(source.getSource().getInfoField())).getCurs(),parent.userService.getSession().getSessionType());
		t.view.setVisible(false);
		aux.refresh();
		aux.view.setSize(t.view.getSize());
		aux.view.setLocationRelativeTo(t.view);
		aux.view.setVisible(true);
	}
}
class AdvancedSearchListener extends SearchListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		CourseView v=((CourseView)view);
		ArrayList<Curs> rez=null;
		try {
			ResultSet rst=v.getAdvancedFilter().findRowInTableFiltered();
			if(rst.next())
			{
				rez=new ArrayList<Curs>();
				rez.add(Curs.parseCurs(rst));
				while(rst.next())
					rez.add(Curs.parseCurs(rst));
			}
		}catch(Exception e1) {
			e1.printStackTrace();
		}
		v.addResults(rez, parent.userService.getSession().getSessionType());
		super.actionPerformed(e);
		
	}
	
}
@Override
public void refresh() {
	super.refresh();
	((CourseView)this.view).setMain((t.parent.userService).viewCourses(), parent.userService.getSession().getSessionType());
	
}
}
