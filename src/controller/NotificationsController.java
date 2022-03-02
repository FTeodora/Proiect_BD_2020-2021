package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import service.ProfesorService;
import service.StudentService;
import view.NotificationsView;
import view.utility.InsertionButton;

public class NotificationsController extends OptionsController{
	private ArrayList<ActionListener> actions;
	public NotificationsController(UserController parent) {
		super(new NotificationsView(),parent);
		actions=new ArrayList<ActionListener>();
		actions.add(new AcceptNotification());
		actions.add(new AcceptGroupNotification());
		((NotificationsView)view).setActions(actions);
	}
	class AcceptGroupNotification implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			InsertionButton source=(InsertionButton)e.getSource();
			((StudentService)parent.userService).joinGroup(source.getValue());
				JOptionPane.showMessageDialog(view, "Invitatia dumneavoastra a fost acceptata!","Succes!",JOptionPane.INFORMATION_MESSAGE);
				((NotificationsView)view).removeNotification(source.getSource());
			}
			
		}
		
	class AcceptNotification implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			InsertionButton source=(InsertionButton)e.getSource();
			if(parent.userService instanceof ProfesorService)
			{
				((ProfesorService)parent.userService).acceptGroupInvitation(source.getValue());
				JOptionPane.showMessageDialog(view, "Invitatia dumneavoastra a fost acceptata!","Succes!",JOptionPane.INFORMATION_MESSAGE);
				
			}
			else {
				
				((StudentService)parent.userService).joinGroupActivity(source.getValue());
				JOptionPane.showMessageDialog(view, "Invitatia dumneavoastra a fost acceptata!","Succes!",JOptionPane.INFORMATION_MESSAGE);
			}
			((NotificationsView)view).removeNotification(source.getSource());
		}
		
	}
@Override public void refresh() {
	((NotificationsView)this.view).setMain(this.parent.userService.viewNotifications(),parent.userService.getSession().getSessionType());
}
}
