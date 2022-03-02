package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import model.entity.Notificare;
import model.enumeration.UserRole;
import view.utility.*;

public class NotificationsView extends OptionsView{
	private final CasettePanel notifications;
	public NotificationsView() {
		super("Notificari");
		notifications=new CasettePanel(new Field("Momentan nu aveti notificari",new JLabel()));
		this.add(notifications,BorderLayout.CENTER);
	}
	public void setMain(ArrayList<Notificare> notificari,UserRole who) {
		notifications.resetFields(NotificationWindow.toWindows(notificari,who));
		
	}
	public void setActions(ArrayList<ActionListener> actions) {
		notifications.setActions(actions);
	}
	public void removeNotification(Field f) {
		notifications.removeField(f);
	}
}
