package view.utility;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

import model.entity.Notificare;
import model.enumeration.UserRole;

public class NotificationWindow extends Casette {
	private Notificare notificare;
	public NotificationWindow(Notificare n,UserRole who) {
		this.whoSearched=who;
		this.notificare=n;
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		infos.add(new JLabel(n.getSenderName()));
		infos.add(new JLabel("<html>"+n.getDescriere().replaceAll("\n", "<br>")+"</html>"));
		infos.add(new JLabel("Expirare: "+n.getExpirare()));
		for(JLabel i:infos)
		{
			i.setBorder(new EmptyBorder(4,4,4,4));
			center.add(i);
		}
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		this.setBackground(AppWindow.mainColor);
		infos.get(0).setFont(AppWindow.HIGHLIGHTED_INFO);
		
	}
	public static ArrayList<Casette> toWindows(ArrayList<Notificare> notificari,UserRole who){
	    ArrayList<Casette> rez=null;
	    if(notificari!=null&&notificari.size()>0)
	    {
	        rez=new ArrayList<Casette>();
	        for(Notificare  i:notificari)
	            rez.add(new NotificationWindow(i,who));
	        Collections.reverse(rez);
	    }
	    return rez;
	}
	@Override
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
		if(this.notificare.getSenderID()!=0) {
			if(this.notificare.getDescriere().contains("programat")||this.whoSearched==UserRole.PROFESOR)
			{
				InsertionButton activities=new InsertionButton(notificare.getSenderID(),"Participa");
				activities.addActionListener(actions.get(0));
				activities.setSource(source);
				options.add(activities);
			}
			else {
				InsertionButton activities=new InsertionButton(notificare.getSenderID(),"Accepta");
				activities.addActionListener(actions.get(1));
				activities.setSource(source);
				options.add(activities);
			}
		}
	}
}
