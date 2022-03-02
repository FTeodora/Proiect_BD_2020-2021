package controller;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import model.entity.Curs;
import model.entity.Profesor;
import service.AdminService;
import service.ProfesorService;
import service.StudentService;
import view.CourseActivitiesView;
import view.utility.ActivityWindow;
import view.utility.InsertionButton;

public class CourseActivitiesController extends OptionsController{
	Curs c;
	public CourseActivitiesController(Curs c,OptionsController parent) {
		super(new CourseActivitiesView(c,parent.parent.userService.getSession().getSessionType()),parent);
		ArrayList<ActionListener> actions=new ArrayList<ActionListener>();
		actions.add(new ViewParticipantsListener());
		((CourseActivitiesView)view).addActions(actions);
		this.c=c;
		this.refresh();
	}
	public CourseActivitiesController(Curs c,OptionsController parent,ProfesorService service,ArrayList<Profesor> profesor) {
		super(new CourseActivitiesView(c,parent.parent.userService.getSession().getSessionType(),service,profesor),parent);
		ArrayList<ActionListener> actions=new ArrayList<ActionListener>();
		actions.add(new ViewParticipantsListener());
		((CourseActivitiesView)view).addActions(actions);
		((CourseActivitiesView)view).addCreateListener(new ToNewForm());
		((CourseActivitiesView)view).addSubmitListener(new CreateActivity());
		((CourseActivitiesView)view).addCancelListener(new BackToMain());
		this.c=c;
		this.refresh();
	}
	CourseActivitiesController t=this;
	class ViewParticipantsListener implements ActionListener{
		@Override 
		public void actionPerformed(ActionEvent e) {
			InsertionButton source=(InsertionButton)e.getSource();
			ParticipantsController aux=new ParticipantsController(t,((ActivityWindow)(source.getSource().getInfoField())).getActivitate());
			aux.refresh();
			view.setVisible(false);
			aux.view.setSize(view.getSize());
			aux.view.setLocationRelativeTo(view);
			aux.view.setVisible(true);
		}
	}
	class ToNewForm implements ActionListener{
		@Override 
		public void actionPerformed(ActionEvent e) {
				CardLayout cl=(CardLayout)((((CourseActivitiesView)view).getPanels()).getLayout());
				cl.last(((CourseActivitiesView)view).getPanels());	
			
		}
	}
	class BackToMain implements ActionListener{
		@Override 
		public void actionPerformed(ActionEvent e) {
			CardLayout cl=(CardLayout)((((CourseActivitiesView)view).getPanels()).getLayout());
			cl.first(((CourseActivitiesView)view).getPanels());
			
		}
	}
	class CreateActivity implements ActionListener{
		@Override 
		public void actionPerformed(ActionEvent e) {
			boolean succes;
			if(parent.userService instanceof ProfesorService)
				succes=((ProfesorService)parent.userService).createActivity(((CourseActivitiesView)view).getGeneratedActivity());
			else
				succes=((AdminService)parent.userService).createActivity(((CourseActivitiesView)view).getGeneratedActivity());
				
				JOptionPane.showMessageDialog(view, "Inserarea s-a realizat cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
			
			
		}
	}
	@Override
	public void refresh() {
		((CourseActivitiesView)view).setMain((parent.userService).viewCourseActivities(c.getId()),parent.userService.getSession().getSessionType());
		
	}
}
