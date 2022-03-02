package controller;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.entity.*;
import model.enumeration.UserRole;
import service.AdminService;
import service.StudentService;
import view.GroupDetailsView;
import view.utility.*;

public class GroupDetailsController extends OptionsController{
	private Grup grup;
	public GroupDetailsController(OptionsController prev,Grup g) {
		super(new GroupDetailsView(g,prev.parent.userService.getSession().getUserID(),(prev.parent.userService).getSession().getSessionType()),prev);
		this.grup=g;
		((GroupDetailsView)view).setConnection(prev.parent.userService.getSession().getJdbConnectionWrapper().getConnection());
		((GroupDetailsView)view).addSubmitMessageListener(new MessageSubmitListener());
		if((parent.userService).getSession().getSessionType()==UserRole.STUDENT)
		{
			((GroupDetailsView)view).addCreateListener(new CreateActivityListener());
			((GroupDetailsView)view).addCreateActivityListener(new SubmitActivityListener());
			((GroupDetailsView)view).addCreateCancelListener(new CancelCreateActivityListener());
			((GroupDetailsView)view).addInviteListener(new AddNewMemberListener());
			((GroupDetailsView)view).addCancelInviteListener(new CancelAddNewMemberListener());
		}
		ArrayList<ActionListener> messageActions= new ArrayList<ActionListener>();
		
		((GroupDetailsView)view).addMessageActions(messageActions);
		ArrayList<ActionListener> activitiesActions=new ArrayList<ActionListener>();
		activitiesActions.add(new ShowActivityParticipants());
		 activitiesActions.add(new ParticipateInActivityListener());
		((GroupDetailsView)view).addActivitiesActions(activitiesActions);
		
		ArrayList<ActionListener> participantsActions=new ArrayList<ActionListener>();
		participantsActions.add(new InviteStudentListener());
		participantsActions.add(new KickStudentListener());
		((GroupDetailsView)view).addParticipantsActions(participantsActions);
		
		refresh();
	}
	class MessageSubmitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(((GroupDetailsView)view).getNewMessageForm().insertTouple()==-1)
				JOptionPane.showMessageDialog(view, "Mesajul nu a putut fi postat!", "Eroare!", JOptionPane.INFORMATION_MESSAGE);
			else {
				((GroupDetailsView)view).getNewMessageForm().resetDatas();
				refresh();
			}
		}
	}
	class CreateActivityListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			CardLayout cl=(CardLayout)(((GroupDetailsView)view).getActivitiesPanel().getLayout());
			cl.last(((GroupDetailsView)view).getActivitiesPanel());
		}
	}
	class SubmitActivityListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			CreateNewForm v=((GroupDetailsView)view).getNewActivityForm();
			int returnedID=v.insertTouple();
			if(returnedID!=-1)
			{
				((StudentService)parent.userService).joinGroupActivity(returnedID);
						JOptionPane.showMessageDialog((GroupDetailsView)view, "Activitatea a fost adaugata cu succes! Adunati suficienti participanti pentru validarea ei!","Succes",JOptionPane.INFORMATION_MESSAGE);
						ArrayList<Student> membri=((StudentService)parent.userService).viewGroupMembers(grup.getId());
						Notificare n=new Notificare();
						n.setSenderID(returnedID);
						n.setSenderName(grup.getNume());
						n.setDescriere("A fost programata o noua activitate pe data de "+v.getField(2).getInfo().substring(0,v.getField(2).getInfo().length()-3));
						n.setExpirare(v.getField(1).getInfo());
						for(Student s:membri) {
							n.setReceiverID(s.getId());
							parent.userService.sendNotification(n);
						}
						refresh();
				
			}
			else
				JOptionPane.showMessageDialog((GroupDetailsView)view, "Activitatea nu a putut fi adaugata","Eroare",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	class CancelCreateActivityListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			CardLayout cl=(CardLayout)(((GroupDetailsView)view).getActivitiesPanel().getLayout());
			cl.first(((GroupDetailsView)view).getActivitiesPanel());
		}
	}
	class ParticipateInActivityListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			InsertionButton source=(InsertionButton)e.getSource();
			((StudentService)parent.userService).joinGroupActivity(source.getValue());
				JOptionPane.showMessageDialog((GroupDetailsView)view, "Ati fost inscris la activitate! Nu uitati sa participati la ora inceperii","Succes",JOptionPane.INFORMATION_MESSAGE);
				refresh();
		}
	}
	class AddNewMemberListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			CardLayout cl=(CardLayout)(((GroupDetailsView)view).getMembersPanel().getLayout());
			cl.last(((GroupDetailsView)view).getMembersPanel());
			((GroupDetailsView)view).addParticipantsSuggestions(((StudentService)parent.userService).groupMemberSuggestions(grup), UserRole.STUDENT);
		}
	}
	class CancelAddNewMemberListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			CardLayout cl=(CardLayout)(((GroupDetailsView)view).getMembersPanel().getLayout());
			cl.first(((GroupDetailsView)view).getMembersPanel());
			
		}
	}
	class InviteStudentListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Notificare n=new Notificare();
			InsertionButton source=(InsertionButton)e.getSource();
			n.setReceiverID(source.getValue());
			n.setSenderID(grup.getId());
			n.setSenderName(grup.getNume());
			n.setDescriere("Ati fost invitat(a) intr-un nou grup de studiu la materia\n "+grup.getNumeCurs());
			n.setExpirare("24:00:00");
			parent.userService.sendNotification(n);
			
				source.getSource().getInfoField().remove(source);
				JOptionPane.showMessageDialog((GroupDetailsView)view, "Invitatia a fost trimisa cu succes","Succes",JOptionPane.INFORMATION_MESSAGE);	
			}
		
	}
	class KickStudentListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			InsertionButton source=(InsertionButton)e.getSource();
			if(((AdminService)parent.userService).kickGroup( grup.getId(),source.getValue()))
			{
				Notificare n=new Notificare();
				n.setReceiverID(source.getValue());
				n.setSenderID(0);
				n.setSenderName(grup.getNume());
				n.setDescriere("Ati fost dat(a) afara din acest grup de studiu");
				n.setExpirare("48:00:00");
				parent.userService.sendNotification(n);
				((GroupDetailsView)view).removeMember(source.getSource());
				JOptionPane.showMessageDialog((GroupDetailsView)view, "Membrul ales a fost dat afara","Succes",JOptionPane.INFORMATION_MESSAGE);	
				((GroupDetailsView)view).getMembersPanel().remove(source.getSource().getInfoField());
			}
			else
				JOptionPane.showMessageDialog((GroupDetailsView)view, "Membrul nu a putut fi dat afara","Eroare",JOptionPane.ERROR_MESSAGE);	
		}
		
	}
	GroupDetailsController t=this;
	class ShowActivityParticipants implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			InsertionButton source=(InsertionButton)e.getSource();
			ActivitateGrup activitate=((GroupActivityWindow)(source.getSource().getInfoField())).getActivitate();
			activitate.setGrup(t.grup);
			ParticipantsController aux=new ParticipantsController(t,activitate);
			aux.view.setSize(view.getSize());
			aux.view.setLocationRelativeTo(view);
			aux.view.setVisible(true);
			view.setVisible(false);
			aux.refresh();
			
		}
		
	}
	@Override
	public void refresh() {
		ArrayList<ActivitateGrup>activitati=null;
		ArrayList<Mesaj> mesaje=null;
		ArrayList<Student> participanti=null;
		if(parent.userService instanceof StudentService)
		{
			activitati=((StudentService)parent.userService).viewGroupActivities(this.grup.getId(),true);
			ArrayList<ActivitateGrup> activitati2=((StudentService)parent.userService).viewGroupActivities(this.grup.getId(),false);
			activitati.addAll(activitati2);
			mesaje=((StudentService)parent.userService).viewGroupMessages(this.grup.getId());
			participanti=((StudentService)parent.userService).viewGroupMembers(this.grup.getId());
		}
		else {
			activitati=((AdminService)parent.userService).viewGroupActivities(this.grup.getId());
			mesaje=((AdminService)parent.userService).viewGroupMessages(this.grup.getId());
			participanti=((AdminService)parent.userService).viewGroupMembers(this.grup.getId());
		}
		((GroupDetailsView)view).addActivities(activitati, parent.userService.getSession().getSessionType());
		((GroupDetailsView)view).addMessages(mesaje, parent.userService.getSession().getSessionType(), parent.userService.getSession().getUserID());
		((GroupDetailsView)view).addParticipants(participanti,parent.userService.getSession().getSessionType());
	}
}
