package controller;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.entity.Activitate;
import model.entity.ActivitateGrup;
import model.entity.Curs;
import model.entity.Notificare;
import model.entity.Profesor;
import model.entity.Student;
import model.enumeration.UserRole;
import service.AdminService;
import service.ProfesorService;
import service.StudentService;
import view.TimeTableView;
import view.utility.Casette;
import view.utility.GroupActivityWindow;
import view.utility.InsertionButton;
import view.utility.ParticipantWindow;
import view.CourseActivitiesView;
import view.CourseView;
import view.GroupDetailsView;
import view.ParticipantsView;

public class ParticipantsController extends OptionsController{
	protected final Object parentType;
	protected final ArrayList<ActionListener> actions;
	public ParticipantsController(CourseController prev,Curs c,UserRole who) {
		super(new ParticipantsView(c,who),prev);
		actions=new  ArrayList<ActionListener>();
		this.parentType=c;
		if(who.ordinal()<=UserRole.ADMIN.ordinal()) {
			((ParticipantsView)view).addAssignListener(new AssignTeacherListener());
			((ParticipantsView)view).addCancelAssignListener(new CancelAssignTeacherListener());
			actions.add(new NewTeacherListener());
			((ParticipantsView)view).addPossibleActions(actions);
			((ParticipantsView)view).addSearchButtonListener(new SearchButton());
			((ParticipantsView)view).addCancelSearchListener(new CancelSearchListener());
			
		}
		if(who==UserRole.PROFESOR)
			((ParticipantsView)view).addDownloadListener(new DownLoadButtonListener());
	}
	public ParticipantsController(CourseActivitiesController prev,Activitate a) {
		super(new ParticipantsView(a),prev);
		actions=new  ArrayList<ActionListener>();
		((ParticipantsView)view).setConnection(prev.parent.userService.getConnection());
		this.parentType=a;
	}
	public ParticipantsController(GroupDetailsController prev,ActivitateGrup a) {
		super(new ParticipantsView(a,prev.parent.userService.getSession().getSessionType()),prev);
		actions=new  ArrayList<ActionListener>();
		if(prev.parent.userService instanceof StudentService)
		{
			((ParticipantsView)view).addAssignListener(new InviteNewTeacherListener());
			((ParticipantsView)view).addCancelAssignListener(new CancelAssignTeacherListener());		
		((ParticipantsView)view).setConnection(prev.parent.userService.getConnection());
		actions.add(new InviteTeacherListener());
		((ParticipantsView)view).addPossibleActions(actions);
		}
		this.parentType=a;
	}
class NewTeacherListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		InsertionButton source=(InsertionButton)e.getSource();
		if(((AdminService)(parent.userService)).assignTeacher(((Curs)parentType).getId(), source.getValue()))
		{
			JOptionPane.showMessageDialog(view, "Profesorul ales a fost asignat la curs!","Succes",JOptionPane.INFORMATION_MESSAGE);
			((ParticipantsView)view).removeSuggestedTeacher(source.getSource());
			refresh();
		}else 
			JOptionPane.showMessageDialog(view, "Profesorul nu a putut fi asignat la curs!","Eroare",JOptionPane.ERROR_MESSAGE);
	}
}
class AssignTeacherListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout cl=(CardLayout)(((ParticipantsView)view).getTeacherPanel().getLayout());
		cl.last(((ParticipantsView)view).getTeacherPanel());
		((ParticipantsView)view).addSuggestedTeachers(((AdminService)(parent.userService)).getOtherTeachersForCourse(((Curs)parentType).getId()));
	}
}
class CancelAssignTeacherListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout cl=(CardLayout)(((ParticipantsView)view).getTeacherPanel().getLayout());
		cl.first(((ParticipantsView)view).getTeacherPanel());
	}
}
class DownLoadButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Casette> catalog=((ParticipantsView)view).getCatalog();
		if(catalog==null||catalog.size()==0)
			JOptionPane.showMessageDialog(view, "Nu exista studenti la acest curs!","Eroare",JOptionPane.ERROR_MESSAGE);
		else
			{
			ParticipantWindow.toDocument(catalog, ((Curs)parentType).getMaterie());
		JOptionPane.showMessageDialog(view, "Catalogul a fost descarcat. Verificati-va fisierul de descarcari!","Succes",JOptionPane.INFORMATION_MESSAGE);
		
	}
	}
}
class SearchButton implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		ParticipantsView v=((ParticipantsView)view);
	v.addSuggestedTeachers(((AdminService)(parent.userService)).findTeachersbyDepartamentforAdmin(v.getDepartament(),((Curs)parentType).getId()));
	v.getCancel().setEnabled(true);
	}
}
class CancelSearchListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
	ParticipantsView v=((ParticipantsView)view);
	v.addSuggestedTeachers(((AdminService)(parent.userService)).getOtherTeachersForCourse(((Curs)parentType).getId()));
	v.getCancel().setEnabled(false);
	}
}
class InviteTeacherListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		InsertionButton source=(InsertionButton)e.getSource();
		Notificare n=new Notificare();
		n.setReceiverID(source.getValue());
		n.setSenderID(((ActivitateGrup)parentType).getId());
		n.setSenderName(((ActivitateGrup)parentType).getGrupName());
		n.setDescriere("Ati fost invitat la o activitate de grup pe data de\n"+((ActivitateGrup)parentType).getDataInceperii() );
		n.setExpirare(((ActivitateGrup)parentType).getTimpAnuntare());
		parent.userService.sendNotification(n);
		((ParticipantsView)view).removeSuggestedTeacher(source.getSource());
		JOptionPane.showMessageDialog(view, "Invitatia dumeavoastra a fost trimisa!","Succes",JOptionPane.INFORMATION_MESSAGE);
	}
	
}
class InviteNewTeacherListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		CardLayout cl=(CardLayout)(((ParticipantsView)view).getTeacherPanel().getLayout());
		cl.last(((ParticipantsView)view).getTeacherPanel());
		((ParticipantsView)view).addSuggestedTeachers(((StudentService)(parent.userService)).suggestedTeachers(((ActivitateGrup)parentType)));
	}
}
public void refresh() {
	ArrayList<Profesor> p=null;
	ArrayList<Student> s=null;
	ProfesorService service=null;
	if(view.getBackLink() instanceof CourseActivitiesView) {
		if((this.parent.userService) instanceof ProfesorService)
			s=((ProfesorService)(this.parent.userService)).viewActivityParticipants(((Activitate)parentType).getId());
		else
			s=((AdminService)(this.parent.userService)).getActivityParticipants(((Activitate)parentType).getId());
	}
	if(view.getBackLink() instanceof CourseView)
	{
		p=((this.parent.userService)).viewCourseTeachers(((Curs)parentType).getId());
		s=((this.parent.userService)).viewCourseStudents(((Curs)parentType).getId());
		if(this.parent.userService instanceof ProfesorService)
			service=(ProfesorService)this.parent.userService;
		
	}
	if(view.getBackLink() instanceof GroupDetailsView) {
		if((this.parent.userService) instanceof AdminService)
		{
			s=((AdminService)(this.parent.userService)).groupActivityStudents(((ActivitateGrup)parentType).getId());
			p=((AdminService)(this.parent.userService)).getGroupActivityTeachers(((ActivitateGrup)parentType));
		}
		else
		{
			s=((StudentService)(this.parent.userService)).groupActivityStudents(((ActivitateGrup)parentType).getId());
			p=((StudentService)(this.parent.userService)).getGroupActivityTeachers(((ActivitateGrup)parentType));
		}
	}
	if(service==null)
		((ParticipantsView)view).addParticipants(p, s, parent.userService.getSession().getSessionType());
	else
		((ParticipantsView)view).addGradeStatus(s, parent.userService.getSession().getSessionType(),service);
}
public Object getInfoObject() {
	return this.parentType;
	
}
}
