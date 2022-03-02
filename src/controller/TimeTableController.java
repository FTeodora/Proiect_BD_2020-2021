package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.entity.Activitate;
import service.StudentService;
import view.TimeTableView;
import view.utility.ActivityWindow;
import view.utility.Casette;

public class TimeTableController extends OptionsController{
	public TimeTableController(UserController parent) {
		super(new TimeTableView(parent.userService.getSession().getSessionType()),parent);
		((TimeTableView)view).initializeDays(new ChangeDayListener());
		((TimeTableView)view).addDownloadListener(new DownloadActivitiesListener());
	}
	class ChangeDayListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			((TimeTableView)view).resetActivities(parent.userService.viewActivities(((TimeTableView)view).getDate()), parent.userService.getSession().getSessionType());
		}
	}
	class DownloadActivitiesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Casette> act=((TimeTableView)view).getActivities();
			if(act==null||act.size()==0)
				JOptionPane.showMessageDialog(view, "Nu exista activitati in data selectata!","Eroare",JOptionPane.ERROR_MESSAGE);
			else
			{
				ActivityWindow.toDocument(act,((TimeTableView)view).getDate(),parent.userService.getSession().getSessionName());
				JOptionPane.showMessageDialog(view, "Activitatile au fost descarcate. Verificati-va fisierul de descarcari!","Succes",JOptionPane.INFORMATION_MESSAGE);
				
			}
		}
		
	}
	@Override
	public void refresh() {
		((TimeTableView)view).resetActivities(parent.userService.viewActivities(LocalDateTime.now().toString()), parent.userService.getSession().getSessionType());
	}
}
